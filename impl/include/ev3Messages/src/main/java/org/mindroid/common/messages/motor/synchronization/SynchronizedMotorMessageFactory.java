package org.mindroid.common.messages.motor.synchronization;

public class SynchronizedMotorMessageFactory {

    /**
     * Create a Message to execute a synced motor operation
     * @param opForPortA - operation on motor at port A
     * @param opForPortB - operation on motor at port B
     * @param opForPortC - operation on motor at port C
     * @param opForPortD - operation on motor at port D
     * @param isBlocked - true if it should be a blocked operation (can not be interrupted by another synced op)
     * @return the specified SynchronizedOperationMessage
     */
    public static SynchronizedOperationMessage createSynchronizedMotorOperationMessage(SynchronizedMotorOperation opForPortA, SynchronizedMotorOperation opForPortB, SynchronizedMotorOperation opForPortC, SynchronizedMotorOperation opForPortD,boolean isBlocked){
        if(opForPortA == null){
            opForPortA = SyncedMotorOpFactory.createNoOperation();
        }

        if(opForPortB == null){
            opForPortB = SyncedMotorOpFactory.createNoOperation();
        }

        if(opForPortC == null){
            opForPortC = SyncedMotorOpFactory.createNoOperation();
        }

        if(opForPortD == null){
            opForPortD = SyncedMotorOpFactory.createNoOperation();
        }

        return new SynchronizedOperationMessage(new SynchronizedMotorOperation[]{opForPortA,opForPortB,opForPortC,opForPortD},isBlocked);
    }

    /**
     * Returns a message creating a synchronized Group
     * @return Message to create a synchronized motor group
     */
    public static CreateSynchronizedMotorsMessage createCreateSynchronizedMotorsMessage(){
        return new CreateSynchronizedMotorsMessage();
    }

    public static SynchronizedMotorGroupCreatedMessage createCreationSuccessMessage(boolean isSuccess){
        return new SynchronizedMotorGroupCreatedMessage(isSuccess);
    }

    public static SyncedMotorOpCompleteMessage createSyncedMotorOperationCompleteMessage(){
        return new SyncedMotorOpCompleteMessage();
    }
}
