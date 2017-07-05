package org.mindroid.impl.sensor;

import org.mindroid.api.sensor.IColorSensorEvent;

/**
 * Created by torben on 21.01.2017.
 */

public class ColorSensorEvent implements IColorSensorEvent {

    /** Event type **/
    public ColorsensorEventType eventType = null;

    /** event origin **/
    public EV3Sensor origin = null;

    public long timestamp = -1;

    public ColorSensorEvent(ColorsensorEventType eventType, EV3Sensor eventSource, long timestamp){
        this.eventType = eventType;
        this.origin = eventSource;
        this.timestamp = timestamp;
    }

    public ColorSensorEvent(ColorsensorEventType eventType, EV3Sensor eventSource){
        this.eventType = eventType;
        this.origin = eventSource;
    }

    @Override
    public ColorsensorEventType getEvent() {
        return eventType;
    }

    @Override
    public EV3Sensor getEventSource() {
        return origin;
    }

    @Override
    public long getTimeStamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColorSensorEvent that = (ColorSensorEvent) o;

        if (eventType != that.eventType) return false;
        return origin != null ? origin.equals(that.origin) : that.origin == null;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((origin == null) ? 0 : origin.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ColorSensorEvent{" +
                "eventType=" + eventType +
                ", origin=" + origin +
                ", timestamp=" + timestamp +
                '}';
    }
}
