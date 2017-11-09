package org.mindroid.impl.communication;

import org.mindroid.api.communication.IMessageListener;
import org.mindroid.api.communication.IMessageServer;
import org.mindroid.api.communication.IMessenger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *  Created by Felicia Ruppel on 19.04.17.
 *
 *  Receives Inter Robot Messages
 *
 */

public class MessageServer implements IMessageServer {
    private final ArrayList<IMessageListener> listeners = new ArrayList();
    private ServerSocket server = null;
    private int port;
    private static boolean isRunning = false;
    private IMessenger messenger;

    /**
     *
     * @param port
     * @param messenger is needed to register the robot server at the main server
     */
    public MessageServer(int port, IMessenger messenger){
        this.port = port;
        this.messenger = messenger;
    }

    @Override
    public void start() {
        Thread mainServerThread = new Thread() {
        public void run() {
            if (!isRunning) {
                try {

                    server = new ServerSocket(port);
                    messenger.registerToServer(port);
                    isRunning = true;

                } catch (IOException e) {
                    //TODO view in app
                    e.printStackTrace();
                }

                Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread th, Throwable ex) {
                        //TODO view in app
                        ex.printStackTrace();
                    }
                };

                int iteration = 0;
                while (true)

                {
                    ++iteration;
                    ServerWorker w;
                    try {
                        w = new ServerWorker(server.accept(), listeners);
                        Thread workerThread = new Thread(w);
                        workerThread.setUncaughtExceptionHandler(exceptionHandler);
                        workerThread.start();
                    } catch (IOException e) {
                        //TODO view in app
                        e.printStackTrace();
                    }
                }
            } else {
                messenger.registerToServer(port);
            }
        }

        };
        mainServerThread.start();
    }

    @Override
    public void stop() {
        //TODO
        //isRunning = false;
    }

    @Override
    public void registerMsgListener(IMessageListener listener) {
        listeners.add(listener);
    }
}
