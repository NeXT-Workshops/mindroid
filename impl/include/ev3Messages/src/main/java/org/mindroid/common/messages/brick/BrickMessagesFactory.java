package org.mindroid.common.messages.brick;

import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;

/**
 * All Messages sent by or to the Brick
 * 
 * @author Torben
 *
 */
public class BrickMessagesFactory {


	private static final ResetBrickMessage resetBrickMessage = new ResetBrickMessage();

	@Deprecated
	public static HelloMessage newHelloThereMessage(String msg){
		return new HelloMessage(msg);
	}

	@Deprecated
	public static CreateDisplayMessage createDisplay(){
		return new CreateDisplayMessage();
	}

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
	
	public static ResetBrickMessage createResetBrickMsg(){
		return resetBrickMessage;
	}


	

	

}
