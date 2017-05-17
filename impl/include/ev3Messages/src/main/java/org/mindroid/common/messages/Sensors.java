package org.mindroid.common.messages;

import org.mindroid.common.messages.SensorMessages.SensorMode_;

import static org.mindroid.common.messages.SensorMessages.SensorMode_.AMBIENT;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.ANGLE;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.COLOR_ID;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.DISTANCE;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.LISTEN;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.RATE;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.RATEANDANGLE;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.RED;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.RGB;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.SEEK;
import static org.mindroid.common.messages.SensorMessages.SensorMode_.TOUCH;

/**
 * All available Sensors.
 * 
 * @author Torben
 *
 */ //TODO Rename to SensorType
public enum Sensors {
	EV3ColorSensor(new SensorMode_[]{RED, COLOR_ID, AMBIENT, RGB},"Colorsensor"),
	EV3GyroSensor(new SensorMode_[]{ANGLE, RATE, RATEANDANGLE},"Gyrosensor"),
	EV3IRSensor(new SensorMode_[]{DISTANCE, SEEK},"Infraredsensor"),
	EV3TouchSensor(new SensorMode_[]{TOUCH},"Touchsensor"),
	EV3UltrasonicSensor(new SensorMode_[]{DISTANCE, LISTEN},"Ultrasonicsensor");

	SensorMode_[] modes = null;
	String name = "";
	
	Sensors(SensorMode_[] modes,String name){
		this.modes = modes;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public static String[] getAllModes(){
		String[] modes = {
				RED.getValue(),
				AMBIENT.getValue(),
				COLOR_ID.getValue(),
				RGB.getValue(),
				DISTANCE.getValue(),
				LISTEN.getValue(),
				SEEK.getValue(),
				ANGLE.getValue(),
				RATE.getValue(),
				RATEANDANGLE.getValue(),
				TOUCH.getValue()
		};
		return modes;
	}

	public static Sensors[] getAllSensorTypes(){
		return new Sensors[]{
				EV3ColorSensor,
				EV3GyroSensor,
				EV3IRSensor,
				EV3TouchSensor,
				EV3UltrasonicSensor
		};
	}
}
