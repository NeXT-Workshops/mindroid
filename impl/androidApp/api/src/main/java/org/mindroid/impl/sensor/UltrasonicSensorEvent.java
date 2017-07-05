package org.mindroid.impl.sensor;

import org.mindroid.api.sensor.IUltrasonicSensorEvent;

public class UltrasonicSensorEvent implements IUltrasonicSensorEvent {

	/** Event type **/
	public UltrasonicEventType eventType = null;
	
	/** event origin **/
	public EV3Sensor origin = null;
	
	public long timestamp = -1;
	
	public UltrasonicSensorEvent(UltrasonicEventType eventType, EV3Sensor eventSource, long timestamp){
		this.eventType = eventType;
		this.origin = eventSource;
		this.timestamp = timestamp;
	}
	
	public UltrasonicSensorEvent(UltrasonicEventType eventType, EV3Sensor eventSource){
		this.eventType = eventType;
		this.origin = eventSource;		
	}
	
	@Override
	public long getTimeStamp() {
		return timestamp;
	}

	@Override
	public UltrasonicEventType getEvent() {
		return eventType;
	}

	@Override
	public EV3Sensor getEventSource() {
		return origin;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UltrasonicSensorEvent other = (UltrasonicSensorEvent) obj;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (eventType != other.eventType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UltrasonicSensorEvent{" +
				"eventType=" + eventType +
				", origin=" + origin +
				", timestamp=" + timestamp +
				'}';
	}
}
