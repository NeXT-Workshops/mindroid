package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.statemachine.constraints.AbstractSimpleSensorValueComparator;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorPorperty;

/**
 * Created by torben on 10.03.2017.
 */
public class LT extends AbstractSimpleSensorValueComparator {

    public LT(SimpleEV3SensorPorperty property) {
        super(property);
    }

    @Override
    protected boolean evaluate(float value) {
        return value < ((SimpleEV3SensorPorperty)getProperty()).getValue();
    }

}
