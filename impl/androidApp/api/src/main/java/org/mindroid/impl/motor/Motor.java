package org.mindroid.impl.motor;

import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.robot.MotorController;
import org.mindroid.impl.robot.MotorDirection;

/**
 *  Created by Torbe on 03.05.2017.
 *
 *  This motor is used as an easy motor interface for APILevel 2
 */
public class Motor implements RegulatedMotor {

    MotorController motorController;
    EV3PortID port;

    public Motor(MotorController motorController, EV3PortID port){
        this.motorController = motorController;
        this.port = port;
    }

    @Override
    public final void forward() {
        motorController.setMotorDirection(port, MotorDirection.FORWARD);
    }

    @Override
    public final void backward() {
        motorController.setMotorDirection(port, MotorDirection.BACKWARD);
    }

    @Override
    public final void stop() {
        motorController.stop(port);
    }

    @Override
    public final void setSpeed(int speed) {
        motorController.setMotorSpeed(port,speed);
    }

    @Override
    public void rotate(int angle) {
        //TODO implement
    }

    @Override
    public void rotateTo(int angle) {
        //TODO implement
    }

    @Override
    public int getRotationSpeed() {
        //TODO implement
        return 0;
    }

    @Override
    public int getLimitAngle() {
        //TODO implement
        return 0;
    }

    @Override
    public int getAcceleration() {
        //TODO implement
        return 0;
    }

    @Override
    public int getTachoCount() {
        //TODO implement
        return 0;
    }

    @Override
    public float getPosition() {
        //TODO implement
        return 0;
    }

    @Override
    public float getMaxSpeed() {
        //TODO implement
        return 0;
    }
}
