package org.mindroid.server.app;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.MessageMarshaller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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
            MessageMarshaller messageMarshaller = new MessageMarshaller();
            boolean connected =true;
            while(connected) {
            if(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                sb.append(line);
                if(line.endsWith("}")){
                    MindroidMessage deserializedMsg = messageMarshaller.deserializeMessage(sb.toString());
                    //view log message
                    if (deserializedMsg.isLogMessage()) {
                        mindroidServerFrame.addContentLine(deserializedMsg);
                    }
                    sb = new StringBuilder();

                    //handle registration message
                    if (deserializedMsg.getMessageType().equals(MessageType.REGISTRATION)) {
                        SocketAddress socketAddress = socket.getRemoteSocketAddress();
                        if (socketAddress instanceof InetSocketAddress) {
                            int port = Integer.parseInt(deserializedMsg.getContent());
                            InetSocketAddress robotAddress = new InetSocketAddress(((InetSocketAddress) socketAddress).getAddress(),port);
                            mindroidServerFrame.register(deserializedMsg.getSource(), robotAddress);
                        } else {
                            throw new IOException("Registration of "+deserializedMsg.getSource().getValue()+" failed.");
                        }
                    }

                    //deliver message meant to be sent to another robot
                    if(deserializedMsg.getMessageType().equals(MessageType.MESSAGE)) {
                        InetSocketAddress address = mindroidServerFrame.findAddress(deserializedMsg.getSource());
                        if (address!=null) {
                            Socket socket = new Socket(address.getAddress(),address.getPort());
                            PrintWriter out = new PrintWriter(socket.getOutputStream(),
                                    true);

                            String serializedMessage = messageMarshaller.serialize(deserializedMsg);

                            out.println(serializedMessage);
                            out.println("<close>");
                            socket.close();
                            out.close();

                        } else {
                            throw new IOException("The message to "+deserializedMsg.getDestination().getValue()+" has not been sent because its ip address is not known.");
                        }
                    }
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
