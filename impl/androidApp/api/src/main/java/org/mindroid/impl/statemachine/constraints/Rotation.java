package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.statemachine.constraints.AbstractComparator;
import org.mindroid.api.statemachine.constraints.AbstractSensorValueComparator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.ComplexEV3SensorProperty;
import org.mindroid.api.statemachine.properties.IEV3SensorPorperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.statemachine.properties.sensorproperties.Angle;
import org.mindroid.impl.statemachine.properties.sensorproperties.RateAndAngle;

/**
 * Created by Torbe on 08.05.2017.
 */
public class Rotation extends AbstractSensorValueComparator {
    public Rotation(float value,Angle property) {
        super(value,property);
    }

    public Rotation(float[] value,RateAndAngle property) {
        super(value,property);
    }

    @Override
    protected boolean evaluate(float sample) {
        //sample = (start_cond_posiiton - currentSensorValue) < or > than the wanted degree (getValue())
        //e.g. start_cond_angle = 50
        if(getValue() < 0){
            // e.g. 20 - 50 < -90 --> false ## -40 - 50 < -90 --> true
            return sample < getValue();
        }else{
            // e.g. 80 - 50 > 90 --> false ## 141 - 50 > 90 --> true
            return sample > getValue();
        }

    }

    @Override
    protected boolean evaluate(float[] sample) {
        //Never called!! - Therefore its empty
        return false;
    }

    @Override
    public IConstraint copy() {
        switch(evaluationMode){
            case SIMPLE_VALUE_EVALUATION: return new Rotation(getValue(), (Angle) getProperty());
            case COMPLEX_VALUE_EVALUATION: return new Rotation(getValues(),(RateAndAngle) getProperty());//TODO return new Rotation(getValues(), (RateAndAngle) getProperty());
            default: return null;
        }
    }

    @Override
    public boolean evaluate(IRobotContextState context) {
        EV3PortID sensorPort = ((IEV3SensorPorperty) getProperty()).getSensorPort();

        System.out.println("## Rotation.evaluate "+context.getStartCondition().getPosition(sensorPort));

        if(context.getStartCondition().getPosition(sensorPort) == null){
            return false;
        }

        if(getProperty() instanceof IEV3SensorPorperty){
            IEV3SensorEvent event = context.getSensorEvent(((IEV3SensorPorperty) getProperty()).getSensorPort());
            if(((IEV3SensorPorperty) getProperty()).getSensormode() == event.getSensorMode()){
                switch(evaluationMode){
                    case SIMPLE_VALUE_EVALUATION:
                        System.out.println("## startCondition "+context.getStartCondition().getPosition(sensorPort).getSample()[0]);
                        System.out.println("## eventSample "+event.getSample()[0]);
                        return evaluate(context.getStartCondition().getPosition(sensorPort).getSample()[0]-event.getSample()[0]);

                    case COMPLEX_VALUE_EVALUATION:
                        System.out.println("## startCondition "+context.getStartCondition().getPosition(sensorPort).getSample()[1]);
                        System.out.println("## eventSample "+event.getSample()[1]);
                        //Second element is Angle -- Sensormode should be RateAndAngle
                        return evaluate(context.getStartCondition().getPosition(sensorPort).getSample()[1]-event.getSample()[1]);

                    default: return false;
                }
            }
        }
        return false;
    }

}
