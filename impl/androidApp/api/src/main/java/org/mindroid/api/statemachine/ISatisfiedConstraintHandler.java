package org.mindroid.api.statemachine;

import org.mindroid.api.robot.context.IRobotContextStateEvaluator;
import org.mindroid.api.statemachine.constraints.IConstraint;

/**
 * Created by torben on 20.03.2017.
 */
public interface ISatisfiedConstraintHandler {

    void handleSatisfiedConstraint(String ID,IConstraint satConstraint);

    void addConstraintEvalauator(IRobotContextStateEvaluator evaluator);
}
