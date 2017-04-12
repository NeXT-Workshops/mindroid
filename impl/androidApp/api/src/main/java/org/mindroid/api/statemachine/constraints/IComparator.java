package org.mindroid.api.statemachine.constraints;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.statemachine.properties.IProperty;

/**
 * Created by torben on 10.03.2017.
 */
public interface IComparator extends IConstraint {

    IProperty getProperty();

    boolean evaluate(IRobotContextState context);

}
