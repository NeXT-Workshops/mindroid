package org.mindroid.server.app.log;

import org.mindroid.common.messages.server.LogMessageMarshaller;
import org.mindroid.common.messages.server.MindroidLogMessage;
import org.mindroid.common.messages.server.NetworkProperties;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.server.app.MindroidServerConsoleFrame;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class LogFetcher {

    public static MindroidLogMessage fetchLog(InetSocketAddress socketAddress){
        try {
            Socket socket = new Socket();
            InetSocketAddress targetAddr = new InetSocketAddress(socketAddress.getAddress(),NetworkProperties.LOG_SERVER.getPort());
            socket.connect(targetAddr);
            InputStream is = socket.getInputStream();
            LogMessageMarshaller logMsgMarshaller = new LogMessageMarshaller();
            MindroidLogMessage deserializedMsg = null;
            StringBuffer sb = new StringBuffer();
            Scanner scanner = new Scanner(is);
            boolean isConnected = true;
            while(isConnected){
                if(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    sb.append(line);
                    //TODO finding the end of a message could be solved without "}"
                    if(line.endsWith("}")){
                        deserializedMsg = logMsgMarshaller.deserialize(sb.toString());
                    }
                    if (line.contains("<close>")) {
                        isConnected = false;
                        //Close messenger client
                        is.close();
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            return deserializedMsg;
        } catch (IOException e1) {
            e1.printStackTrace();
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Couldn't fetch Log: "+e1.getMessage());
            return new MindroidLogMessage(new RobotId("FetchLogFailed"),"null");
        }
    }
}
