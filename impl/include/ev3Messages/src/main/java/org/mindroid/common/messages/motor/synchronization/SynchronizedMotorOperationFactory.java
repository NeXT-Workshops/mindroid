package org.mindroid.common.messages.motor.synchronization;

public class SynchronizedMotorOperationFactory {

    private static final SynchronizedMotorOperation NO_OPERATION = new SynchronizedMotorOperation(OperationType.NO_OPERATION);
    private static final SynchronizedMotorOperation FLT = new SynchronizedMotorOperation(OperationType.FLT);
    private static final SynchronizedMotorOperation STOP = new SynchronizedMotorOperation(OperationType.STOP);


    /**
     * Creates a synchronized operation which causes the motor to rotate through angle
     * @param value angle - through which the motor will rotate
     * @return SynchronizedMotorOperation {@link SynchronizedMotorOperation}
     */
    public static SynchronizedMotorOperation createRotateOperation(int value){
        return new SynchronizedMotorOperation(OperationType.ROTATE,value);
    }

    /**
     * Creates a synchronized operation to rotate the motor to a specified angle
     * @param value limitAngle - to which the motor will rotate, and then stop (in degrees). Includes any positive or negative int, even values > 360.
     * @return SynchronizedMotorOperation {@link SynchronizedMotorOperation}
     */
    public static  SynchronizedMotorOperation createRotateToOperation(int value){
        return new SynchronizedMotorOperation(OperationType.ROTATE_TO,value);
    }

    /**
     * Creates a synchronized operation to set the motor into flt mode.
     * @return SynchronizedMotorOperation {@link SynchronizedMotorOperation}
     */
    public static SynchronizedMotorOperation createFltOperation(){
        return FLT;
    }

    /**
     * Creates a synchronized operation to stop a motor.
     * @return SynchronizedMotorOperation {@link SynchronizedMotorOperation}
     */
    public static SynchronizedMotorOperation createStopOperation(){
        return STOP;
    }

    /**
     * Creates an synchronized operation doing nothing
     * @return SynchronizedMotorOperation {@link SynchronizedMotorOperation}
     */
    public static SynchronizedMotorOperation createNoOperation(){
        return NO_OPERATION;
    }





}
