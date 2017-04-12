package org.mindroid.api.sensor;

import org.mindroid.impl.sensor.EV3Sensor;

public interface IUltrasonicSensorEvent {
	
	public enum UltrasonicEventType {
		/** Ultrasonic sensor detects collision **/
		COLLISION,
		
		/** Ultrasonic sensor detects no collision **/
		NO_COLLISION,

		COLLISION_WALL,
		NO_WALL;
	}
	
	UltrasonicEventType getEvent();
	
	EV3Sensor getEventSource();
	
	long getTimeStamp();

}
