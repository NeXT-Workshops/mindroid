package org.mindroid.impl.sensor;

import org.mindroid.api.robot.control.ISensorControl;
import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.impl.ev3.EV3PortID;

import java.util.Arrays;

public class Sensor implements ISensorControl,IEV3SensorEventListener {

    private IEV3SensorEndpoint sEndpoint;
    private EV3PortID port;

    private float[] value;
    private Sensormode mode;

    public Sensor(IEV3SensorEndpoint sEndpoint, EV3PortID port){
        this.sEndpoint = sEndpoint;
        this.port = port;
        this.sEndpoint.registerListener(this);

        //Retrieve the last event, so the initial values (sensor-value/mode) won't be null!
        handleSensorEvent(port,sEndpoint.getLastRcvdSensorEvt());
    }

    @Override
    public void changeSensorMode(Sensormode mode) {
        sEndpoint.changeSensorToMode(mode);
    }

    @Override
    public synchronized float[] getValue() {
        return value;
    }

    @Override
    public synchronized Sensormode getSensormode() {
        return mode;
    }

    @Override
    public EV3PortID getPort() {
        return port;
    }

    @Override
    public Sensors getSensorType() {
        return sEndpoint.getSensorType();
    }

    @Override
    public void handleSensorEvent(EV3PortID port, IEV3SensorEvent event) {
        if(this.port.equals(port)){
           setValue(event.getSample());
           setSensorMode(event.getSensorMode());
        }
    }

    private synchronized void setValue(float[] value){
        this.value = value;
    }

    private synchronized void setSensorMode(Sensormode mode){
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sEndpoint=" + sEndpoint +
                ", port=" + port +
                ", value=" + Arrays.toString(value) +
                ", mode=" + mode +
                ", endpointHashCode=" + sEndpoint.hashCode() +
                '}';
    }
}
