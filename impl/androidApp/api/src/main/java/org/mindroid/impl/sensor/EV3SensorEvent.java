package org.mindroid.impl.sensor;

import org.mindroid.api.sensor.IEV3SensorEvent;

import org.mindroid.common.messages.SensorMessages.SensorMode_;

public class EV3SensorEvent implements IEV3SensorEvent{
    private final float sample;
    private final long timeStamp;
    private final EV3Sensor sensor;
    private final SensorMode_ mode;

    public EV3SensorEvent(EV3Sensor sensor, float sample, long timeStamp, SensorMode_ mode){
        this.sample = sample;
        this.timeStamp = timeStamp;
        this.sensor = sensor;
        this.mode = mode;
    }

    @Override
    public EV3Sensor getSensor(){
        return sensor;
    }

    @Override
    public float getSample() {
        return this.sample;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

	@Override
	public SensorMode_ getSensorMode() {
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