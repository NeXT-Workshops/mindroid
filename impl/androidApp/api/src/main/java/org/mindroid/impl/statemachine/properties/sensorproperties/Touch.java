package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Property for EV3TouchSensor mode
 *
 * Created by Torbe on 02.05.2017.
 */
public class Touch extends SimpleEV3SensorProperty {

    public static final int NO_HIT = 0;
    public static final int HIT = 1;

    /**
     * @param port brickport of the touch-mode running sensor
     */
    public Touch(EV3PortID port) {
        super(port);
    }

    @Override
    public IProperty copy() {
        return new Touch(getSensorPort());
    }

    @Override
    public Sensormode getSensormode() {
        return Sensormode.TOUCH;
    }
}
