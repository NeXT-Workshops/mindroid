package org.mindroid.api.robot.context;

import org.mindroid.api.statemachine.ISatisfiedConstraintHandler;
import org.mindroid.api.statemachine.constraints.IConstraint;

import java.util.List;

/**
 * Created by torben on 11.03.2017.
 */
public interface IRobotContextStateEvaluator {

    public void evaluateConstraints(IRobotContextState rcs);

    public void subscribeConstraints(ISatisfiedConstraintHandler listener,String statemachine_id, List<IConstraint> constraints);

}
