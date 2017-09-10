package org.mindroid.common.messages.motor;


public class MotorStateMessage{

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
}


