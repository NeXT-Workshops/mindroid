package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.ComplexEV3SensorProperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.common.messages.hardware.Sensormode;
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
    public Sensormode getSensormode() {
        return Sensormode.RATEANDANGLE;
    }
}
