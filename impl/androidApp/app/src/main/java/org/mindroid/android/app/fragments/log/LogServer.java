package org.mindroid.android.app.fragments.log;

import android.provider.Settings;
import org.mindroid.common.messages.server.NetworkProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * ServerSocket to access Log File from the phone
 */
public class LogServer {

    ServerSocket serverSocket;

    private final GlobalLogger globalLogger;

    public LogServer(GlobalLogger globalLogger) {
        this.globalLogger = globalLogger;
    }

    public void openServer(){
        try {
            serverSocket = new ServerSocket(NetworkProperties.LOG_SERVER.getPort());
            Runnable listen = new Runnable() {
                @Override
                public void run() {
                    try {
                        while(!serverSocket.isClosed()) {
                            Socket socket = serverSocket.accept();
                            ServerWorker sw = new ServerWorker(socket, globalLogger);
                            new Thread(sw).start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            new Thread(listen).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServer(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
