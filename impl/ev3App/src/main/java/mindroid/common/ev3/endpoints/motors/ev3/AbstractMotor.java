package mindroid.common.ev3.endpoints.motors.ev3;

import org.mindroid.common.messages.hardware.Motors;
import lejos.hardware.port.Port;
import lejos.robotics.BaseMotor;

/**
 * Created by torben on 27.01.2017.
 */

public abstract class AbstractMotor {

    protected Port motorPort;
    protected BaseMotor motor;
    protected Motors motortype;

    public AbstractMotor(Port motorPort){
        this.motorPort = motorPort;
    }

    abstract protected BaseMotor createMotor(Port motorPort);

    public BaseMotor getMotor() {
        return motor;
    }

    public void setMotor(BaseMotor motor) {
        this.motor = motor;
    }

    public Port getMotorPort() {
        return motorPort;
    }

    public void setMotorPort(Port motorPort) {
        this.motorPort = motorPort;
    }

    public Motors getMotortype() {
        return motortype;
    }

    public void setMotortype(Motors motortype) {
        this.motortype = motortype;
    }

    /**
     * Closes the Lejos motor-port
     */
    abstract public void close();

    /**
     * Calls forward on the lejos motor Object.
     * Causes motor to rotate forward until stop() or flt() is called.
     * Checks if lejos motor is not null.
     */
    public void forward(){
        if(motor != null) {
            motor.forward();
        }
    }

    /**
     * Calls flt on the lejos motor Object.
     * Motor loses all power, causing the rotor to float freely to a stop.
     * Checks if lejos motor is not null..
     */
    public void flt(){
        if(motor != null){
            motor.flt();
        }
    }

    /**
     * Calls backward on the lejos motor Object.
     * Causes motor to rotate backwards until stop() or flt() is called.
     * Checks if lejos motor is not null..
     */
    public void backward(){
        if(motor != null){
            motor.backward();
        }
    }

    /**
     * Calls stop on the lejos motor Object.
     * Causes motor to stop immediately.
     * Checks if lejos motor is not null..
     */
    public void stop(){
        if(motor != null){
            motor.stop();
        }
    }

    /**
     * Calls isMoving on the lejos motor Object.
     * Return true if the motor is moving.
     * Checks if lejos motor is not null..
     * @return true if motor is moving
     */
    public boolean isMoving(){
        if(motor != null){
            return motor.isMoving();
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMotor that = (AbstractMotor) o;

        if (motorPort != null ? !motorPort.equals(that.motorPort) : that.motorPort != null) return false;
        if (motor != null ? !motor.equals(that.motor) : that.motor != null) return false;
        return motortype == that.motortype;
    }

    @Override
    public int hashCode() {
        int result = motorPort != null ? motorPort.hashCode() : 0;
        result = 31 * result + (motor != null ? motor.hashCode() : 0);
        result = 31 * result + (motortype != null ? motortype.hashCode() : 0);
        return result;
    }
}
