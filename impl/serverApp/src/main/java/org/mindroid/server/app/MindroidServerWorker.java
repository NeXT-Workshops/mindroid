package org.mindroid.server.app;

import org.mindroid.common.messages.server.LogLevel;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.common.messages.server.ServerLogMessage;
import org.mindroid.common.messages.server.ServerMessageMarshaller;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerWorker implements Runnable {
    private Socket socket;
    private MindroidServerFrame mindroidServerFrame;

    public MindroidServerWorker(final Socket socket, MindroidServerFrame mindroidServerFrame) {
        this.socket = socket;
        this.mindroidServerFrame = mindroidServerFrame;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            StringBuilder sb = new StringBuilder();
            ServerMessageMarshaller serverMessageMarshaller = new ServerMessageMarshaller();
            boolean connected =true;
            while(connected) {
            if(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                sb.append(line);
                if(line.endsWith("}")){
                    ServerLogMessage deseriaLogMessage = serverMessageMarshaller.deserializeLogMessage(sb.toString());
                    mindroidServerFrame.addContentLine(deseriaLogMessage);
                    sb = new StringBuilder();
                }
                if (line.contains("<close>")) {
                    connected = false;
            }
            }
            //else thread sleep, 5s break TODO
            }


        } catch (IOException e) {
            e.printStackTrace();
            //2. Fenster TODO
        }
    }
}
