package org.mindroid.api.statemachine.constraints;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorPorperty;

/**
 * Created by torben on 16.03.2017.
 */
public abstract class AbstractSimpleSensorValueComparator extends AbstractComparator{


    public AbstractSimpleSensorValueComparator(SimpleEV3SensorPorperty property) {
        super(property);
    }

    protected abstract boolean evaluate(float value);

    @Override
    public boolean evaluate(IRobotContextState context) {
        if(getProperty() instanceof SimpleEV3SensorPorperty){
            IEV3SensorEvent event = context.getSensorEvent(((SimpleEV3SensorPorperty) getProperty()).getSensorPort());
            if(((SimpleEV3SensorPorperty) getProperty()).getSensormode() == event.getSensorMode()){
                return evaluate(event.getSample());
            }
        }
        return false;
    }
}
