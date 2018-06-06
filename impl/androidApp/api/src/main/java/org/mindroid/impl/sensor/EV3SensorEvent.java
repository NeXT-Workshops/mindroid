package org.mindroid.impl.sensor;

import org.mindroid.api.sensor.IEV3SensorEvent;

import org.mindroid.common.messages.hardware.Sensormode;

public class EV3SensorEvent implements IEV3SensorEvent{
    private final float[] sample;
    private final long timeStamp;
    private final EV3SensorEndpoint sensor;
    private final Sensormode mode;

    public EV3SensorEvent(EV3SensorEndpoint sensor, float[] sample, long timeStamp, Sensormode mode){
        this.sample = sample;
        this.timeStamp = timeStamp;
        this.sensor = sensor;
        this.mode = mode;
    }

    @Override
    public EV3SensorEndpoint getSensor(){
        return sensor;
    }

    @Override
    public float[] getSample() {
        return this.sample;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

	@Override
	public Sensormode getSensorMode() {
		return mode;
	}

    @Override
    public String toString() {
        return "EV3SensorEvent{" +
                "sample=" + sample +
                ", timeStamp=" + timeStamp +
                ", sensor=" + sensor +
                ", mode=" + mode +
                '}';
    }
}