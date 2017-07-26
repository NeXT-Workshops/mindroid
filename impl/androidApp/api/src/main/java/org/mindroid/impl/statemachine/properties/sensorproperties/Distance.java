package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.EV3SensorPorperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 10.03.2017.
 */
public class Distance extends SimpleEV3SensorProperty {

    /**
     *
     * @param port
     */
    public Distance(EV3PortID port) {
        super(port);
    }

    @Override
    public SensorMessages.SensorMode_ getSensormode() {
        return SensorMessages.SensorMode_.DISTANCE;
    }

    @Override
    public IProperty copy() {
        return new Distance(getSensorPort());
    }
}
