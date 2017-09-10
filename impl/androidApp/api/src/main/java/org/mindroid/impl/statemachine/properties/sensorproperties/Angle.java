package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.statemachine.constraints.Rotation;

/**
 * Created by Torbe on 08.05.2017.
 *
 * Works only with ANGLE mode
 */
public class Angle extends SimpleEV3SensorProperty {
    public Angle(EV3PortID port) {
        super(port);
    }

    @Override
    public IProperty copy() {
        return new Angle(getSensorPort());
    }

    @Override
    public Sensormode getSensormode() {
        return Sensormode.ANGLE;
    }
}
