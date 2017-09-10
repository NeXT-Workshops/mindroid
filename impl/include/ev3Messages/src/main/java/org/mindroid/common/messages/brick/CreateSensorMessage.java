package org.mindroid.common.messages.brick;

import org.mindroid.common.messages.hardware.Sensors;

public class CreateSensorMessage{
    String port;
    Sensors sensorType;
    int networkPort;


    public CreateSensorMessage(){

    }


    public String getPort() {
        return port;
    }


    public void setPort(String port) {
        this.port = port;
    }


    public Sensors getSensorType() {
        return sensorType;
    }


    public void setSensorType(Sensors sensorType) {
        this.sensorType = sensorType;
    }


    public int getNetworkPort() {
        return networkPort;
    }


    public void setNetworkPort(int networkPort) {
        this.networkPort = networkPort;
    }
}