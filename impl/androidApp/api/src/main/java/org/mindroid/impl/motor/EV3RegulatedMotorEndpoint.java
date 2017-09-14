package org.mindroid.impl.motor;



import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.common.messages.motor.MotorState;
import org.mindroid.common.messages.motor.MotorStateMessage;
import org.mindroid.common.messages.motor.RegulatedMotorMessagesFactory;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;


/**
 * This class represents a single Endpoint of the connection at the API-side connected to the motor-object at the EV3-side
 * It is listening on the connection handeling the Motorstate message.
 * It is sending messages to control the motor of the EV3 using the open Connection.
 */
public class EV3RegulatedMotorEndpoint extends ClientEndpointImpl implements RegulatedMotor{

	private int rotationSpeed = -1;
	private int limitAngle = -1;
	private int acceleration = -1;
	private int tachoCount = -1;
	private float position = -1;
	private int speed = -1;
	private float maxSpeed = -1;
	private boolean isMoving = false;

	
	public EV3RegulatedMotorEndpoint(String ip, int tcpPort, int brickTimeout) {
		super(ip,tcpPort,brickTimeout); 
	}


	//---------- Incomming message handling

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
			isMoving = ms.isMoving();
			speed = ms.getSpeed();
		}
	}

	//---------- Information about the state of the motor

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
	public int getSpeed() {
		return speed;
	}

	@Override
	public boolean isMoving() {
		return isMoving;
	}

	@Override
	public float getMaxSpeed() {
		return maxSpeed;
	}


	//---------- Operations the motor executes

	@Override
	public void forward() {
		if(client.isConnected()){
			System.out.println("[EV3RegulatedMotorEndpoint:forward()] forward got called!");
			client.sendTCP(RegulatedMotorMessagesFactory.createForwardMessage());
			System.out.println("[EV3RegulatedMotorEndpoint:forward()] forward-message sent!");
		}
	}

	@Override
	public void backward() {
		if(isClientReady()){
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
	public void flt() {
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createFltMessage());
		}
	}

	@Override
	public void stop(boolean immediateReturn) {
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createStopMessage(immediateReturn));
		}
	}

	@Override
	public void setSpeed(int speed) {
		if(speed < MIN_SPEED){
			speed = MIN_SPEED;
		}else if(speed > MAX_SPEED){
			speed = MAX_SPEED;
		}
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createSetSpeedMessage(speed));
		}
	}

	@Override
	public void rotate(int angle) {
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createRotateMessage(angle));
		}
	}

	@Override
	public void rotateTo(int angle) {
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createRotateToMessage(angle));
		}
	}

	@Override
	public void flt(boolean immediateReturn) {
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createFltMessage());
		}
	}

	@Override
	public void rotate(int angle, boolean immediateReturn) {
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createRotateMessage(angle,immediateReturn));
		}
	}

	@Override
	public void rotateTo(int limitAngle, boolean immediateReturn) {
		if(isClientReady()){
			client.sendTCP(RegulatedMotorMessagesFactory.createRotateToMessage(limitAngle,immediateReturn));
		}
	}


	@Override
	public String toString() {
		return "EV3RegulatedMotorEndpoint [rotationSpeed=" + rotationSpeed + ", limitAngle=" + limitAngle + ", acceleration="
				+ acceleration + ", tachoCount=" + tachoCount + ", position=" + position + ", maxSpeed=" + maxSpeed
				+ "]";
	}
	
	

}
