package org.mindroid.common.messages.motor.synchronization;

public class SynchronizedOperationMessage {

    private SynchronizedMotorOperation[] operations;

    public SynchronizedOperationMessage(){

    }

    public SynchronizedOperationMessage(SynchronizedMotorOperation[] operations){
        this.operations = operations;
    }

    public SynchronizedMotorOperation[] getOperations() {
        return operations;
    }
}
