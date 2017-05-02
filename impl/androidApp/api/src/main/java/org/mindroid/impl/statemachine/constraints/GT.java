package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.statemachine.constraints.AbstractSensorValueComparator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.ComplexEV3SensorProperty;
import org.mindroid.api.statemachine.properties.IEV3SensorPorperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;


/**
 * Created by torben on 10.03.2017.
 */
public class GT extends AbstractSensorValueComparator {

    public GT(float value, SimpleEV3SensorProperty property) {
        super(value,property);
    }

    public GT(float[] values, ComplexEV3SensorProperty property) {
        super(values,property);
    }

    @Override
    protected boolean evaluate(float sample) {
        return sample > getValue();
    }

    @Override
    protected boolean evaluate(float[] sample) {
        if(!(sample.length == getValues().length)){
            return false;
        }

        for(int i = 0; i < sample.length;i++){
            if(sample[i] <= getValues()[i]){
                return false;
            }
        }
        return true;
    }

    @Override
    public IConstraint copy() {
        switch(evaluationMode){
            case SIMPLE_VALUE_EVALUATION: return new GT(getValue(), (SimpleEV3SensorProperty) getProperty());
            case COMPLEX_VALUE_EVALUATION: return new GT(getValues(), (ComplexEV3SensorProperty) getProperty());
            default: return null;
        }
    }
}
