package org.mindroid.common.messages.sensor;

import org.mindroid.common.messages.hardware.Sensormode;

public class SensorEventMessage {
    private float[] sample;
    private long timestamp;
    private Sensormode sensormode;

    public SensorEventMessage(){}

    public float[] getSample() {
        return this.sample;
    }

    public void setSample(float[] sample) {
        this.sample = sample;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Sensormode getSensormode() {
        return sensormode;
    }

    public void setSensormode(Sensormode sensormode) {
        this.sensormode = sensormode;
    }
}