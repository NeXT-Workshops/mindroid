package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class SynchronizedMotorOperation {

    private int value = 0;
    private OperationType optype = OperationType.NO_OPERATION;

    public SynchronizedMotorOperation(){
        //Necessary empty constructor for kryo
    }

    /**
     *
     * @param optype - type of the operation  {@link OperationType}
     */
    public SynchronizedMotorOperation(OperationType optype){
        this.optype = optype;
    }

    /**
     *
     * @param optype - type of the operation  {@link OperationType}
     * @param value  - has different meanings depending on OperationType {@link OperationType}
     *  {@link OperationType#ROTATE} ROTATE: angle - through which the motor will rotate
     *  {@link OperationType#ROTATE_TO} ROTATE TO: limitAngle - to which the motor will rotate, and then stop (in degrees). Includes any positive or negative int, even values bigger 360.
     *
     */
    public SynchronizedMotorOperation(OperationType optype,int value){
        this.optype = optype;
        this.value = value;
    }

    /**
     * Meaning of value:
     *  ROTATE: angle - through which the motor will rotate
     *  ROTATE TO: limitAngle - to which the motor will rotate, and then stop (in degrees). Includes any positive or negative int, even values bigger 360.
     *
     *  NO MEANING IN OTHER OPERATIONS THAN LISTED ABOVE!
     *
     * @param value value
     */
    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public OperationType getOptype() {
        return optype;
    }

}
