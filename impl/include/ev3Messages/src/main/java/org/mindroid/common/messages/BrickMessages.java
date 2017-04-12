package org.mindroid.common.messages;

/**
 * All Messages sent by or to the Brick
 * 
 * @author Torben
 *
 */
public class BrickMessages {
		
	public static HelloMessage newHelloThereMessage(String msg){
		return new HelloMessage(msg);
	}
	
	@Deprecated
	public static CreateDisplayMessage createDisplay(){
		return new CreateDisplayMessage();
	}
	
	public static class CreateDisplayMessage{
		public CreateDisplayMessage(){};
	}
	
	/**
	 * 
	 * @param success
	 * @param port
	 * @param msg
	 * @param isSensor
	 * @param isMotor
	 * @return
	 */
	public static EndpointCreatedMessage  createEndpointCreatedMessage(boolean success,String port, String msg,boolean isSensor,boolean isMotor){
		return new EndpointCreatedMessage(success,port,msg,isSensor,isMotor);
	}
	
	
	/**
	 * Message send to brick to create a SensorEndpoint at the brick.
	 * @param port
	 * @param sensorType
	 * @param networkPort
	 * @return
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
	 * @param port
	 * @param motorType
	 * @param networkPort
	 * @return
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
	 * @author mindroid
	 *
	 */
	public static class HelloMessage{
		String msg ="";
		
		public HelloMessage(){
			
		}
		
		HelloMessage(String msg){
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}

		@Override
		public String toString() {
			return "HelloThereMessage [msg=" + msg + "]";
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
