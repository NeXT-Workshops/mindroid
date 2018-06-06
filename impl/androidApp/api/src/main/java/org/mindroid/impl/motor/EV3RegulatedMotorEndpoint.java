package org.mindroid.impl.motor;



import org.mindroid.api.motor.IRegulatedMotor;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.motor.MotorState;
import org.mindroid.common.messages.motor.MotorStateMessage;
import org.mindroid.common.messages.motor.RegulatedMotorMessagesFactory;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.logging.EV3MsgLogger;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class represents a single Endpoint of the connection at the API-side connected to the motor-object at the EV3-side
 * It is listening on the connection handeling the Motorstate message.
 * It is sending messages to control the motor of the EV3 using the open Connection.
 */
public class EV3RegulatedMotorEndpoint extends ClientEndpointImpl implements IRegulatedMotor {

	private int rotationSpeed = -1;
	private int limitAngle = -1;
	private int acceleration = -1;
	private int tachoCount = -1;
	private float position = -1;
	private int speed = -1;
	private float maxSpeed = -1;
	private boolean isMoving = false;

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final EV3MsgLogger msgRcvdLogger;
	private final EV3MsgLogger msgSendLogger;

	public EV3RegulatedMotorEndpoint(String ip, int tcpPort, int brickTimeout) {
		super(ip,tcpPort,brickTimeout);

		//Init Loggers
		APILoggerManager.getInstance().registerLogger(LOGGER);
		msgRcvdLogger = new EV3MsgLogger(LOGGER,"Received ");
		msgSendLogger = new EV3MsgLogger(LOGGER,"Send ");
	}

	// Gets set (true) when the creation on Brick site failed.
	private boolean hasCreationFailed = false;

	//---------- Incomming message handling

	@Override
	public void received(Connection connection, Object object) {
		//Log msg
		if(object instanceof ILoggable){
			((ILoggable) object).accept(msgRcvdLogger);
		}

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
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createForwardMessage();
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void backward() {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createBackwardMessage();
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void stop() {
		if(client.isConnected()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createStopMessage();
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void flt() {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createFltMessage();
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void stop(boolean immediateReturn) {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createStopMessage(immediateReturn);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
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
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createSetSpeedMessage(speed);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void rotate(int angle) {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createRotateMessage(angle);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void rotateTo(int angle) {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createRotateToMessage(angle);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void flt(boolean immediateReturn) {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createFltMessage(immediateReturn);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void rotate(int angle, boolean immediateReturn) {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createRotateMessage(angle,immediateReturn);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void rotateTo(int limitAngle, boolean immediateReturn) {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createRotateToMessage(limitAngle,immediateReturn);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}

	@Override
	public void setAcceleration(int acceleration) {
		if(isClientReady()){
			//Log msg
			ILoggable msg = RegulatedMotorMessagesFactory.createSetAccelerationMessage(acceleration);
			msg.accept(msgSendLogger);
			client.sendTCP(msg);
		}
	}


	@Override
	public String toString() {
		return "EV3RegulatedMotorEndpoint [rotationSpeed=" + rotationSpeed + ", limitAngle=" + limitAngle + ", acceleration="
				+ acceleration + ", tachoCount=" + tachoCount + ", position=" + position + ", maxSpeed=" + maxSpeed
				+ "]";
	}

	public boolean hasCreationFailed() {
		return hasCreationFailed;
	}

	public void setHasCreationFailed(boolean hasCreationFailed) {
		this.hasCreationFailed = hasCreationFailed;
	}
}
