package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class SetMotorSpeedMessage implements ILoggable {
    private int speed;

    public SetMotorSpeedMessage(){}

    public SetMotorSpeedMessage(int speed){
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
