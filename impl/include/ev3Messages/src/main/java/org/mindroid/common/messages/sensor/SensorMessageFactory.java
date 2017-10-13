package org.mindroid.common.messages.sensor;

import org.mindroid.common.messages.hardware.Sensormode;

public class SensorMessageFactory {

	public static HelloSensorMessage HelloSensorMessage(String msg){
		return new HelloSensorMessage(msg);
	}


	/**
	 *
	 * Creates a Status message.
	 * @param msg - Your message as a String
	 * @return SensorStatusMessage
	 */
	public static SensorStatusMessage createStatusMessage(String msg){
		SensorStatusMessage statusMessage = new SensorStatusMessage();
		statusMessage.setMsg(msg);
		return statusMessage;
	}


	/**
	 * Creates a SensorErrorMessage with the given parameters
	 * @param port - port of the sensor the error comes from
	 * @param errorMsg - the message
	 * @return SensorErrorMessage object with given paramters
	 */
	public static SensorErrorMessage createSensorErrorMessage(String port, String errorMsg){
		SensorErrorMessage sem = new SensorErrorMessage();
		sem.setPort(port);
		sem.setErrorMsg(errorMsg);
		
		return sem;
	}
	

	
	
	/**
	 * Creates a {@link SensorEventMessage} to transmit from EV3 to the smartphone
	 * 
	 * @param sample the raw sensor data
	 * @param timestamp the time stamp of the measurement
	 * @return the prepared message
	 */
	public static SensorEventMessage sensorEvent(float[] sample, Sensormode mode, long timestamp){
		SensorEventMessage msg = new SensorEventMessage();
		msg.setSample(sample);
		msg.setSensormode(mode);
		msg.setTimestamp(timestamp);
		return msg;
	}
	

	/**
	 * Send this object from Smartphone to change the sensor mode on the EV3
	 * 
	 * @param mode the sensor mode
	 * @return the prepared message
	 */
	public static ChangeSensorModeMessage changeModeTo (Sensormode mode){
		ChangeSensorModeMessage msg = new ChangeSensorModeMessage();
		msg.setNewMode(mode);
		return msg;
	}

	
	/**
	 * Send this object containing the new sensormode to tell the smartphone
	 *
	 * @param m sensor mode
	 * @return a new object of the corresponding class: ChangeSensorModeResponseMsg
	 */
	public static ModeChangedSuccessfullyMessage sensorModeChangedTo (Sensormode m){
		ModeChangedSuccessfullyMessage msg = new ModeChangedSuccessfullyMessage();
		msg.setNewMode(m);
		return msg;
	}



}
