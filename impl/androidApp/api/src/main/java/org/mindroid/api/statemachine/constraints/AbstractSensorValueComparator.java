package org.mindroid.api.statemachine.constraints;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.statemachine.properties.ComplexEV3SensorProperty;
import org.mindroid.api.statemachine.properties.IEV3SensorPorperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;

/**
 * Created by torben on 16.03.2017.
 */
public abstract class AbstractSensorValueComparator extends AbstractComparator{


    public AbstractSensorValueComparator(float value,SimpleEV3SensorProperty property) {
        super(property);
        super.setValue(value);
        super.evaluationMode = SIMPLE_VALUE_EVALUATION;
    }

    public AbstractSensorValueComparator(float[] values,ComplexEV3SensorProperty property) {
        super(property);
        super.setValues(values);
        super.evaluationMode = COMPLEX_VALUE_EVALUATION;
    }


    protected abstract boolean evaluate(float sample);

    protected abstract boolean evaluate(float[] sample);

    @Override
    public boolean evaluate(IRobotContextState context) {
        if(getProperty() instanceof IEV3SensorPorperty){
            IEV3SensorEvent event = context.getSensorEvent(((IEV3SensorPorperty) getProperty()).getSensorPort());
            if(((IEV3SensorPorperty) getProperty()).getSensormode() == event.getSensorMode()){
                    switch(evaluationMode){
                        case SIMPLE_VALUE_EVALUATION: return evaluate(event.getSample()[0]);
                        case COMPLEX_VALUE_EVALUATION: return evaluate(event.getSample());
                        default: return false;
                    }
            }
        }
        return false;
    }
}
