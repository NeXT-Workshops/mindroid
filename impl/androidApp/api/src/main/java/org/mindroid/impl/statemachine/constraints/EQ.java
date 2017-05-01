package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.statemachine.constraints.AbstractSimpleSensorValueComparator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.IEV3SensorPorperty;

/**
 * Created by torben on 10.03.2017.
 */
public class EQ extends AbstractSimpleSensorValueComparator {

    public EQ(IEV3SensorPorperty property) {
        super(property);
    }


    @Override
    protected boolean evaluate(float value) {
        return ((IEV3SensorPorperty)getProperty()).getValue() == value;
    }

    @Override
    public IConstraint copy() {
        return new EQ((IEV3SensorPorperty) getProperty().copy());
    }
}
