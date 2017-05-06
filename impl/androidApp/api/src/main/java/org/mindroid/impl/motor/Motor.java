package org.mindroid.impl.motor;

import org.mindroid.api.motor.IMotor;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.robot.MotorController;

/**
 *  Created by Torbe on 03.05.2017.
 *
 *  This motor is used as an easy motor interface for APILevel 2 and 3.
 */
public class Motor implements IMotor {

    MotorController motorController;
    EV3PortID port;

    public Motor(MotorController motorController, EV3PortID port){
        this.motorController = motorController;
        this.port = port;
    }

    @Override
    public final void forward() {
        motorController.setMotorDirection(port,MotorController.FORWARD);
    }

    @Override
    public final void backward() {
        motorController.setMotorDirection(port,MotorController.BACKWARD);
    }

    @Override
    public final void stop() {
        motorController.stop(port);
    }

    @Override
    public final void setSpeed(int speed) {
        motorController.setMotorSpeed(port,speed);
    }
}
