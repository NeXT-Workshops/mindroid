package org.mindroid.common.messages;

/**
 * All Messages sent by or to the Brick
 * 
 * @author Torben
 *
 */
public class BrickMessages {

	@Deprecated
	public static HelloMessage newHelloThereMessage(String msg){
		return new HelloMessage(msg);
	}

	@Deprecated
	public static CreateDisplayMessage createDisplay(){
		return new CreateDisplayMessage();
	}

	@Deprecated
	public static class CreateDisplayMessage{
		public CreateDisplayMessage(){}
	}

	@Deprecated
	public static EndpointCreatedMessage createEndpointCreatedMessage(boolean success,String port, String msg,boolean isSensor,boolean isMotor){
		return new EndpointCreatedMessage(success,port,msg,isSensor,isMotor);
	}
	
	
	/**
	 * Message send to brick to create a SensorEndpoint at the brick.
	 * @param port the ID of the port on the EV3 brick
	 * @param sensorType the type of sensor to assume at the given port
	 * @param networkPort the TCP port to use
	 * @return the prepared message
	 */
	public static CreateSensorMessage createSensor(String port, Sensors sensorType, int networkPort){
		CreateSensorMessage csm = new CreateSensorMessage();
		csm.setPort(port);
		csm.setSensorType(sensorType);
		csm.setNetworkPort(networkPort);
		return csm;
	}
	
	/**
	 * Message send to the brick to create a MotorEndpoint at the brick.
	 * @param port the ID of the port on the EV3 brick
	 * @param motorType the type of motor so assume at the given port
	 * @param networkPort the TCP port to use
	 * @return the prepared message
	 */
	public static CreateMotorMessage createMotor(String port, Motors motorType, int networkPort){
		CreateMotorMessage cmm = new CreateMotorMessage();
		cmm.setPort(port);
		cmm.setMotorType(motorType);
		cmm.setNetworkPort(networkPort);
		return cmm;
	}
	

	/**
	 * Hello message gets send by the Brick when connection is established 
	 * and the Brick is ready to receive commands.
	 * 
	 * @author Torben Unzicker
	 */
	public static class HelloMessage{
		final String msg;
		
		public HelloMessage(){
			this("");
		}
		
		HelloMessage(String msg){
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}

		@Override
		public String toString() {
			return "HelloMessage [msg=" + msg + "]";
		}
		
		
	}
	
	public static class EndpointCreatedMessage{
		String msg;
		boolean success = false;
		String port;
		boolean sensor;
		boolean motor;
		
		public EndpointCreatedMessage(){
			
		}
		
		public EndpointCreatedMessage(boolean success, String port, String msg,boolean isSensor,boolean isMotor ){
			this.msg = msg;
			this.success = success;
			this.port = port;
			this.sensor = isSensor;
			this.motor = isMotor;
		}
		public String getMsg() {
			return msg;
		}
		
		public boolean isSuccess(){
			return success;
		}
		
		
		public String getPort() {
			return port;
		}

		
		
		public boolean isSensor() {
			return sensor;
		}

		public boolean isMotor() {
			return motor;
		}

		@Override
		public String toString() {
			return "EndpointCreatedMessage [msg=" + msg + ", success=" + success + ", port=" + port + ", sensor="
					+ sensor + ", motor=" + motor + "]";
		}

		
		
		
	}
	
	public static class CreateSensorMessage{
		String port;
		Sensors sensorType;
		int networkPort;
	
			
		public CreateSensorMessage(){
			
		}


		public String getPort() {
			return port;
		}


		public void setPort(String port) {
			this.port = port;
		}


		public Sensors getSensorType() {
			return sensorType;
		}


		public void setSensorType(Sensors sensorType) {
			this.sensorType = sensorType;
		}


		public int getNetworkPort() {
			return networkPort;
		}


		public void setNetworkPort(int networkPort) {
			this.networkPort = networkPort;
		}		
	}
	
	public static class CreateMotorMessage{
		String port;
		Motors motorType;
		int networkPort;
		
			
		public CreateMotorMessage(){
			
		}


		public String getPort() {
			return port;
		}


		public void setPort(String port) {
			this.port = port;
		}


		public Motors getMotorType() {
			return motorType;
		}


		public void setMotorType(Motors motorType) {
			this.motorType = motorType;
		}


		public int getNetworkPort() {
			return networkPort;
		}


		public void setNetworkPort(int networkPort) {
			this.networkPort = networkPort;
		}
		
	}
	

}
