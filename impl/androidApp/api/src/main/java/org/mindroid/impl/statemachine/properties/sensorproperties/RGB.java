package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.ComplexEV3SensorProperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by Torbe on 02.05.2017.
 */
public class RGB extends ComplexEV3SensorProperty {

    /**
     *
     * @param port
     */
    public RGB(EV3PortID port) {
        super(port);
    }

    @Override
    public IProperty copy() {
        return new RGB(getSensorPort());
    }

    @Override
    public SensorMessages.SensorMode_ getSensormode() {
        return SensorMessages.SensorMode_.RGB;
    }
}
