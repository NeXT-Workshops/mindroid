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
        if(motorEndpoint != null) {
            motorEndpoint.forward();
        }
    }

    @Override
    public final void backward() {
        if(motorEndpoint != null) {
            motorEndpoint.backward();
        }
    }

    @Override
    public final void stop() {
        if(motorEndpoint != null) {
            motorEndpoint.stop();
        }
    }

    @Override
    public void flt() {
        if(motorEndpoint != null) {
            motorEndpoint.flt();
        }
    }

    @Override
    public void stop(boolean immediateReturn) {
        if(motorEndpoint != null) {
            motorEndpoint.stop(immediateReturn);
        }
    }

    @Override
    public final void setSpeed(int speed) {
        if(motorEndpoint != null) {
            motorEndpoint.setSpeed(speed);
        }
    }

    @Override
    public void rotate(int angle) {
        if(motorEndpoint != null) {
            motorEndpoint.rotate(angle);
        }
    }

    @Override
    public void rotateTo(int angle) {
        if(motorEndpoint != null) {
            motorEndpoint.rotateTo(angle);
        }
    }

    @Override
    public void flt(boolean immediateReturn) {
        if(motorEndpoint != null) {
            motorEndpoint.flt(immediateReturn);
        }
    }

    @Override
    public void rotate(int angle, boolean immediateReturn) {
        if(motorEndpoint != null) {
            motorEndpoint.rotate(angle, immediateReturn);
        }
    }

    @Override
    public void rotateTo(int limitAngle, boolean immediateReturn) {
        if(motorEndpoint != null) {
            motorEndpoint.rotateTo(limitAngle, immediateReturn);
        }
    }

    @Override
    public int getRotationSpeed() {
        if(motorEndpoint != null) {
            return motorEndpoint.getRotationSpeed();
        }
        return -1;
    }

    @Override
    public int getLimitAngle() {
        if(motorEndpoint != null) {
            return motorEndpoint.getLimitAngle();
        }
        return -1;
    }

    @Override
    public int getAcceleration() {
        if(motorEndpoint != null) {
            return motorEndpoint.getAcceleration();
        }
        return -1;
    }

    @Override
    public int getTachoCount() {
        if(motorEndpoint != null) {
            return motorEndpoint.getTachoCount();
        }
        return -1;
    }

    @Override
    public float getPosition() {
        if(motorEndpoint != null) {
            return motorEndpoint.getPosition();
        }
        return -1f;
    }

    @Override
    public int getSpeed() {
        if(motorEndpoint != null) {
            return motorEndpoint.getSpeed();
        }
        return -1;
    }

    @Override
    public float getMaxSpeed() {
        if(motorEndpoint != null) {
            return motorEndpoint.getMaxSpeed();
        }
        return -1f;
    }

    @Override
    public boolean isMoving() {
        if(motorEndpoint != null) {
            return motorEndpoint.isMoving();
        }
        return false;
    }

    public EV3PortID getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Motor motor = (Motor) o;

        if (motorEndpoint != null ? !motorEndpoint.equals(motor.motorEndpoint) : motor.motorEndpoint != null)
            return false;
        return port != null ? port.equals(motor.port) : motor.port == null;
    }

    @Override
    public int hashCode() {
        int result = motorEndpoint != null ? motorEndpoint.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }
}
