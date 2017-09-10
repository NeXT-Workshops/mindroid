package mindroid.common.ev3.endpoints.motors.ev3;

import java.io.IOException;

import lejos.hardware.motor.BaseRegulatedMotor;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.motor.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.BaseMotor;

/**
 * Created by torben on 30.01.2017.
 */

public class LargeRegulatedMotor extends AbstractMotor implements MotorMessageListener {

    public MotorState motorState;

    public LargeRegulatedMotor(Port motorPort) throws IOException {
        super(motorPort);
        this.motor = createMotor(motorPort);
        this.motortype = Motors.LargeRegulatedMotor;
        this.motorState = new MotorState();
    }

    @Override
    protected BaseMotor createMotor(Port motorPort) {
        return new EV3LargeRegulatedMotor(motorPort);
    }

    @Override
    public void close() {
        ((EV3LargeRegulatedMotor)motor).close();;
    }

    public MotorState getMotorState() {
        if(this.motorState != null){
            this.motorState.setAcceleration(((BaseRegulatedMotor) motor).getAcceleration());
            this.motorState.setLimitAngle(((BaseRegulatedMotor) motor).getLimitAngle());
            this.motorState.setMaxSpeed(((BaseRegulatedMotor) motor).getMaxSpeed());
            this.motorState.setPosition(((BaseRegulatedMotor) motor).getMaxSpeed());
            this.motorState.setRotationSpeed(((BaseRegulatedMotor) motor).getRotationSpeed());
            this.motorState.setTachoCount(((BaseRegulatedMotor) motor).getTachoCount());
        }else{
            this.motorState = new MotorState();
            return getMotorState();
        }
        return this.motorState;
    }

    @Override
    public void handleMotorMessage(Object msg) {
        if(msg instanceof RotateMessage){
            ((EV3LargeRegulatedMotor) motor).rotate(((RotateMessage)msg).getAngle());
            return;
        }

        if(msg instanceof RotateToMessage){
            ((EV3LargeRegulatedMotor) motor).rotateTo(((RotateToMessage)msg).getAngle());
            return;
        }

        if (msg instanceof ForwardMessage) {
            motor.forward();
            return;
        }
        if (msg instanceof BackwardMessage) {
            motor.backward();
            return;
        }
        if (msg instanceof StopMessage) {
            motor.stop();
            return;
        }
        if (msg instanceof SetMotorSpeedMessage) {
            ((EV3LargeRegulatedMotor) motor).setSpeed(((SetMotorSpeedMessage) msg).getSpeed());
            return;
        }
    }
}
