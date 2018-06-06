package org.mindroid.api.sensor;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.sensor.EV3SensorEndpoint;


public interface IEV3SensorEvent  {
    EV3SensorEndpoint getSensor();

    float[] getSample();

    long getTimeStamp();
    
    Sensormode getSensorMode();


}
