package org.mindroid.common.messages.motor.synchronization;

public class SynchronizedMotorMessageFactory {

    public static SynchronizedOperationMessage createSynchronizedMotorOperationMessage(SynchronizedMotorOperation opForPortA, SynchronizedMotorOperation opForPortB, SynchronizedMotorOperation opForPortC, SynchronizedMotorOperation opForPortD){
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

        return new SynchronizedOperationMessage(new SynchronizedMotorOperation[]{opForPortA,opForPortB,opForPortC,opForPortD});
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
}
