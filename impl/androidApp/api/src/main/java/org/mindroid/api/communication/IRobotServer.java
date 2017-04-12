package org.mindroid.api.communication;

import org.mindroid.api.IMessageListener;

/**
 *
 * Server, der auf dem Roboter läuft, um Nachrichten von anderen Robotern zu empfangen.
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
