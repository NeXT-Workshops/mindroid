package org.mindroid.impl.sensor.mock;

import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.sensor.EV3SensorEndpoint;
import org.mindroid.impl.sensor.EV3SensorEvent;
import org.mindroid.impl.sensor.IEV3SensorEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * MockSensorEndpoint Class, has no real connection!
 */
public class MockSensorEndpoint implements IEV3SensorEndpoint {

    List<IEV3SensorEventListener> listeners = new ArrayList<>(2);

    private Sensors sensorType;
    private Sensormode sensorMode;

    private EV3PortID brick_port;

    // Gets set (true) when the creation on Brick site failed.
    private boolean hasCreationFailed = false;

    public MockSensorEndpoint(EV3PortID brickPort, Sensors sensorType, Sensormode mode) {
        this.sensorType = sensorType;
        this.brick_port = brickPort;
        this.sensorMode = mode;
    }


    @Override
    public void registerListener(IEV3SensorEventListener listener) {
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override
    public void unregisterListener(IEV3SensorEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void changeSensorToMode(Sensormode newMode) {
        sensorMode = newMode;
    }

    @Override
    public void handleSensorEvent(EV3SensorEvent sensorevent) {
        for (IEV3SensorEventListener listener : listeners) {
            listener.handleSensorEvent(brick_port,sensorevent);
        }
    }

    @Override
    public Sensors getSensorType(){
        return sensorType;
    }
}
