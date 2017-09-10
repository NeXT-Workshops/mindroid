package org.mindroid.api.sensor;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.sensor.EV3Sensor;


public interface IEV3SensorEvent  {
    EV3Sensor getSensor();

    float[] getSample();

    long getTimeStamp();
    
    Sensormode getSensorMode();


}
