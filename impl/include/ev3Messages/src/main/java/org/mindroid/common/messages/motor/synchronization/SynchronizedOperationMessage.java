package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class SynchronizedOperationMessage implements ILoggable {

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

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
