package org.mindroid.impl.communication;

import org.mindroid.api.IMessageListener;
import org.mindroid.common.messages.server.MessageMarshaller;
import org.mindroid.common.messages.server.MindroidMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Felicia Ruppel on 19.04.17.
 */

public class ServerWorker implements Runnable {
    private Socket socket;
    private IMessageListener listener;

    public ServerWorker(final Socket socket, final IMessageListener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            StringBuilder sb = new StringBuilder();
            MessageMarshaller messageMarshaller = new MessageMarshaller();
            boolean connected =true;
            while(connected) {
                if(scanner.hasNextLine())
                {
                    String line = scanner.nextLine();
                    sb.append(line);
                    //TODO finding the end of a message could be solved without "}"
                    if(line.endsWith("}")){
                        MindroidMessage deserializedMsg = messageMarshaller.deserializeMessage(sb.toString());
                        if(listener != null) {
                            listener.handleMessage(deserializedMsg);
                        }
                        sb = new StringBuilder();

                    }
                    if (line.contains("<close>")) {
                        connected = false;
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            //TODO view exception in app
        }
    }
}
