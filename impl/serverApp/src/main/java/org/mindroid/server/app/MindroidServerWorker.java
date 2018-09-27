package org.mindroid.server.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.common.messages.server.*;
import org.mindroid.server.app.util.ADBService;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerWorker implements Runnable {
    private Socket socket;
    private MindroidServerFrame mindroidServerFrame;
    private MessageMarshaller messageMarshaller;
    static MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
    private SessionHandler sessionHandler;


    //Robot Connected to this socket - will be set when robot registers himself
    private String connectedRobot = null;
    private boolean connected = false;

    private Logger logger;

    public MindroidServerWorker(final Socket socket, MindroidServerFrame mindroidServerFrame) {
        logger = LogManager.getLogger(MindroidServerWorker.class);
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
                    if (line.contains("<close>")) {
                        UserManagement.getInstance().removeUserAndCloseConnection(connectedRobot);
                    }
                }else {
                    UserManagement.getInstance().removeUserAndCloseConnection(connectedRobot);
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
        logger.log(Level.INFO,"Closing Connection of "+connectedRobot);

        //Connection closed
        connected = false; //Stop listening

        //Close Socekt
        disconnect();
    }

    private void disconnect() {
        logger.log(Level.INFO,"Disconnecting robot: "+connectedRobot);
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

    private void handleMessage(MindroidMessage deserializedMsg) throws IOException, ConnectionToRemoteDeviceException, JadbException {
        logger.log(Level.INFO,"[Connection "+connectedRobot+"] Handling message: "+deserializedMsg.toString());
        console.appendLine("INCOMIIIIIIIING");
        console.appendLine(deserializedMsg.toString());
        // Append to Server Log
        if (deserializedMsg.isLogMessage()) {
            console.appendLine("logmessage");
            mindroidServerFrame.addContentLineFromMessage(deserializedMsg);
        }

        // Session Messages
        if (deserializedMsg.isSessionMessage()){
            console.appendLine("session msg" + deserializedMsg.getSessionRobotCount());
            sessionHandler.handleSessionMessage(deserializedMsg);
        }

        // Registration Message
        if (deserializedMsg.isRegistrationMessage()) {
            console.appendLine("registerRobot");
            SocketAddress socketAddress = socket.getRemoteSocketAddress();
            if (socketAddress instanceof InetSocketAddress) {
                //the port was sent as content of the registration message
                int port = Integer.parseInt(deserializedMsg.getContent());
                //Store owner 'robotID' of this Worker
                connectedRobot = deserializedMsg.getSource().getValue();

                boolean isAccepted = UserManagement.getInstance().registerRobot(deserializedMsg.getSource(),this, socket, port);

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
                System.out.println("Device connected, establish adb_tcp");
                ADBService.connectADB((InetSocketAddress) socketAddress);
            } else {
                throw new IOException("Registration of "+deserializedMsg.getSource().getValue()+" failed.");
            }
        }

        if( sessionHandler.isSessionRunning()) {
            mindroidServerFrame.addLocalContentLine("WARN", "Message received, Session running");

            // Unicast Message
            if (deserializedMsg.isUnicastMessage()) {
                mindroidServerFrame.addContentLine(deserializedMsg.getSource().getValue(), deserializedMsg.getDestination().getValue(), "LOG", deserializedMsg.getContent(), String.valueOf(deserializedMsg.getSessionRobotCount()));
                Socket socket = UserManagement.getInstance().getSocket(deserializedMsg.getDestination());
                sendMessage(deserializedMsg, socket);
                //mindroidServerFrame.addContentLine("SERVER",deserializedMsg.getDestination().getValue(),"DEBUG","MSG["+deserializedMsg.getContent()+"] sent to destination", "-");
            }
            //deliver broadcast message
            if (deserializedMsg.isBroadcastMessage()) {
                broadcastMessage(deserializedMsg);
            }
        }

    }
    public void broadcastMessage(MindroidMessage msg) throws IOException {
        Map<Destination, Socket> socketMapping = UserManagement.getInstance().getSocketMapping();
        mindroidServerFrame.addContentLine(msg.getSource().getValue(), msg.getDestination().getValue(), "LOG", msg.getContent(), String.valueOf(msg.getSessionRobotCount()));
        for(Map.Entry<Destination, Socket> entry : socketMapping.entrySet()) {
            if(!msg.getSource().getValue().equals(entry.getKey().getValue())) {
                Socket address = entry.getValue();
                console.appendLine("broadcasting" + entry.getKey().toString());

                sendMessage(new MindroidMessage(msg.getSource(),msg.getMessageType(), msg.getContent(), entry.getKey(),  msg.getSessionRobotCount()), address);
            }
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
        return connectedRobot.concat(" [").concat(socket.getInetAddress().getHostAddress()).concat("]");
    }

    public MindroidServerFrame getMindroidServerFrame() {
        return mindroidServerFrame;
    }
}
