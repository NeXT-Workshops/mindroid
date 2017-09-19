package org.mindroid.common.messages.motor.synchronization;

public class SynchronizedOperationMessage {

    private SynchronizedMotorOperation[] operations;

    private boolean isBlocked = false;

    public SynchronizedOperationMessage(){

    }

    /**
     *
     * @param operations - array with operation for each motor (sorted by port A to D)
     * @param isBlocked - true to make it an blocked operation.
     */
    public SynchronizedOperationMessage(SynchronizedMotorOperation[] operations, boolean isBlocked){
        this.operations = operations;
        this.isBlocked = isBlocked;
    }

    public SynchronizedMotorOperation[] getOperations() {
        return operations;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
