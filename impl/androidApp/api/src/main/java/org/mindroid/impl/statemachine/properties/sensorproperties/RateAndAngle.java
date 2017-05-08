package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.ComplexEV3SensorProperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by Torbe on 08.05.2017.
 */
public class RateAndAngle extends ComplexEV3SensorProperty {
    public RateAndAngle(EV3PortID port) {
        super(port);
    }

    @Override
    public IProperty copy() {
        return new RateAndAngle(getSensorPort());
    }

    @Override
    public SensorMessages.SensorMode_ getSensormode() {
        return SensorMessages.SensorMode_.RATEANDANGLE;
    }
}
