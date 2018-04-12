package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class BackwardMessage implements ILoggable {
    BackwardMessage(){

    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
