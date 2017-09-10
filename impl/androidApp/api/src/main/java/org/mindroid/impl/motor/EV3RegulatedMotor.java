package org.mindroid.impl.motor;



import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.common.messages.motor.MotorState;
import org.mindroid.common.messages.motor.MotorStateMessage;
import org.mindroid.common.messages.motor.RegulatedMotorMessagesFactory;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;



/**
 * Endpoint to control a RegulatedMotor (Large and Medium)
 */
public class EV3RegulatedMotor extends ClientEndpointImpl implements RegulatedMotor{

	private int rotationSpeed = -1;
	private int limitAngle = -1;
	private int acceleration = -1;
	private int tachoCount = -1;
	private float position = -1;
	private float maxSpeed = -1;
	
	public EV3RegulatedMotor(String ip,int tcpPort, int brickTimeout) {
		super(ip,tcpPort,brickTimeout); 
	}

	@Override
	public void forward() {
		if(client.isConnected()){
			client.sendTCP(RegulatedMotorMessagesFactory.createForwardMessage());
		}
	}

	@Override
	public void backward() {
		if(client.isConnected()){
			client.sendTCP(RegulatedMotorMessagesFactory.createBackwardMessage());
		}
	}

	@Override
	public void stop() {
		if(client.isConnected()){
			client.sendTCP(RegulatedMotorMessagesFactory.createStopMessage());
		}		
	}

	@Override
	public void setSpeed(int speed) {
		if(speed < MIN_SPEED){
			speed = MIN_SPEED;
		}else if(speed > MAX_SPEED){
			speed = MAX_SPEED;
		}
		if(client.isConnected()){
			client.sendTCP(RegulatedMotorMessagesFactory.createSetSpeedMessage(speed));
		}
	}
	
	@Override
	public void rotate(int angle) {
		if(client.isConnected()){
			client.sendTCP(RegulatedMotorMessagesFactory.createRotateMessage(angle));
		}
	}

	@Override
	public void rotateTo(int angle) {
		if(client.isConnected()){
			client.sendTCP(RegulatedMotorMessagesFactory.createRotateToMessage(angle));
		}
	}

	@Override
	public void received(Connection connection, Object object) {
		
		if(object.getClass() == MotorStateMessage.class){
			MotorState ms = ((MotorStateMessage) object).getMotorState();
			acceleration = ms.getAcceleration();
			limitAngle = ms.getLimitAngle();
			maxSpeed = ms.getMaxSpeed();
			position = ms.getPosition();
			rotationSpeed  = ms.getRotationSpeed();
			tachoCount = ms.getTachoCount();
		}
		
	}
	@Override
	public int getRotationSpeed() {
		return rotationSpeed;
	}
	@Override
	public int getLimitAngle() {
		return limitAngle;
	}
	@Override
	public int getAcceleration() {
		return acceleration;
	}
	@Override
	public int getTachoCount() {
		return tachoCount;
	}
	@Override
	public float getPosition() {
		return position;
	}
	@Override
	public float getMaxSpeed() {
		return maxSpeed;
	}

	@Override
	public String toString() {
		return "EV3RegulatedMotor [rotationSpeed=" + rotationSpeed + ", limitAngle=" + limitAngle + ", acceleration="
				+ acceleration + ", tachoCount=" + tachoCount + ", position=" + position + ", maxSpeed=" + maxSpeed
				+ "]";
	}
	
	

}
