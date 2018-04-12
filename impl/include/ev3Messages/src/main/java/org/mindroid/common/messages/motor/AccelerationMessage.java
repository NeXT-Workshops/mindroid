package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class AccelerationMessage implements ILoggable {

    private int acceleration = -1;

    public AccelerationMessage(int acceleration){

    }

    public int getAcceleration() {
        return acceleration;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
