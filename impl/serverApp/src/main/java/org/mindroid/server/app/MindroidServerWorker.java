package org.mindroid.server.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.common.messages.server.*;
import org.mindroid.server.app.util.ADBService;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerWorker implements Runnable {
    private Socket socket;
    private MindroidServerFrame mindroidServerFrame;
    private MessageMarshaller messageMarshaller;
    static MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
    private SessionHandler sessionHandler;
    private UserManagement um = UserManagement.getInstance();



    //Robot Connected to this socket - will be set when robot registers himself
    private RobotId connectedRobot = null;
    private boolean connected = false;

    private Logger LOGGER;

    public MindroidServerWorker(final Socket socket, MindroidServerFrame mindroidServerFrame) {
        LOGGER = LogManager.getLogger(MindroidServerWorker.class);
        this.socket = socket;
        this.mindroidServerFrame = mindroidServerFrame;
        this.messageMarshaller = new MessageMarshaller();

        sessionHandler = SessionHandler.getInstance();
        sessionHandler.setMsw(this);
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream,"UTF-8");
            StringBuilder sb = new StringBuilder();
            connected = true;
            while(connected) {
                if(scanner.hasNextLine())
                {
                    String line = scanner.nextLine();
                    sb.append(line);
                    if(line.endsWith("}")){
                        MindroidMessage deserializedMsg = messageMarshaller.deserializeMessage(sb.toString());
                        handleMessage(deserializedMsg);
                        sb = new StringBuilder();
                    }
                    if (line.contains("<close>") && connected) {
                        um.removeUserAndCloseConnection(connectedRobot);
                    }
                }else {
                    if(connected) {
                        um.removeUserAndCloseConnection(connectedRobot);
                    }
                }
            }
        } catch (IOException e) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Error while receiving or forwarding a message.");
            console.appendLine("IOException: "+e.getMessage()+"\n");
        } catch (ConnectionToRemoteDeviceException | JadbException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the socket.
     *
     * !! NOTE: ONLY CLOSE CONNECTION USING METHODS FROM UserManagement
     */
    public void closeConnection() {
        LOGGER.log(Level.INFO,"Closing Connection of "+connectedRobot);

        //Connection closed
        connected = false; //Stop listening

        //Close Socekt
        disconnect();
    }

    private void disconnect() {
        LOGGER.log(Level.INFO,"Disconnecting robot: "+connectedRobot);
        try {
            //Close Socket
            socket.close();
            if(connectedRobot != null) {
                mindroidServerFrame.addLocalContentLine( "LOG", "Connection to "+getStrRobotID()+" got closed!");
            }else{
                mindroidServerFrame.addLocalContentLine("LOG", "Connection to unknown Robot got closed!");
            }
        } catch (IOException e) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Error while closing a socket");
            console.appendLine("IOException: "+e.getMessage()+"\n");
        }
    }

    private void handleMessage(MindroidMessage msg) throws IOException, ConnectionToRemoteDeviceException, JadbException {
        if(connectedRobot==null){
            connectedRobot=msg.getSource();
        }
        LOGGER.log(Level.INFO,"[Connection "+connectedRobot+"] Handling message: "+msg.toString());

        switch (msg.getMessageType()){
            case LOG:
                LOGGER.info("Handling ServerLog Message, Content: " + msg.getContent());
                mindroidServerFrame.addContentLineFromMessage(msg);
                break;
            case SESSION:
                LOGGER.info("Handling Session Message, Session: " + msg.getSessionRobotCount());
                sessionHandler.handleSessionMessage(msg);
                break;
            case REGISTRATION:
                LOGGER.info("Handling Registration Message");
                registerRobot(msg);
                break;
            case MESSAGE:
                if (sessionHandler.isSessionRunning()){
                    if(msg.isBroadcastMessage()){
                        LOGGER.info("Handling Broadcast Message");
                        mindroidServerFrame.addContentLine(msg.getSource().getValue(), msg.getDestination().getValue(), "LOG", msg.getContent());
                        broadcastMessage(msg);
                    }else{
                        LOGGER.info("Handling Unicast Message");
                        mindroidServerFrame.addContentLine(msg.getSource().getValue(), msg.getDestination().getValue(), "LOG", msg.getContent());
                        Socket socket = um.getSocket(msg.getDestination());
                        sendMessage(msg, socket);
                    }
                }else{
                    LOGGER.warn("No Session Running, message not forwarded");
                }
                break;
            default:
                LOGGER.warn("Message is not conform. Seems to have bad format.");
        }


    }
    public void broadcastMessage(final MindroidMessage msg) {
        RobotId[] destinations = um.getRobotIdsArray();
        LOGGER.info("Sending broadcast message to: " + Arrays.toString(destinations));
        mindroidServerFrame.addContentLine(msg.getSource().getValue(), msg.getDestination().getValue(), "LOG", msg.getContent());
        sendToRobots(destinations, msg);
    }

    public void multicast(RobotId[] destinatins, final MindroidMessage msg){
        LOGGER.info("Sending multicast message to: " + Arrays.toString(destinatins));
        sendToRobots(destinatins, msg);
    }

    private void sendToRobots(RobotId[] destinations, final MindroidMessage msg){
        // V1, start each thread right after creating it

        for(final RobotId destination : destinations) {
            if (!msg.getSource().equals(destination)) {
                Runnable sendMessage = new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(new MindroidMessage(msg.getSource(), destination, msg.getMessageType(), msg.getContent(), msg.getSessionRobotCount()), destination);
                    }
                };
                new Thread(sendMessage).start();
            }
        }

        // V2, collect threads and start them in seperate loop after all threads have been created
        /*
        Set<Thread> threads = null;
        for(final RobotId destination : destinations) {
            if (!msg.getSource().equals(destinations)) {
                Runnable sendMessage = new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(new MindroidMessage(msg.getSource(), destination, msg.getMessageType(), msg.getContent(), msg.getSessionRobotCount()), destination);
                    }
                };
                threads.add(new Thread(sendMessage));
            }
        }
        for (Thread thread : threads){
            thread.start();
        }
        */

    }

    private void registerRobot(MindroidMessage deserializedMsg) throws IOException, JadbException, ConnectionToRemoteDeviceException {
        SocketAddress socketAddress = socket.getRemoteSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            //the port was sent as content of the registration message
            int port = Integer.parseInt(deserializedMsg.getContent());
            boolean isAccepted = um.registerRobot(deserializedMsg.getSource(),this, socket, port);

            if(!isAccepted){
                //Registration got rejected by the server
                mindroidServerFrame.addLocalContentLine("WARNING", "The Connection of "+getStrRobotID()+" got rejected! A Robot with that ID is already connected. Change RobotID to connect.");
                //Stop ServerWorker run-Thread, otherwise calling disconnect will lead to the unregestration of the already connected device with the rejecetd robotID, and disconnect that connection too.
                connected = false;
                //Disconnect this socket as it got rejected.
                disconnect();
                return;
            }


            mindroidServerFrame.addLocalContentLine( "LOG", getStrRobotID()+" was registered.");

            // When device connects, connect adb to device
            LOGGER.info("Device connected, establish adb_tcp");
            ADBService.connectADB((InetSocketAddress) socketAddress);
        } else {
            throw new IOException("Registration of "+deserializedMsg.getSource().getValue()+" failed.");
        }
    }

    public void sendMessage(MindroidMessage msg, RobotId robot){
        try {
            sendMessage(msg, um.getSocket(robot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(MindroidMessage deserializedMsg, Socket socket) throws IOException{
        if (socket==null) {
            throw new IOException("The message to "+deserializedMsg.getDestination().getValue()+" has not been sent because the address is unknown.");
        }
        try {
            //Socket socket = new Socket(address.getAddress(), address.getPort());
            PrintWriter out = new PrintWriter(socket.getOutputStream(),
                    true);

            String serializedMessage = messageMarshaller.serialize(deserializedMsg);

            out.println(serializedMessage);
        } catch (IOException e) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Error while forwarding a message to "+deserializedMsg.getDestination().getValue());
            console.appendLine("IOException: "+e.getMessage()+"\n");
        }
    }

    private String getStrRobotID(){
        return connectedRobot.getValue().concat(" [").concat(socket.getInetAddress().getHostAddress()).concat("]");
    }

    public MindroidServerFrame getMindroidServerFrame() {
        return mindroidServerFrame;
    }
}
