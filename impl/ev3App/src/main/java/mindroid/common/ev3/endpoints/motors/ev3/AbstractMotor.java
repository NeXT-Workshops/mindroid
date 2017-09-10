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
}
