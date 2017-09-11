package mindroid.common.ev3.endpoints.motors.ev3;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import org.mindroid.common.messages.motor.*;

public abstract class AbstractRegulatedIMotor extends AbstractMotor implements IMotorMessageListener,RegulatedMotor {

    private MotorState motorState;

    public AbstractRegulatedIMotor(Port motorPort) {
        super(motorPort);
    }

    @Override
    public void close() {
        getMotor().close();
    }

    public MotorState getMotorState() {
        if(this.motorState != null){
            this.motorState.setAcceleration(getMotor().getAcceleration());
            this.motorState.setLimitAngle(getMotor().getLimitAngle());
            this.motorState.setMaxSpeed(getMotor().getMaxSpeed());
            this.motorState.setPosition(getMotor().getMaxSpeed());
            this.motorState.setRotationSpeed(getMotor().getRotationSpeed());
            this.motorState.setTachoCount(getMotor().getTachoCount());
        }else{
            this.motorState = new MotorState();
            return getMotorState();
        }
        return this.motorState;
    }

    public void setMotorState(MotorState motorState) {
        this.motorState = motorState;
    }

    public BaseRegulatedMotor getMotor(){
        return (BaseRegulatedMotor) motor;
    }

    @Override
    public void handleMotorMessage(Object msg) {
        if (msg instanceof ForwardMessage) {
            forward();
            return;
        }
        if (msg instanceof BackwardMessage) {
            backward();
            return;
        }
        if (msg instanceof StopMessage) {
            stop();
            return;
        }

        if (msg instanceof SetMotorSpeedMessage) {
            setSpeed(((SetMotorSpeedMessage) msg).getSpeed());
            return;
        }

        if(msg instanceof RotateMessage){
            rotate(((RotateMessage)msg).getAngle());
            return;
        }

        if(msg instanceof RotateToMessage){
            rotateTo(((RotateToMessage)msg).getAngle());
            return;
        }

        //TODO implement further messages
    }


    @Override
    public void addListener(RegulatedMotorListener regulatedMotorListener) {
        if(getMotor() != null) {
            getMotor().addListener(regulatedMotorListener);
        }
    }

    @Override
    public RegulatedMotorListener removeListener() {
        if(getMotor() != null) {
            return getMotor().removeListener();
        }
        return null;
    }

    @Override
    public void stop(boolean b) {
        if(getMotor() != null) {
            getMotor().stop(b);
        }
    }

    @Override
    public void flt(boolean b) {
        if(getMotor() != null) {
            getMotor().flt(b);
        }
    }

    @Override
    public void waitComplete() {
        if(getMotor() != null) {
            getMotor().waitComplete();
        }
    }

    @Override
    public void rotate(int i, boolean b) {
        if(getMotor() != null) {
            getMotor().rotate(i,b);
        }
    }

    @Override
    public void rotate(int i) {
        if(getMotor() != null) {
            getMotor().rotate(i);
        }
    }

    @Override
    public void rotateTo(int i) {
        if(getMotor() != null) {
            getMotor().rotateTo(i);
        }
    }

    @Override
    public void rotateTo(int i, boolean b) {
        if(getMotor() != null) {
            getMotor().rotateTo(i,b);
        }
    }

    @Override
    public int getLimitAngle() {
        if(getMotor() != null) {
            return getMotor().getLimitAngle();
        }
        return -1;
    }

    @Override
    public void setSpeed(int i) {
        if(getMotor() != null) {
            getMotor().setSpeed(i);
        }
    }

    @Override
    public int getSpeed() {
        if(getMotor() != null) {
            return getMotor().getSpeed();
        }
        return -1;
    }

    @Override
    public float getMaxSpeed() {
        if(getMotor() != null) {
            return getMotor().getMaxSpeed();
        }
        return -1;
    }

    @Override
    public boolean isStalled() {
        if(getMotor() != null) {
            return getMotor().isStalled();
        }
        return false;
    }

    @Override
    public void setStallThreshold(int i, int i1) {
        if(getMotor() != null) {
            getMotor().setStallThreshold(i,i1);
        }
    }

    @Override
    public void setAcceleration(int acceleration) {
        if(getMotor() != null) {
            getMotor().setAcceleration(acceleration);
        }
    }

    @Override
    public void synchronizeWith(RegulatedMotor[] regulatedMotors) {
        if(getMotor() != null) {
            getMotor().synchronizeWith(regulatedMotors);
        }
    }

    @Override
    public void startSynchronization() {
        if(getMotor() != null) {
            getMotor().startSynchronization();
        }
    }

    @Override
    public void endSynchronization() {
        if(getMotor() != null) {
            getMotor().endSynchronization();
        }
    }

    @Override
    public int getRotationSpeed() {
        if(getMotor() != null) {
            return getMotor().getRotationSpeed();
        }
        return -1;
    }

    @Override
    public int getTachoCount() {
        if(getMotor() != null) {
            return getMotor().getTachoCount();
        }
        return -1;
    }

    @Override
    public void resetTachoCount() {
        if(getMotor() != null) {
            getMotor().resetTachoCount();
        }
    }

}
