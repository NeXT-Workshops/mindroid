package org.mindroid.impl.communication;

import org.mindroid.api.communication.IMessage;
import org.mindroid.api.communication.IMessenger;
import org.mindroid.common.messages.server.LogLevel;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.common.messages.server.ServerLogMessage;
import org.mindroid.common.messages.server.ServerMessageMarshaller;

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
    public boolean isConnected() {
        return false;
    }

    @Override
    public void sendMessage(String destination, String content) {
        //TODO Allow destination other than server
        LogLevel logLevel;
        if (destination.equals("ServerLog")) {
            logLevel=LogLevel.INFO;
        } else {
            logLevel=LogLevel.MESSAGE;
        }
        ServerLogMessage msgObj = new ServerLogMessage(new RobotId(robotID),logLevel,content);
        sendMessage(msgObj);
    }

    @Override
    public void sendMessage(ServerLogMessage msg) {
        final ServerLogMessage msgFinal = msg;
        Thread client = new Thread() {
            public void run() {
                try {
                    Socket socket = new Socket(serverip, serverport);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(),
                            true);

                    ServerMessageMarshaller serverMessageMarshaller = new ServerMessageMarshaller();
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

}
