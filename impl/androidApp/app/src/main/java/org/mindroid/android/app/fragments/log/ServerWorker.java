package org.mindroid.android.app.fragments.log;

import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.common.messages.server.LogMessageMarshaller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Log Server's Server Worker
 *
 * When a connection to the {@link LogServer} is established an instance of this class will be created with the respective socket.
 * It will then immediately send the current Log through the output stream of the socket and close the connection when its done.
 * Therefore It will not expect any incoming messages!
 *
 */
public class ServerWorker implements Runnable {

    private final Socket socket;


    public ServerWorker(final Socket socket, final GlobalLogger globalLogger){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String log = GlobalLogger.getInstance().getLog();
            if(log != null && log.length() > 0) { //Only print if there is a log available
                out.println(new LogMessageMarshaller().serialize(SettingsProvider.getInstance().getRobotID(), log));
            }
            //Inform other side to close connection and transmission complete
            out.println("<close>");

            closeSocket();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeSocket() throws IOException {
        socket.close();
    }
}
