package org.mindroid.impl.communication;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.LogLevel;
import org.mindroid.common.messages.server.MessageMarshaller;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Felicia Ruppel on 04.04.17.
 */

public class Messenger implements IMessenger {

    private String robotID;
    private InetAddress serverip;
    private int serverport;

    public  Messenger(String ownName, InetAddress serverip, int serverport){
        this.robotID = ownName;
        this.serverip = serverip;
        this.serverport = serverport;
    }


    @Override
    public synchronized void sendMessage(String destination, String content) {
        MessageType type;
        if (destination.equals(IMessenger.SERVER_LOG)) {
            type = MessageType.INFO;
        } else {
            type = MessageType.MESSAGE;
        }
        MindroidMessage msgObj = new MindroidMessage(new RobotId(robotID), new Destination(destination), type, content);
        sendMessage(msgObj);
    }

    @Override
    public synchronized void sendMessage(MindroidMessage msg) {
        final MindroidMessage msgFinal = msg;
        Thread client = new Thread() {
            public void run() {
                try {
                    Socket socket = new Socket(serverip, serverport);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(),
                            true);

                    MessageMarshaller serverMessageMarshaller = new MessageMarshaller();
                    String serializedMessage = serverMessageMarshaller.serialize(msgFinal);

                    out.println(serializedMessage);
                    out.println("<close>");
                    socket.close();
                    out.close();

                }  catch (IOException e) {
                    e.printStackTrace();
                    //TODO view Exception in app
                }
            }
        };
        new Thread(client).start();
    }


    @Override
    public void registerToServer(int port) {
        MindroidMessage msgObj = new MindroidMessage(new RobotId(robotID), Destination.SERVER_LOG, MessageType.REGISTRATION, ""+port);
        sendMessage(msgObj);
    }

    @Override
    public synchronized void sendLogMessage(String content, LogLevel logLevel) {
        MindroidMessage msgObj = new MindroidMessage(new RobotId(robotID), Destination.SERVER_LOG, logLevel.getMessageType(), content);
        sendMessage(msgObj);
    }


}
