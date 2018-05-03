package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.EV3SensorPorperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by Torbe on 02.05.2017.
 */
public class Listen extends SimpleEV3SensorProperty {

    public static final int FOUND_ONE = 1;

    public static final int NO_ONE_FOUND = 0;

    /**
     *
     * @param port
     */
    public Listen(EV3PortID port) {
        super(port);
    }

    @Override
    public IProperty copy() {
        return new Listen(getSensorPort());
    }

    @Override
    public Sensormode getSensormode() {
        return Sensormode.LISTEN;
    }
}
