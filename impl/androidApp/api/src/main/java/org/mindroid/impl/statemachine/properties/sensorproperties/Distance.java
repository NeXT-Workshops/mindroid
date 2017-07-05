package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.EV3SensorPorperty;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 10.03.2017.
 */
public class Distance extends EV3SensorPorperty {

    /**
     *
     * @param value - in meter
     * @param port
     */
    public Distance(float value, EV3PortID port) {
        super(value, port);
    }

    @Override
    public SensorMessages.SensorMode_ getSensormode() {
        return SensorMessages.SensorMode_.DISTANCE;
    }
}
