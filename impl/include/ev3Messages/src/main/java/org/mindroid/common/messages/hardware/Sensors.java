package org.mindroid.common.messages.hardware;

import static org.mindroid.common.messages.hardware.Sensormode.AMBIENT;
import static org.mindroid.common.messages.hardware.Sensormode.ANGLE;
import static org.mindroid.common.messages.hardware.Sensormode.COLOR_ID;
import static org.mindroid.common.messages.hardware.Sensormode.DISTANCE;
import static org.mindroid.common.messages.hardware.Sensormode.LISTEN;
import static org.mindroid.common.messages.hardware.Sensormode.RATE;
import static org.mindroid.common.messages.hardware.Sensormode.RATEANDANGLE;
import static org.mindroid.common.messages.hardware.Sensormode.RED;
import static org.mindroid.common.messages.hardware.Sensormode.RGB;
import static org.mindroid.common.messages.hardware.Sensormode.SEEK;
import static org.mindroid.common.messages.hardware.Sensormode.TOUCH;

/**
 * All available Sensors.
 * 
 * @author Torben
 *
 */ //TODO Rename to SensorType
public enum Sensors {
	EV3ColorSensor(new Sensormode[]{RED, COLOR_ID, AMBIENT, RGB},"Colorsensor"),
	EV3GyroSensor(new Sensormode[]{ANGLE, RATE, RATEANDANGLE},"Gyrosensor"),
	EV3IRSensor(new Sensormode[]{DISTANCE, SEEK},"Infraredsensor"),
	EV3TouchSensor(new Sensormode[]{TOUCH},"Touchsensor"),
	EV3UltrasonicSensor(new Sensormode[]{DISTANCE, LISTEN},"Ultrasonicsensor");

	Sensormode[] modes = null;
	String name = "";
	
	Sensors(Sensormode[] modes, String name){
		this.modes = modes;
		this.name = name;
	}
	
	public Sensormode[] getModes(){
		return modes;
	}
	
	public boolean isValidMode(Sensormode mode){
		for(Sensormode valMode: modes){
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
