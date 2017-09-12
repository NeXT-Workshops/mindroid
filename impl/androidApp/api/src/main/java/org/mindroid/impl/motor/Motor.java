package org.mindroid.impl.motor;

import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.impl.ev3.EV3PortID;

/**
 *  Created by Torbe on 03.05.2017.
 *
 *  This Class is used by the user to control the Motor at a specified port hiding the Network stuff.
 */
public class Motor implements RegulatedMotor {

    private EV3RegulatedMotorEndpoint motorEndpoint;
    private EV3PortID port;

    public Motor(EV3RegulatedMotorEndpoint motorEndpoint, EV3PortID port){
        this.motorEndpoint = motorEndpoint;
        this.port = port;
    }

    @Override
    public final void forward() {
        motorEndpoint.forward();
    }

    @Override
    public final void backward() {
        motorEndpoint.backward();
    }

    @Override
    public final void stop() {
        motorEndpoint.stop();
    }

    @Override
    public void flt() {
        motorEndpoint.flt();
    }

    @Override
    public void stop(boolean immediateReturn) {
        motorEndpoint.stop(immediateReturn);
    }

    @Override
    public final void setSpeed(int speed) {
        motorEndpoint.setSpeed(speed);
    }

    @Override
    public void rotate(int angle) {
        motorEndpoint.rotate(angle);
    }

    @Override
    public void rotateTo(int angle) {
        motorEndpoint.rotateTo(angle);
    }

    @Override
    public void flt(boolean immediateReturn) {
        motorEndpoint.flt(immediateReturn);
    }

    @Override
    public void rotate(int angle, boolean immediateReturn) {
        motorEndpoint.rotate(angle,immediateReturn);
    }

    @Override
    public void rotateTo(int limitAngle, boolean immediateReturn) {
        motorEndpoint.rotateTo(limitAngle, immediateReturn);
    }

    @Override
    public int getRotationSpeed() {
        return motorEndpoint.getRotationSpeed();
    }

    @Override
    public int getLimitAngle() {
        return motorEndpoint.getLimitAngle();
    }

    @Override
    public int getAcceleration() {
        return motorEndpoint.getAcceleration();
    }

    @Override
    public int getTachoCount() {
        return motorEndpoint.getTachoCount();
    }

    @Override
    public float getPosition() {
        return motorEndpoint.getPosition();
    }

    @Override
    public int getSpeed() {
        return motorEndpoint.getSpeed();
    }

    @Override
    public float getMaxSpeed() {
        return motorEndpoint.getMaxSpeed();
    }

    @Override
    public boolean isMoving() {
        return motorEndpoint.isMoving();
    }



    public EV3PortID getPort() {
        return port;
    }
}
