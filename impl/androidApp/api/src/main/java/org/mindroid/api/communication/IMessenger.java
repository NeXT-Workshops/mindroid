package org.mindroid.api.communication;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;

/**
 * Created by torben on 04.04.2017.
 */
public interface IMessenger {

    public static final String SERVER_LOG = Destination.SERVER_LOG.getValue();

    /**
     * Sends a message to the given destination. If the destination is the server, MessageType INFO is used.
     * For other MessageTypes use sendLogMessage(String content, MessageType type)
     *
     */
    void sendMessage(String destination, String msg);

    void sendMessage(MindroidMessage msg);

    /**
     * Is called by the Robot Server. Does not need to be called manually.
     */
    void registerToServer(int port);
    /**
     * Sends a log message to the server.
     *
     * @param content
     * @param type
     */
    void sendLogMessage(String content, MessageType type);



}
