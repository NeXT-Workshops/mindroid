package org.mindroid.api.communication;

import org.mindroid.api.IMessageListener;

/**
 *
 * Server running on the robot to receive messages from other robots
 *
 * Created by torben on 04.04.2017.
 */
public interface IRobotServer {

    void start();

    void stop();


    /*
     *
     * @param listener
     */
    void registerMsgListener(IMessageListener listener);

    /**
     * Verwendungsidee:
     *
     * IRobotServer server = new Server(tcpport);
     *
     * Wenn der Server Nachrichten bekommt ruft er die listener auf:
     *  >> listener.handleMessage(msg);
     *
     */
}
