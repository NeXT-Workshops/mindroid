package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by Torbe on 02.05.2017.
 */
public class RED extends SimpleEV3SensorProperty {

    /**
     *
     * @param port brickport of the RED-mode running sensor
     */
    public RED(EV3PortID port) {
        super(port);
    }

    @Override
    public IProperty copy() {
        return new RED(getSensorPort());
    }

    @Override
    public Sensormode getSensormode() {
        return Sensormode.RED;
    }
}
