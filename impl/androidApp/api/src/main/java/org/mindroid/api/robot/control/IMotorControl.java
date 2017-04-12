package org.mindroid.api.robot.control;

import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 02.03.2017.
 */

public interface IMotorControl {

    public final static int MOTOR_FORWARD = 0;
    public final static int MOTOR_BACKWARD = 1;


    void setMotorSpeed(EV3PortID motorPort, int speed);
    void setMotorDirection(EV3PortID motorPort, int direction);
    void stop(EV3PortID motorPort);

    public void rotate(EV3PortID motorPort,int angle);
    public void rotateTo(EV3PortID motorPort,int angle);
}
