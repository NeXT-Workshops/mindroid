package org.mindroid.impl.statemachine.properties;

import org.mindroid.api.statemachine.properties.IMessageProperty;
import org.mindroid.api.statemachine.properties.IProperty;

/**
 * Created by torben on 10.03.2017.
 */
public class MessageProperty implements IMessageProperty {

    private String content;
    private String source;

    /**
     *
     * @param content
     * @param source
     */
    public MessageProperty(String content, String source){
        this.content = content;
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public IProperty copy() {
        return new MessageProperty(getContent(),getSource());
    }
}
