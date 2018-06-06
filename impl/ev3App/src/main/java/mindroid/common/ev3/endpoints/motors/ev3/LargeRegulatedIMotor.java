package mindroid.common.ev3.endpoints.motors.ev3;

import java.io.IOException;

import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.motor.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.BaseMotor;

/**
 * Created by torben on 30.01.2017.
 */

public class LargeRegulatedIMotor extends AbstractRegulatedIMotor {

    public MotorState motorState;

    public LargeRegulatedIMotor(Port motorPort) throws IOException {
        super(motorPort);
        setMotor(createMotor(motorPort));
        setMotortype(Motors.LargeRegulatedMotor);
        setMotorState(new MotorState());
    }

    @Override
    protected BaseMotor createMotor(Port motorPort) {
        return new EV3LargeRegulatedMotor(motorPort);
    }


}
