package org.mindroid.api.statemachine.constraints;

/**
 * Created by torben on 10.03.2017.
 */
public interface ILogicOperator extends IConstraint{

    IConstraint getLeftConstraint();
    IConstraint getRightConstraint();

    boolean evaluate(boolean result_left, boolean result_right);

}
