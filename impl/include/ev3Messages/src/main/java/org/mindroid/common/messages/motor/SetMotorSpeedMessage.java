package org.mindroid.common.messages.motor;

public class SetMotorSpeedMessage {
    private int speed;

    public SetMotorSpeedMessage(){}

    public SetMotorSpeedMessage(int speed){
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }


}
