package org.mindroid.api.robot.control;

import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.robot.MotorDirection;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 02.03.2017.
 */

public interface MotorProvider {

    Motor getMotor(EV3PortID motorport);
}
