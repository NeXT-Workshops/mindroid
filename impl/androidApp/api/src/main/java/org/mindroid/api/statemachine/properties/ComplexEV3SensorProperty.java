package org.mindroid.api.statemachine.properties;

import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by Torbe on 02.05.2017.
 */
public abstract class ComplexEV3SensorProperty extends EV3SensorPorperty {

    public ComplexEV3SensorProperty(EV3PortID port) {
        super(port);
    }
}
