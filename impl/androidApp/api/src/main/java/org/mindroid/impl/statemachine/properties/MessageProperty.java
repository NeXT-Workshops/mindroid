package org.mindroid.impl.statemachine.properties;

import org.mindroid.api.statemachine.properties.IMessageProperty;

/**
 * Created by torben on 10.03.2017.
 */
public class MessageProperty implements IMessageProperty {

    private String msg;
    private String sender;

    public MessageProperty(String msg, String sender){
        this.msg = msg;
        this.sender = sender;
    }

    @Override
    public String getSensder() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

}
