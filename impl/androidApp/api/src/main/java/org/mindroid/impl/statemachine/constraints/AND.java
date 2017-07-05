package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.statemachine.constraints.AbstractLogicOperator;
import org.mindroid.api.statemachine.constraints.IConstraint;

/**
 * Created by torben on 10.03.2017.
 */
public class AND extends AbstractLogicOperator {


    public AND(IConstraint constraint_left, IConstraint constraint_right) {
        super(constraint_left, constraint_right);
    }

    @Override
    public boolean evaluate(boolean result_left, boolean result_right) {
        return result_left && result_right;
    }

}
