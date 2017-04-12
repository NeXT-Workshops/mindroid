package mindroid.common.ev3.endpoints.motors.ev3;

import java.io.IOException;

import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.UnregulatedMotorMessages;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.BaseMotor;


public class EV3UnregulatedMotor extends AbstractMotor implements MotorMessageListener  {

	protected UnregulatedMotorMessages.MotorState motorState;
	
	public EV3UnregulatedMotor(Port motorPort) throws IOException {
		super(motorPort);
		this.motor = createMotor(motorPort);
		this.motortype = Motors.UnregulatedMotor;
		this.motorState = new UnregulatedMotorMessages.MotorState();
	}

	public void handleMotorMessage(Object msg) {

		if (msg instanceof UnregulatedMotorMessages.ForwardMsg) {
			this.motor.forward();
			return;
		}
		if (msg instanceof UnregulatedMotorMessages.BackwardMsg) {
			this.motor.backward();
			return;
		}
		if (msg instanceof UnregulatedMotorMessages.StopMsg) {
			this.motor.stop();
			return;
		}
		if (msg instanceof UnregulatedMotorMessages.SetPowerMsg) {
			((UnregulatedMotor)motor).setPower(((UnregulatedMotorMessages.SetPowerMsg) msg).getPower());
			return;
		}
		
		// Drop packet otherwise
	}

	public UnregulatedMotorMessages.MotorState getMotorState() {
		if(motorState != null) {
			motorState.setMoving(motor.isMoving());
			motorState.setPower(((UnregulatedMotor) motor).getPower());
			motorState.setTachoCount(((UnregulatedMotor) motor).getTachoCount());
		}else{
			motorState = new UnregulatedMotorMessages.MotorState();
			return getMotorState();
		}
		return motorState;
	}

	@Override
	protected BaseMotor createMotor(Port motorPort) {
		return new UnregulatedMotor(motorPort);
	}


	@Override
	public String toString() {
		return "EV3UnregulatedMotor [motorPort=" + motorPort + ", motor=" + motor + ", motorState="
				+ motorState + "]";
	}

	@Override
	public void close() {
		((UnregulatedMotor)motor).close();		
	}

	

}
