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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerWorker implements Runnable {
    private Socket socket;
    private MindroidServerFrame mindroidServerFrame;
    private MessageMarshaller messageMarshaller;

    public MindroidServerWorker(final Socket socket, MindroidServerFrame mindroidServerFrame) {
        this.socket = socket;
        this.mindroidServerFrame = mindroidServerFrame;
        this.messageMarshaller = new MessageMarshaller();
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream,"UTF-8");
            StringBuilder sb = new StringBuilder();
            boolean connected = true;
            while(connected) {
            if(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                sb.append(line);
                if(line.endsWith("}")){
                    MindroidMessage deserializedMsg = messageMarshaller.deserializeMessage(sb.toString());
                    handleMessage(deserializedMsg);
                    sb = new StringBuilder();
                }
                if (line.contains("<close>")) {
                    connected = false;
            }
            }
            //else thread sleep, 5s break TODO
            }


        } catch (IOException e) {
            MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Error while receiving or forwarding a message.");
            console.appendLine("IOException: "+e.getMessage()+"\n");
        }
    }

    private void handleMessage(MindroidMessage deserializedMsg) throws IOException{
        //view log message
        if (deserializedMsg.isLogMessage()) {
            mindroidServerFrame.addContentLine(deserializedMsg);
        }


        //handle registration message
        if (deserializedMsg.getMessageType().equals(MessageType.REGISTRATION)) {
            SocketAddress socketAddress = socket.getRemoteSocketAddress();
            if (socketAddress instanceof InetSocketAddress) {
                //the port was sent as content of the registration message
                int port = Integer.parseInt(deserializedMsg.getContent());
                InetSocketAddress robotAddress = new InetSocketAddress(((InetSocketAddress) socketAddress).getAddress(),port);
                mindroidServerFrame.register(deserializedMsg.getSource(), robotAddress);
                mindroidServerFrame.addContentLine("Local","INFO", deserializedMsg.getSource().getValue()+" was registered.");
            } else {
                throw new IOException("Registration of "+deserializedMsg.getSource().getValue()+" failed.");
            }
        }

        //deliver message meant to be sent to another robot
        if(deserializedMsg.getMessageType().equals(MessageType.MESSAGE)&& !deserializedMsg.isLogMessage() && !deserializedMsg.isBroadcastMessage() ) {
            mindroidServerFrame.addContentLine("Local","INFO", "Message from "+deserializedMsg.getSource().getValue()+" to "+deserializedMsg.getDestination().getValue()+": '"+deserializedMsg.getContent()+"'");
            InetSocketAddress address = mindroidServerFrame.findAddress(deserializedMsg.getDestination());
            sendMessage(deserializedMsg, address);
            }


        //deliver broadcast message
        if (deserializedMsg.getDestination().getValue().equals(Destination.BROADCAST.getValue())) {
            HashMap<Destination, InetSocketAddress> ipMapping = mindroidServerFrame.getIPMapping();
            mindroidServerFrame.addContentLine("Local","INFO", "Message from "+deserializedMsg.getSource().getValue()+" to everyone: '"+deserializedMsg.getContent()+"'");
            for(Map.Entry<Destination, InetSocketAddress> entry : ipMapping.entrySet()) {
                if(!deserializedMsg.getSource().getValue().equals(entry.getKey().getValue())) {
                    InetSocketAddress address = entry.getValue();
                    sendMessage(new MindroidMessage(deserializedMsg.getSource(),entry.getKey(), deserializedMsg.getMessageType(),deserializedMsg.getContent()), address);
                }
            }
        }

    }

    private void sendMessage(MindroidMessage deserializedMsg, InetSocketAddress address) throws IOException{
        if (address==null) {
            throw new IOException("The message to "+deserializedMsg.getDestination().getValue()+" has not been sent because its ip address is not known.");
        }
        try {
            Socket socket = new Socket(address.getAddress(), address.getPort());
            PrintWriter out = new PrintWriter(socket.getOutputStream(),
                    true);

            String serializedMessage = messageMarshaller.serialize(deserializedMsg);

            out.println(serializedMessage);
            out.println("<close>");
            socket.close();
            out.close();
        } catch (IOException e) {
            MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Error while forwarding a message to "+deserializedMsg.getDestination().getValue());
            console.appendLine("IOException: "+e.getMessage()+"\n");
        }
    }
}
