package org.mindroid.common.messages;

public class SensorMessages {
	
	public enum SensorMode_ {
		//Modes of EV3ColorSensor 
		RED("Red"), 
		AMBIENT("Ambient"), 
		COLOR_ID("ColorID"), 
		RGB("RGB"), 
		
		//Modes of EV3UltraSonicSensor
		DISTANCE("Distance"), 
		LISTEN("Listen"),
		
		//Mode of EV3IRSensor, also DISTANCE 
		SEEK("Seek"),	//Untested Identifier(Stringvalue)
		
		//Modes of EV3GyroSensor
		ANGLE("Angle"),					//Untested Identifier(Stringvalue)
		RATE("Rate"),					//Untested Identifier 
		RATEANDANGLE("RateAndAngle"), 	//Untested Identifier 
		
		//Mode of EV3TouchSensor
		TOUCH("Touch");			//Untested Identifier(Stringvalue)
		
		String mode ="";
				
		SensorMode_(String mode){
			this.mode = mode;
		}
		
		public String getValue(){
			return mode;
		}
		
	}

	
	public static HelloSensorMessage HelloSensorMessage(String msg){
		return new HelloSensorMessage(msg);
	}
	
	/**
	 * Gets returned from the SensorEndpoint on Bricks-side, when the 
	 * Phone-side Sensor-Object connects the first time to the Endpoint.
	 * 
	 * Shows when the Sensor is ready to use.
	 * 
	 * @author Torben
	 *
	 */
	public static class HelloSensorMessage{
		String msg;
		
		public HelloSensorMessage(){
			
		}
		
		public HelloSensorMessage(String msg){
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}

		@Override
		public String toString() {
			return "HelloSensorMessage [msg=" + msg + "]";
		}
	
		
	}
	
	public static StatusMessage createStatusMessage(String msg){
		StatusMessage statusMessage = new StatusMessage();
		statusMessage.setMsg(msg);
		return statusMessage;
	}
	
	public static class StatusMessage{
		String msg;
		public StatusMessage(){
			
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
	}
	
	public static SensorErrorMessage createSensorErrorMessage(String port, String errorMsg){
		SensorErrorMessage sem = new SensorErrorMessage();
		sem.setPort(port);
		sem.setErrorMsg(errorMsg);
		
		return sem;
	}
	
	public static class SensorErrorMessage{
		String errorMsg;
		String port;
		
		public SensorErrorMessage(){
			
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		@Override
		public String toString() {
			return "SensorErrorMessage [errorMsg=" + errorMsg + ", port=" + port + "]";
		}
		
		
	}
	
	
	/**
	 * This object is used to send sensordata from EV3 to Smartphone
	 * 
	 * @param sample
	 * @param timestamp
	 * @return
	 */
	public static SensorEventMsg sensorEvent(float sample, long timestamp){
		SensorEventMsg msg = new SensorEventMsg();
		msg.setSample(sample);
		msg.setTimestamp(timestamp);
		return msg;
	}
	
	public static class SensorEventMsg {
		private float sample;
		private long timestamp;
		
		public SensorEventMsg(){}
		
		public float getSample() {
			return this.sample;
		}
		
		public void setSample(float sample) {
			this.sample = sample;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}
	
	
	/**
	 * Send this object from Smartphone to change the sensormode on the EV3
	 * 
	 * @param mode
	 * @return a new object of the corresponding class: ChangeSensorModeMsg
	 * @author Alex
	 */
	public static ChangeSensorModeMsg changeModeTo (SensorMode_ mode){
		ChangeSensorModeMsg msg = new ChangeSensorModeMsg();
		msg.setNewMode(mode);
		return msg;
	}
	public static class ChangeSensorModeMsg {
		private SensorMode_ newMode = null;
		
		public ChangeSensorModeMsg(){}
		
		public void setNewMode(SensorMode_ m){
			this.newMode = m;
		}
		
		public SensorMode_ getNewMode(){
			return this.newMode;
		}
	}
	
	
	/**
	 * Send this object containing the new sensormode to tell the smartphone
	 * 
	 * @return a new object of the corresponding class: ChangeSensorModeResponseMsg
	 * @author Alex
	 */
	public static modeChangedSuccessfullyMsg sensorModeChangedTo (SensorMode_ m){
		modeChangedSuccessfullyMsg msg = new modeChangedSuccessfullyMsg();
		msg.setNewMode(m);
		return msg;
	}
	public static class modeChangedSuccessfullyMsg {
		SensorMode_ newMode;
		
		public modeChangedSuccessfullyMsg(){}
		
		public void setNewMode(SensorMode_ m){
			this.newMode = m;
		}
		
		public SensorMode_ getMode(){
			return this.newMode;
		}
	}


}
