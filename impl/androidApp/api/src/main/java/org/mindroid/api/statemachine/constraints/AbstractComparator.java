package org.mindroid.api.statemachine.constraints;

import org.mindroid.api.statemachine.properties.IMessageProperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.ITimeProperty;

/**
 * Created by torben on 10.03.2017.
 */
public abstract class AbstractComparator implements IComparator {

    /**
     * Validate a float value with respect to the given property
     */
    protected final int SIMPLE_VALUE_EVALUATION = 1;

    /**
     * Validate a float-array with respect to the given property
     */
    protected final int COMPLEX_VALUE_EVALUATION = 2;

    IProperty property;
    protected int evaluationMode = -1;

    protected float value;
    protected float[] values;

    public AbstractComparator(IProperty property){
        this.property = property;
    }

    @Override
    public IProperty getProperty() {
        return property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractComparator that = (AbstractComparator) o;

        return property != null ? property.equals(that.property) : that.property == null;
    }

    @Override
    public int hashCode() {
        return property != null ? property.hashCode() : 0;
    }

    public int getEvaluationMode() {
        return evaluationMode;
    }

    public float getValue() {
        return value;
    }

    public float[] getValues() {
        return values;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
