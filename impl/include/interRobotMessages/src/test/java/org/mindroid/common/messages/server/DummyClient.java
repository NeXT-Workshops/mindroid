package org.mindroid.common.messages.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Roland Kluge - Initial implementation
 */
public class DummyClient {

    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost", 33044);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),
                    true);

            MessageMarshaller messageMarshaller = new MessageMarshaller();

            String serializedMessage = messageMarshaller.serialize(new MindroidMessage(new RobotId("Robot 1"), Destination.SERVER_LOG, MessageType.INFO, "Everything is awesome!"));
            out.println(serializedMessage);

            String serializedMessage2 = messageMarshaller.serialize(new MindroidMessage(new RobotId("Robot 2"), Destination.SERVER_LOG, MessageType.INFO, "Hello!"));
            out.println(serializedMessage2);

            String serializedMessage3 = messageMarshaller.serialize(new MindroidMessage(new RobotId("Robot 3"), Destination.SERVER_LOG, MessageType.INFO, "Hi there!"));
            out.println(serializedMessage3);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out.println("<close>");
            socket.close();
            out.close();

            socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 33044);
            out = new PrintWriter(socket.getOutputStream(),
                    true);

            messageMarshaller = new MessageMarshaller();

             serializedMessage = messageMarshaller.serialize(new MindroidMessage(new RobotId("Robot 1"), Destination.SERVER_LOG, MessageType.INFO, "Everything is awesome!"));
            out.println(serializedMessage);


            serializedMessage2 = messageMarshaller.serialize(new MindroidMessage(new RobotId("Robot 2"), Destination.SERVER_LOG, MessageType.INFO, "Hello!"));
            out.println(serializedMessage2);

            serializedMessage = messageMarshaller.serialize(new MindroidMessage(new RobotId("Robot 3"), Destination.SERVER_LOG, MessageType.INFO, "Hi!"));
            out.println(serializedMessage);

            out.println("<close>");
            socket.close();
            out.close();


        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e);
            System.exit(1);
        } catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }
}
