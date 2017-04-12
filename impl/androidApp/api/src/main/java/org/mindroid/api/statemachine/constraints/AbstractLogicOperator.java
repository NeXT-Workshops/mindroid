package org.mindroid.api.statemachine.constraints;

/**
 * Created by torben on 10.03.2017.
 */
public abstract class AbstractLogicOperator implements ILogicOperator{


    IConstraint constraint_left;
    IConstraint constraint_right;

    public AbstractLogicOperator(IConstraint constraint_left, IConstraint constraint_right){
        this.constraint_left = constraint_left;
        this.constraint_right = constraint_right;
    }

    @Override
    public IConstraint getLeftConstraint() {
        return constraint_left;
    }

    @Override
    public IConstraint getRightConstraint() {
        return constraint_right;
    }

    @Override
    public abstract boolean evaluate(boolean result_left, boolean result_right);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractLogicOperator that = (AbstractLogicOperator) o;

        if (constraint_left != null ? !constraint_left.equals(that.constraint_left) : that.constraint_left != null)
            return false;
        return constraint_right != null ? constraint_right.equals(that.constraint_right) : that.constraint_right == null;
    }

    @Override
    public int hashCode() {
        int result = constraint_left != null ? constraint_left.hashCode() : 0;
        result = 31 * result + (constraint_right != null ? constraint_right.hashCode() : 0);
        return result;
    }
}
