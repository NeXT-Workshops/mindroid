package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class ForwardMessage implements ILoggable {
    public ForwardMessage(){

    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
