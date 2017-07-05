package org.mindroid.api;

import org.mindroid.api.communication.IMessage;

/**
 * Created by torben on 19.03.2017.
 */
public interface IMessageListener {

    public void handleMessage(IMessage msg);
}
