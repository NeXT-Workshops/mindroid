package org.mindroid.api.sensor;

import org.mindroid.impl.ev3.EV3PortID;

public interface IEV3SensorEventListener {


    public void handleSensorEvent(EV3PortID port, IEV3SensorEvent event);
}