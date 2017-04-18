package org.mindroid.api.communication;

import org.mindroid.common.messages.server.ServerLogMessage;

/**
 * Created by torben on 04.04.2017.
 */
public interface IMessenger {

    public static final String SERVER_LOG = "ServerLog";

    /**
     *
     * Sendet eine Message.
     *
     */
    void sendMessage(String destination, String msg);

    void sendMessage(ServerLogMessage msg);

    /**
     *
     * Verbindungsstatus
     *
     *
     *
     * @return
     */
    boolean isConnected();


    /**
     * Verwendung:
     *
     * IMessenger messenger = new Messenger(ownName, serverip,serverport);
     *
     * Kommunikation mit anderen Robotern:
     * messenger.sendMessage("Peter","Hallo!");
     *
     * Lognachrichten:
     * messenger.sendMessage("ServerLog","Lognachricht");
     *
     */
}
