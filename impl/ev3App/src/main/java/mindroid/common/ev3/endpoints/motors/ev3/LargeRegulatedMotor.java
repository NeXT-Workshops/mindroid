package mindroid.common.ev3.endpoints.motors.ev3;

import java.io.IOException;

import org.mindroid.common.messages.RegulatedMotorMessages;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.BaseMotor;

/**
 * Created by torben on 30.01.2017.
 */

public class LargeRegulatedMotor extends AbstractMotor implements MotorMessageListener {
    public LargeRegulatedMotor(Port motorPort) throws IOException {
        super(motorPort);
        this.motor = createMotor(motorPort);
    }

    @Override
    protected BaseMotor createMotor(Port motorPort) {
        return new EV3LargeRegulatedMotor(motorPort);
    }

    @Override
    public void close() {
        ((EV3LargeRegulatedMotor)motor).close();;
    }

    @Override
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
    }
}
