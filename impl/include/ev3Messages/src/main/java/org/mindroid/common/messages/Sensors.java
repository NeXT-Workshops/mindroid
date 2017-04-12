package org.mindroid.common.messages;

import org.mindroid.common.messages.SensorMessages.SensorMode_;

/**
 * All available Sensors.
 * 
 * @author Torben
 *
 */ //TODO Rename to SensorType
public enum Sensors {
	EV3ColorSensor(new SensorMode_[]{SensorMode_.RED,SensorMode_.COLOR_ID,SensorMode_.AMBIENT,SensorMode_.RGB}),
	EV3GyroSensor(new SensorMode_[]{SensorMode_.ANGLE,SensorMode_.RATE,SensorMode_.RATEANDANGLE}),
	EV3IRSensor(new SensorMode_[]{SensorMode_.DISTANCE,SensorMode_.SEEK}),
	EV3TouchSensor(new SensorMode_[]{SensorMode_.TOUCH}),
	EV3UltrasonicSensor(new SensorMode_[]{SensorMode_.DISTANCE,SensorMode_.LISTEN});
	

	SensorMode_[] modes = null;
	
	Sensors(SensorMode_[] modes){ 
		this.modes = modes;			
	}
	
	public SensorMode_[] getModes(){
		return modes;
	}
	
	public boolean isValidMode(SensorMode_ mode){
		for(SensorMode_ valMode: modes){
			if(valMode == mode){
				return true;
			}
		}
		return false;
	}
	
} 
