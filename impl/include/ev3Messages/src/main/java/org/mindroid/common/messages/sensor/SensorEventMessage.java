package org.mindroid.common.messages.sensor;

public class SensorEventMessage {
    private float[] sample;
    private long timestamp;

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
}