package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.statemachine.constraints.AbstractSimpleSensorValueComparator;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorPorperty;

/**
 * Created by torben on 10.03.2017.
 */
public class EQ extends AbstractSimpleSensorValueComparator {

    public EQ(SimpleEV3SensorPorperty property) {
        super(property);
    }


    @Override
    protected boolean evaluate(float value) {
        return ((SimpleEV3SensorPorperty)getProperty()).getValue() == value;
    }

}
