package org.mindroid.api.communication;

import org.mindroid.common.messages.server.MindroidMessage;

/**
 * Created by torben on 19.03.2017.
 */
public interface IMessageListener {

    /**
     * Called by the Server when it received a message.
     * @param msg - message to handle
     */
    public void handleMessage(MindroidMessage msg);

    /**
     * Checks if a message has been received.
     * @return true - if message has been received
     */
    public boolean hasMessage();

    /**
     * Returns the next Message in line (Received Messages).
     * The Message will be deleted and therefore can only accessed once.
     *
     * @return the next Message in line
     */
    public MindroidMessage getNextMessage();

    /**
     * Returns the number of received messages currently stored in memory.
     * @return Number of stored messages
     */
    public int getReceivedMessagesCount();

}
