package org.mindroid.server.app;

import org.mindroid.common.messages.server.*;
import org.mindroid.server.app.util.ADBService;
import org.mindroid.server.app.util.IPService;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerWorker implements Runnable, IUserAction {
    private Socket socket;
    private MindroidServerFrame mindroidServerFrame;
    private MessageMarshaller messageMarshaller;
    static MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
    private SessionHandler sessionHandler;


    //Robot Connected to this socket - will be set when robot registers himself
    private String connectedRobot = null;
    private boolean connected = false;

    public MindroidServerWorker(final Socket socket, MindroidServerFrame mindroidServerFrame) {
        this.socket = socket;
        this.mindroidServerFrame = mindroidServerFrame;
        this.messageMarshaller = new MessageMarshaller();
        sessionHandler = new SessionHandler(this);
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
                        closeConnection();
                    }
                }else {
                    closeConnection();
                }
            }
        } catch (IOException e) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Error while receiving or forwarding a message.");
            console.appendLine("IOException: "+e.getMessage()+"\n");
        } catch (ConnectionToRemoteDeviceException e) {
            e.printStackTrace();
        } catch (JadbException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the socket. Removes the Registration.
     */
    private void closeConnection() {
        //Connection closed
        connected = false; //Stop listening
        //Close Socekt
        disconnect();
        removeRegistration();
    }

    private void disconnect() {
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

    private void removeRegistration(){
        if(connectedRobot != null) {
            mindroidServerFrame.removeRegistration(connectedRobot);
        }
    }

    private void handleMessage(MindroidMessage deserializedMsg) throws IOException, ConnectionToRemoteDeviceException, JadbException {
        // Append to Server Log
        if (deserializedMsg.isLogMessage()) {
            mindroidServerFrame.addContentLineFromMessage(deserializedMsg);
        }

        // Session Messages
        if (deserializedMsg.isSessionMessage()){
            sessionHandler.handleSessionMessage(deserializedMsg);
        }

        // Registration Message
        if (deserializedMsg.isRegistrationMessage()) {
            SocketAddress socketAddress = socket.getRemoteSocketAddress();
            if (socketAddress instanceof InetSocketAddress) {
                //the port was sent as content of the registration message
                int port = Integer.parseInt(deserializedMsg.getContent());
                boolean isAccepted = mindroidServerFrame.register(deserializedMsg.getSource(), socket, (InetSocketAddress) socketAddress, port);
                connectedRobot = deserializedMsg.getSource().getValue(); //Save registered robotID

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
            mindroidServerFrame.addLocalContentLine("WARN", "Message received, but no Session running");
            // Unicast Message
            if (deserializedMsg.isUnicastMessage()) {
                mindroidServerFrame.addContentLine(deserializedMsg.getSource().getValue(), deserializedMsg.getDestination().getValue(), "LOG", deserializedMsg.getContent(), String.valueOf(deserializedMsg.getSessionRobotCount()));
                Socket socket = IPService.findSocket(deserializedMsg.getDestination());
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
        HashMap<Destination, Socket> socketMapping = IPService.getSocketMapping();
        mindroidServerFrame.addContentLine(msg.getSource().getValue(), msg.getDestination().getValue(), "LOG", msg.getContent(), String.valueOf(msg.getSessionRobotCount()));
        for(Map.Entry<Destination, Socket> entry : socketMapping.entrySet()) {
            if(!msg.getSource().getValue().equals(entry.getKey().getValue())) {
                Socket address = entry.getValue();
                sendMessage(new MindroidMessage(msg.getSource(),entry.getKey(), msg.getMessageType(),msg.getContent()), address);
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


    @Override
    public void kickUser(String username) {
        if(this.connectedRobot.equals(username)){
            closeConnection();
        }
    }

    public MindroidServerFrame getMindroidServerFrame() {
        return mindroidServerFrame;
    }
}
