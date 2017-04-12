package mindroid.common.ev3.endpoints.motors.ev3;

import java.io.IOException;

import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.RegulatedMotorMessages;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.BaseMotor;

/**
 * Represents a EV3MediumRegulatedMotor
 */
public class MediumRegulatedMotor extends AbstractMotor implements MotorMessageListener {

	public RegulatedMotorMessages.MotorState motorState;
	
	public MediumRegulatedMotor(Port motorPort) throws IOException {
		super(motorPort);
		this.motor = createMotor(motorPort);
		this.motortype = Motors.MediumRegulatedMotor;
		this.motorState = new RegulatedMotorMessages.MotorState();

	}

	public RegulatedMotorMessages.MotorState getMotorState() {
		if(this.motorState != null){
			this.motorState.setAcceleration(((BaseRegulatedMotor) motor).getAcceleration());
			this.motorState.setLimitAngle(((BaseRegulatedMotor) motor).getLimitAngle());
			this.motorState.setMaxSpeed(((BaseRegulatedMotor) motor).getMaxSpeed());
			this.motorState.setPosition(((BaseRegulatedMotor) motor).getMaxSpeed());
			this.motorState.setRotationSpeed(((BaseRegulatedMotor) motor).getRotationSpeed());
			this.motorState.setTachoCount(((BaseRegulatedMotor) motor).getTachoCount());
		}else{
			this.motorState = new RegulatedMotorMessages.MotorState();
			return getMotorState();
		}
		return this.motorState;
	}

	@Override
	protected BaseMotor createMotor(Port motorPort){
		return new EV3MediumRegulatedMotor(motorPort);
	}

	public void handleMotorMessage(Object msg) {
		if(msg instanceof RegulatedMotorMessages.RotateMessage){
			((EV3MediumRegulatedMotor) motor).rotate(((RegulatedMotorMessages.RotateMessage)msg).getAngle());
			return;
		}
		
		if(msg instanceof RegulatedMotorMessages.RotateToMessage){
			((EV3MediumRegulatedMotor) motor).rotateTo(((RegulatedMotorMessages.RotateToMessage)msg).getAngle());
			return;			
		}
		
		if (msg instanceof RegulatedMotorMessages.ForwardMsg) {
			
			motor.forward();
			return;
		}
		if (msg instanceof RegulatedMotorMessages.BackwardMsg) {
			motor.backward();
			return;
		}
		if (msg instanceof RegulatedMotorMessages.StopMsg) {
			motor.stop();
			return;
		}
		if (msg instanceof RegulatedMotorMessages.SetSpeedMsg) {
			((EV3MediumRegulatedMotor) motor).setSpeed(((RegulatedMotorMessages.SetSpeedMsg) msg).getSpeed());
			return;
		}
		// Drop packet otherwise
	}

	@Override
	public void close() {
		((EV3MediumRegulatedMotor)motor).close();;
	}





}
