package org.mindroid.api.robot.context;

import org.mindroid.api.statemachine.ISatisfiedConstraintHandler;
import org.mindroid.api.statemachine.constraints.IConstraint;

import java.util.List;

/**
 * Created by torben on 11.03.2017.
 */
public interface IConstraintEvaluator {

    /**     *
     * @param rcs robot context state object to handle
     */
    public void handleRobotContextState(IRobotContextState rcs);

    /**
     * Subscribe the contstaints to the Evaluator.
     * Subscribed constraints will be evaluated in respect to the RobotContextState
     *
     * @param listener - gets called if the Evaluator found a satisfied Constraint
     * @param statemachine_id - id of the statemachine the constraints belong to
     * @param constraints - the Constraints to subscribe
     */
    void subscribeConstraints(ISatisfiedConstraintHandler listener, String statemachine_id, List<IConstraint> constraints);

    /**
     * Unsubscribes Constraints from the Evaluator.
     * The Constraints wont be evaluated anymore.
     *
     * @param statemachineId statemachine id
     */
    void unsubscribeConstraints(String statemachineId);
}
