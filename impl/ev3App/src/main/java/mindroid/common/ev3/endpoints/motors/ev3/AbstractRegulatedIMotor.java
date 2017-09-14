package mindroid.common.ev3.endpoints.motors.ev3;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import mindroid.common.ev3.endpoints.motors.ev3.exceptions.NoMotorFoundException;
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

    // TODO maybe only send motorState on request? auslastung?
    public MotorState getMotorState() {
        if(this.motorState != null){
            this.motorState.setAcceleration(getMotor().getAcceleration());
            this.motorState.setLimitAngle(getMotor().getLimitAngle());
            this.motorState.setMaxSpeed(getMotor().getMaxSpeed());
            this.motorState.setPosition(getMotor().getMaxSpeed());
            this.motorState.setRotationSpeed(getMotor().getRotationSpeed());
            this.motorState.setTachoCount(getMotor().getTachoCount());
            this.motorState.setMoving(getMotor().isMoving());
            this.motorState.setSpeed(getMotor().getSpeed());
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
        System.out.println(msg.toString());
        if(motor == null){
            new NoMotorFoundException("Motor not found: "+motorPort.getName()).printStackTrace();
            return;
        }

        if (msg instanceof ForwardMessage) {
            forward();
            return;
        }
        if (msg instanceof BackwardMessage) {
            backward();
            return;
        }
        if (msg instanceof StopMessage) {
            stop(((StopMessage) msg).isImmidiateReturn());
            return;
        }

        if (msg instanceof SetMotorSpeedMessage) {
            setSpeed(((SetMotorSpeedMessage) msg).getSpeed());
            return;
        }

        if(msg instanceof RotateMessage){
            rotate(((RotateMessage)msg).getAngle(),((RotateMessage) msg).isImmediateReturn());
            return;
        }

        if(msg instanceof RotateToMessage){
            rotateTo(((RotateToMessage)msg).getAngle(),((RotateToMessage) msg).isImmediateRetrun());
            return;
        }

        if(msg instanceof FltMessage){
            flt(((FltMessage) msg).isImmediateRetrun());
        }

        if(msg instanceof AccelerationMessage){
            setAcceleration(((AccelerationMessage) msg).getAcceleration());
        }
        
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
    public void stop(boolean immidiateReturn) {
        if(getMotor() != null) {
            getMotor().stop(immidiateReturn);
        }
    }

    @Override
    public void flt(boolean immidiateReturn) {
        if(getMotor() != null) {
            getMotor().flt(immidiateReturn);
        }
    }

    @Override
    public void waitComplete() {
        if(getMotor() != null) {
            getMotor().waitComplete();
        }
    }

    @Override
    public void rotate(int angle, boolean immidiateReturn) {
        if(getMotor() != null) {
            getMotor().rotate(angle,immidiateReturn);
        }
    }

    @Override
    public void rotate(int angle) {
        if(getMotor() != null) {
            getMotor().rotate(angle);
        }
    }

    @Override
    public void rotateTo(int limitAngle) {
        if(getMotor() != null) {
            getMotor().rotateTo(limitAngle);
        }
    }

    @Override
    public void rotateTo(int limitAngle, boolean immidiateReturn) {
        if(getMotor() != null) {
            getMotor().rotateTo(limitAngle,immidiateReturn);
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
    public void setSpeed(int speed) {
        if(getMotor() != null) {
            getMotor().setSpeed(speed);
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
