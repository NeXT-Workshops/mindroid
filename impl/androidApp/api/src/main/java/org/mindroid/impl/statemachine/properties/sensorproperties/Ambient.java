package org.mindroid.impl.statemachine.properties.sensorproperties;


import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by Torbe on 02.05.2017.
 */
public class Ambient extends SimpleEV3SensorProperty {

    /**
     *
     * @param port
     */
    public Ambient(EV3PortID port) {
        super(port);
    }

    @Override
    public IProperty copy() {
        return new Ambient(getSensorPort());
    }

    @Override
    public Sensormode getSensormode() {
        return Sensormode.AMBIENT;
    }
}
