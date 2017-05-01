package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.statemachine.constraints.AbstractSimpleSensorValueComparator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.IEV3SensorPorperty;

/**
 * Created by torben on 10.03.2017.
 */
public class LT extends AbstractSimpleSensorValueComparator {

    public LT(IEV3SensorPorperty property) {
        super(property);
    }

    @Override
    protected boolean evaluate(float value) {
        return value < ((IEV3SensorPorperty)getProperty()).getValue();
    }

    @Override
    public IConstraint copy() {
        return new LT((IEV3SensorPorperty) getProperty().copy());
    }
}
