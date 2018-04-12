package org.mindroid.common.messages.motor;


import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class MotorStateMessage implements ILoggable {

    MotorState motorState;

    public MotorStateMessage() { }

    public MotorStateMessage(MotorState motorState) {
        this.motorState = motorState;
    }

    public MotorState getMotorState() {
        return motorState;
    }

    public void setMotorState(MotorState motorState) {
        this.motorState = motorState;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}


