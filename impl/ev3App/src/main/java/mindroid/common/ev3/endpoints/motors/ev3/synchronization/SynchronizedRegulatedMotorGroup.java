package mindroid.common.ev3.endpoints.motors.ev3.synchronization;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import mindroid.common.ev3.app.MotorManager;
import mindroid.common.ev3.endpoints.MotorEndpoint;
import mindroid.common.ev3.endpoints.motors.ev3.AbstractMotor;
import mindroid.common.ev3.endpoints.motors.ev3.AbstractRegulatedIMotor;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  This class synchronizes Regulated Motors.
 *  It is used to execute Motor operations synchronized with the given Motors.
 *
 *  @author Torben
 */
public class SynchronizedRegulatedMotorGroup {
    /**
     *  {@value #MAX_MOTOR_COUNT}
     */
    public static final int MAX_MOTOR_COUNT = 4;


    private Port[] motorports = {MotorPort.A, MotorPort.B,MotorPort.C,MotorPort.D};
    private int motorPortIndex = -1;

    private MotorManager mManger;

    public SynchronizedRegulatedMotorGroup(MotorManager mManager){
        this.mManger = mManager;

    }

    /**
     * This Method creates a synchronized Block to execute the given Motor operations.
     *
     * @param operations - operations {@link SynchronizedMotorOperation} sorted by Motorport [A,..,D] to [operation,..,operation] size has to be equal {@link #MAX_MOTOR_COUNT}
     */
    public void executeSynchronizedOperation(SynchronizedMotorOperation[] operations,boolean isBlocked){
            if(operations.length != motorports.length){
                //TODO May throw an error
                return;
            }
            AbstractRegulatedIMotor firstMotor = getFirstMotor();
            AbstractRegulatedIMotor[] motorPartnersEndpoints = getMotorPartners();
            if(firstMotor != null && this.motorPortIndex != -1) {
                //Synchronize motors
                firstMotor.synchronizeWith(motorPartnersEndpoints);

                //Start synchronization block
                firstMotor.startSynchronization();

                //Execute Operations. Information: Only non blocking calls (operations) can be used between start/end.!!
                AbstractRegulatedIMotor motor;
                for (int i = 0; i < motorports.length; i++) {
                    motor = getRegulatedMotor(motorports[i]);
                    if(motor != null) {
                        executeOperation(motor, operations[i]);
                    }
                }

                //End Synchronization block
                firstMotor.endSynchronization();

                //Wait until complete.
                if(isBlocked) {
                    for (int i = 0; i < motorPartnersEndpoints.length; i++) {
                        if (motorPartnersEndpoints[i] != null) {
                            motorPartnersEndpoints[i].waitComplete();
                        }
                    }
                }
            }else{
                //NO MOTOR FOUND
            }

    }

    /**
     * Returns the first available motor.
     * @return
     */
    private AbstractRegulatedIMotor getFirstMotor(){
        AbstractRegulatedIMotor motor = null;
        this.motorPortIndex = -1;
        for (int i = 0; i < motorports.length; i++) {
            if((motor = getRegulatedMotor(motorports[i])) != null){
                motorPortIndex = i;
                return motor;
            }
        }
        return null;
    }

    private AbstractRegulatedIMotor getRegulatedMotor(Port motorport){
        MotorEndpoint mEndpoint = null;
        if((mEndpoint = mManger.getMotorEndpoint(motorport)) != null) {
            if (mEndpoint.getMotor() instanceof AbstractRegulatedIMotor) {
                return (AbstractRegulatedIMotor) mEndpoint.getMotor();
            }
        }
        return null;
    }

    /**
     * Executes the Operation on the given Motor.
     * This is only called in a synchronized motor operation block!
     * @param syncMotor - motor to execute the operation on
     * @param operation - operation to execute {@link SynchronizedMotorOperation}
     */
    private void executeOperation(AbstractRegulatedIMotor syncMotor, SynchronizedMotorOperation operation) {
        if(syncMotor != null && operation != null){
            switch(operation.getOptype()){
                case FORWARD:
                    syncMotor.forward();
                    break;
                case BACKWARD:
                    syncMotor.backward();
                    break;
                case FLT:
                    syncMotor.flt(true);
                    break;
                case STOP:
                    syncMotor.stop(true);
                    break;
                case ROTATE:
                    syncMotor.rotate(operation.getValue(),true);
                    break;
                case ROTATE_TO:
                    syncMotor.rotateTo(operation.getValue(),true);
                    break;
                case NO_OPERATION:
                    break;//DO nothing
            }
        }
    }

    /**
     * Returns the motor parners the first motor should be synchronized with
     * @return returns the motor partners to sync with.
     */
    private AbstractRegulatedIMotor[] getMotorPartners(){
        motorPortIndex++;
        ArrayList<AbstractRegulatedIMotor> motorPartners = new ArrayList(MAX_MOTOR_COUNT-1);
        MotorEndpoint mEndpoint = null;
        for (int i = motorPortIndex; i < MAX_MOTOR_COUNT; i++) {
            if((mEndpoint = mManger.getMotorEndpoint(motorports[i])) != null){
                if(mEndpoint.getMotor() instanceof AbstractRegulatedIMotor){
                    motorPartners.add((AbstractRegulatedIMotor) mEndpoint.getMotor());
                }
            }
        }
        return motorPartners.toArray(new AbstractRegulatedIMotor[motorPartners.size()]);
    }
}
