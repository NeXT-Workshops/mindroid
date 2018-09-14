package org.mindroid.api.communication;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.LogLevel;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;

/**
 * Created by torben on 04.04.2017.
 */
public interface IMessenger {

    public static final String SERVER_LOG = Destination.SERVER_LOG.getValue();
    public static final String BROADCAST = Destination.BROADCAST.getValue();


    /**
     * Sends a message to the given destination. If the destination is the server, MessageType INFO is used.
     * For other Log-MessageTypes use sendLogMessage(String content, MessageType type)
     *
     * @param destination - destination of the message
     * @param content - text of the message
     * @param runtimeID - id of the program running on the robot
     */
    void sendMessage(String destination, String content, int runtimeID);


    /**
     *
     * Is called by the Robot Server. Does not need to be called manually.
     *
     * @param port - port of the server
     */
    void registerToServer(int port);

    /**
     * Sends a log message to the server.
     *
     * @param content - content of the message
     * @param logLevel - log level of the message
     */
    void sendLogMessage(String content, LogLevel logLevel);

    /**
     * Returns the state of the connection.
     * @return true if connected
     */
    boolean isConnected();


}
