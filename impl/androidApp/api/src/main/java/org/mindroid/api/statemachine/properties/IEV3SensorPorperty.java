package org.mindroid.api.statemachine.properties;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 10.03.2017.
 */
public interface IEV3SensorPorperty extends IProperty {

    Sensormode getSensormode();

    EV3PortID getSensorPort();
}
