package org.mindroid.impl.sensor;

import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;

public interface IEV3SensorEndpoint {

    void registerListener(IEV3SensorEventListener listener);

    void unregisterListener(IEV3SensorEventListener listener);

    void changeSensorToMode(Sensormode newMode);

    void handleSensorEvent(EV3SensorEvent sensorevent);

    IEV3SensorEvent getLastRcvdSensorEvt();

    Sensors getSensorType();
}
