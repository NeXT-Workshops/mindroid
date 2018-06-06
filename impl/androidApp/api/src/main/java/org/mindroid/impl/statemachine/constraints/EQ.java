package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.statemachine.constraints.AbstractSensorValueComparator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.ComplexEV3SensorProperty;
import org.mindroid.api.statemachine.properties.IEV3SensorPorperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;

/**
 * Created by torben on 10.03.2017.
 */
public class EQ extends AbstractSensorValueComparator {

    public EQ(float value, SimpleEV3SensorProperty property) {
        super(value,property);
    }

    public EQ(float[] values, ComplexEV3SensorProperty property) {
        super(values,property);
    }


    @Override
    protected boolean evaluate(float sample) {
        return (getValue() == sample);
    }

    @Override
    protected boolean evaluate(float[] sample) {
        if(!(sample.length == getValues().length)){
            return false;
        }

        for(int i = 0; i < sample.length;i++){
            if(sample[i] != getValues()[i]){
                return false;
            }
        }
        return true;
    }

    @Override
    public IConstraint copy() {
        switch(evaluationMode){
            case SIMPLE_VALUE_EVALUATION: return new EQ(getValue(), (SimpleEV3SensorProperty) getProperty());
            case COMPLEX_VALUE_EVALUATION: return new EQ(getValues(), (ComplexEV3SensorProperty) getProperty());
            default: throw new IllegalStateException("Cannot copy EQ of type " + evaluationMode);
        }
    }
}
