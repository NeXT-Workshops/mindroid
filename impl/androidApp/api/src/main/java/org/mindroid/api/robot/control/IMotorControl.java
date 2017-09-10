package org.mindroid.api.robot.control;

import org.mindroid.impl.robot.MotorDirection;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 02.03.2017.
 */

public interface IMotorControl {

    void setMotorSpeed(EV3PortID motorPort, int speed);
    void setMotorDirection(EV3PortID motorPort, MotorDirection direction);
    void stop(EV3PortID motorPort);

    void rotate(EV3PortID motorPort,int angle);

    void rotateTo(EV3PortID motorPort,int angle);
    //TODO implement the other Regulated Motor methods (from interface  RegulatedMotor)
}
