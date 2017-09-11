package mindroid.common.ev3.endpoints.motors.ev3.synchronization;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import mindroid.common.ev3.endpoints.motors.ev3.AbstractRegulatedIMotor;

import java.util.HashMap;

/**
 *  This class synchronizes Regulated Motors.
 *  This class is used to execute Motor operations synchronized with the given Motors.
 *
 *  @author Torben
 */
public class SynchronizedRegulatedMotorGroup {

    /**
     *  {@value #MIN_MOTOR_COUNT}
     */
    public static final int MIN_MOTOR_COUNT = 2;

    /**
     *  {@value #MAX_MOTOR_COUNT}
     */
    public static final int MAX_MOTOR_COUNT = 4;

    private AbstractRegulatedIMotor[] synchronizedMotors;

    //Number of motors to synchronize, gets set by validateSynchronizedMotors() method;
    private int nbOfMotors = -1;

    private boolean isMotorSetValid = false;

    private Port[] motorports = {MotorPort.A, MotorPort.B,MotorPort.C,MotorPort.D};

    public SynchronizedRegulatedMotorGroup(){

    }

    /**
     * This Method creates a synchronized Block to execute the given Motor operations.
     * Each given motor, defined by {@link #setSynchronizedMotors(AbstractRegulatedIMotor[])} can only execute one non blocking operation.
     *
     * @param operations - operations {@link SynchronizedMotorOperation} sorted by Motorport [A,..,D] to [operation,..,operation] size has to be equal {@link #MAX_MOTOR_COUNT}
     */
    public synchronized void executeSynchronizedOperation(SynchronizedMotorOperation[] operations){
        if(isMotorSetValid() && operations.length == MAX_MOTOR_COUNT){
            int start_index = getStartIndex();
            if(start_index >= 0 ){
                AbstractRegulatedIMotor[] motorPartners = getMotorPartners(start_index);

                //Synchronize motors
                synchronizedMotors[start_index].synchronizeWith(motorPartners);

                //Start synchronization block
                synchronizedMotors[start_index].startSynchronization();

                //Execute Operations. Information: Only non blocking calls (operations) can be used between start/end.!!
                for (int i = 0; i < synchronizedMotors.length; i++) {
                    executeOperation(synchronizedMotors[i],operations[i]);
                }

                //End Synchronization block
                synchronizedMotors[start_index].endSynchronization();

                //Wait until complete.
                for (int i = 0; i < synchronizedMotors.length; i++) {
                    if(synchronizedMotors[i] != null){
                        synchronizedMotors[i].waitComplete();
                    }
                }
            }
        }
    }

    /**
     * Executes the Operation on the given Motor.
     * This is only called in a synchronized motor operation block!
     * @param syncMotor - motor to execute the operation on
     * @param operation - operation to execute {@link SynchronizedMotorOperation}
     */
    private void executeOperation(AbstractRegulatedIMotor syncMotor, SynchronizedMotorOperation operation) {
        if(syncMotor != null && operation != null){
            switch(operation){
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
     * Returns the first motor.
     * @return the first motor.
     */
    private int getStartIndex(){
        for (int i = 0; i < synchronizedMotors.length; i++) {
            if(synchronizedMotors[i] != null){
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the motor parners the first motor should be synchronized with
     * @param start_index index of the first motor
     * @return returns the motor partners to sync with.
     */
    private AbstractRegulatedIMotor[] getMotorPartners(int start_index){
        start_index++;
        AbstractRegulatedIMotor[] motorPartners = new AbstractRegulatedIMotor[nbOfMotors - 1];
        int index_partner = 0;
        for (int i = start_index; i < synchronizedMotors.length; i++) {
            if(synchronizedMotors[i] != null) {
                motorPartners[index_partner] = synchronizedMotors[i];
                index_partner++;
            }
        }
        return motorPartners;
    }


    /**
     * Checks if all given Motors are unique and more than one exists.
     * Sorts the motors by port A->D. Stored in the field synchronizedMotors.
     * Sets the nbOfMotors field.
     * Result is stored in local field which can be obtained by calling {@link #isMotorSetValid()} .
     */
    private void validateSynchronizedMotors(){
        if(checkArraySize()){
            HashMap<Port,AbstractRegulatedIMotor> portToMotorMapping = new HashMap<Port,AbstractRegulatedIMotor>(synchronizedMotors.length);

            for (AbstractRegulatedIMotor motor : synchronizedMotors) {
                portToMotorMapping.put(motor.getMotorPort(),motor);
            }
            nbOfMotors = portToMotorMapping.size();
            synchronizedMotors = new AbstractRegulatedIMotor[MAX_MOTOR_COUNT];

            //Sort motors by port A->D; rebuild synchronizedMotors [A->D] == [0,3] Array
            for (int i = 0; i < motorports.length; i++) {
                if(portToMotorMapping.containsKey(motorports[i])){
                    synchronizedMotors[i] = portToMotorMapping.get(motorports[i]);
                }else{
                    synchronizedMotors[i] = null;
                }
            }
            isMotorSetValid = true;
        }else {
            isMotorSetValid = false;
        }
    }

    /**
     * Checks if the given Motor Array has a proper size.
     * The allowed size is defined by {@link #MIN_MOTOR_COUNT} and {@link #MAX_MOTOR_COUNT}
     * @return true - if enough motors are registered/set.
     */
    private boolean checkArraySize(){
        return (this.synchronizedMotors.length >= MIN_MOTOR_COUNT && this.synchronizedMotors.length <= MAX_MOTOR_COUNT);
    }

    /**
     * Sets the motors operating synchronized
     * Also checks if all motors are valid, means unique. Result is stored and can be obtained by calling @link #isMotorSetValid()}.
     * @param synchronizedMotors set of motors which should execute operation synchronized
     */
    public void setSynchronizedMotors(AbstractRegulatedIMotor[] synchronizedMotors) {
        this.synchronizedMotors = synchronizedMotors;
        validateSynchronizedMotors();
    }

    /**
     * Returns true if the motor set is valid and fulfills the rules of the class.
     * Size has to be betweend {@link #MIN_MOTOR_COUNT} and {@link #MAX_MOTOR_COUNT}
     * @return true - if motorset is valid
     */
    public boolean isMotorSetValid() {
        return isMotorSetValid;
    }



}
