package mindroid.common.ev3.endpoints.motors.ev3;

import java.io.IOException;

import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.motor.*;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.BaseMotor;

/**
 * Represents a EV3MediumRegulatedMotor
 */
public class MediumRegulatedIMotor extends AbstractRegulatedIMotor {

	public MotorState motorState;
	
	public MediumRegulatedIMotor(Port motorPort) throws IOException {
		super(motorPort);
		setMotor(createMotor(motorPort));
		setMotortype(Motors.MediumRegulatedMotor);
		setMotorState(new MotorState());
	}

	@Override
	protected BaseMotor createMotor(Port motorPort){
		return new EV3MediumRegulatedMotor(motorPort);
	}

}
