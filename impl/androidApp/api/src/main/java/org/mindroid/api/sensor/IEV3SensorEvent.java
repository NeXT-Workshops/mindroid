package org.mindroid.api.sensor;

import org.mindroid.impl.sensor.EV3Sensor;

import org.mindroid.common.messages.SensorMessages.SensorMode_;

public interface IEV3SensorEvent  {
    EV3Sensor getSensor();

    float getSample();

    long getTimeStamp();
    
    SensorMode_ getSensorMode();


}
