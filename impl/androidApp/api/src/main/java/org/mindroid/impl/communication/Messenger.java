package org.mindroid.impl.communication;

import org.mindroid.api.communication.IMessageListener;
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
import java.util.ArrayList;

/**
 * Created by Felicia Ruppel on 04.04.17.
 */

public class Messenger implements IMessenger, IMessageListener {

    public static final String SERVER_LOG = Destination.SERVER_LOG.getValue();
    public static final String BROADCAST = Destination.BROADCAST.getValue();

    private String robotID;
    private InetAddress serverip;
    private int serverport;

    private final ArrayList<MindroidMessage> messages = new ArrayList<MindroidMessage>();

    public  Messenger(String ownName, InetAddress serverip, int serverport){
        this.robotID = ownName;
        this.serverip = serverip;
        this.serverport = serverport;
    }


    @Override
    public void sendMessage(String destination, String content) {
        MessageType type;
        if (destination.equals(IMessenger.SERVER_LOG)) {
            type = MessageType.INFO;
        } else {
            type = MessageType.MESSAGE;
        }
        MindroidMessage msgObj = new MindroidMessage(new RobotId(robotID), new Destination(destination), type, content);
        sendMessage(msgObj);
    }

    private void sendMessage(MindroidMessage msg) {
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


    @Override
    public void handleMessage(MindroidMessage msg) {
        getMessages().add(msg);
    }

    @Override
    public int getReceivedMessagesCount(){
        return getMessages().size();
    }

    @Override
    public boolean hasMessage() {
        return getMessages().iterator().hasNext();
    }

    @Override
    public MindroidMessage getNextMessage() {
        return getMessages().iterator().next();
    }

    private ArrayList<MindroidMessage> getMessages(){
        return messages;
    }
}
