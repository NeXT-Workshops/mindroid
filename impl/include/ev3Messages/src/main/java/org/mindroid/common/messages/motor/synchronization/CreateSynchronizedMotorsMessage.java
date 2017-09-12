package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.hardware.EV3MotorPort;

public class CreateSynchronizedMotorsMessage {

    private EV3MotorPort[] motorPorts;

    public CreateSynchronizedMotorsMessage(){

    }

    public CreateSynchronizedMotorsMessage(EV3MotorPort[] motorPorts){
        this.motorPorts = motorPorts;
    }

}
