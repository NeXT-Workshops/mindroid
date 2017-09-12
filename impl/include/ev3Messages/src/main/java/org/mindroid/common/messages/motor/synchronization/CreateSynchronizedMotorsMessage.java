package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.hardware.EV3MotorPort;

public class CreateSynchronizedMotorsMessage {

    private String[] motorPorts;

    public CreateSynchronizedMotorsMessage(){

    }

    public CreateSynchronizedMotorsMessage(String[] motorPorts){
        this.motorPorts = motorPorts;
    }

    public String[] getMotorPorts() {
        return motorPorts;
    }
}
