package org.mindroid.impl.sensor;

import org.mindroid.api.robot.control.ISensorControl;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.impl.ev3.EV3PortID;

public class Sensor implements ISensorControl {

    private EV3SensorEndpoint sEndpoint;
    private EV3PortID port;

    public Sensor(EV3SensorEndpoint sEndpoint, EV3PortID port){
        this.sEndpoint = sEndpoint;
        this.port = port;
    }

    @Override
    public void changeSensorMode(Sensormode mode) {
        sEndpoint.changeSensorToMode(mode);
    }

    @Override
    public float[] getValue() {
        return sEndpoint.getSensorValue();
    }

    @Override
    public Sensormode getSensormode() {
        return sEndpoint.getCurrentMode();
    }

    @Override
    public EV3PortID getPort() {
        return port;
    }

    @Override
    public Sensors getSensorType() {
        return sEndpoint.getSensorType();
    }
}
