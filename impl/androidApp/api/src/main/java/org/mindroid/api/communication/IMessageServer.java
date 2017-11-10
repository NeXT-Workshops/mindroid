package org.mindroid.api.communication;

/**
 *
 * Server running on the robot to receive messages from other robots
 *
 * Created by torben on 04.04.2017.
 */
public interface IMessageServer {
    /*
     *
     * @param listener
     */
    void registerMsgListener(IMessageListener listener);

}
