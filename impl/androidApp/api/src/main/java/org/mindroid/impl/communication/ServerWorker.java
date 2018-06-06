package org.mindroid.impl.communication;

import org.mindroid.api.communication.IMessageListener;
import org.mindroid.common.messages.server.MessageMarshaller;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Felicia Ruppel on 19.04.17.
 *
 * - Handles incoming messages
 */
public class ServerWorker implements Runnable {
    private Socket socket;
    private ArrayList<IMessageListener> listeners;

    private MessengerClient owner;
    private boolean isConnected = false;

    public ServerWorker(final Socket socket,MessengerClient client) {
        this.socket = socket;
        this.listeners = new ArrayList<IMessageListener>();
        this.owner = client;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            StringBuilder sb = new StringBuilder();
            MessageMarshaller messageMarshaller = new MessageMarshaller();
            isConnected = true;
            while(isConnected) {
                if(scanner.hasNextLine())
                {
                    String line = scanner.nextLine();
                    sb.append(line);
                    //TODO finding the end of a message could be solved without "}"
                    if(line.endsWith("}")){
                        MindroidMessage deserializedMsg = messageMarshaller.deserializeMessage(sb.toString());
                        if(listeners != null) {
                            for (IMessageListener listener : listeners) {
                                listener.handleMessage(deserializedMsg);
                            }
                        }
                        sb = new StringBuilder();

                    }
                    if (line.contains("<close>")) {
                        isConnected = false;
                        //Close messenger client
                        inputStream.close();
                        owner.disconnect();
                    }
                }else{
                    //Connection closed
                    isConnected = false; //Stop listening
                    inputStream.close();
                    //Close messenger client
                    owner.disconnect();
                    //Just for testing --> will be shown in app
                    ErrorHandlerManager.getInstance().handleError(new Exception("ServerWorker: Connection closed"),ServerWorker.class,"ServerWorker: Connection closed");
                }
            }


        } catch (IOException e) {
            ErrorHandlerManager.getInstance().handleError(e,ServerWorker.class,e.getMessage());
        }
    }

    protected boolean isConnected() {
        return isConnected;
    }

    /**
     * Adds a message listener to the server worker.
     * The message listener will receive the incoming messages.
     * @param messageListener - msg Listener
     */
    protected void addMessageListener(IMessageListener messageListener){
        listeners.add(messageListener);
    }
}
