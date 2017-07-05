package org.mindroid.api.statemachine;

import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.api.robot.control.ISensorControl;
import org.mindroid.api.statemachine.exception.StateAlreadyExsists;

/**
 * Created by Torben on 01.03.2017.
 */

public interface IMindroidMain {
    IStatemachine getStatemachine() throws StateAlreadyExsists;

}
