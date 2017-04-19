package org.mindroid.api;

import org.mindroid.common.messages.server.MindroidMessage;

/**
 * Created by torben on 19.03.2017.
 */
public interface IMessageListener {

    public void handleMessage(MindroidMessage msg);
}
