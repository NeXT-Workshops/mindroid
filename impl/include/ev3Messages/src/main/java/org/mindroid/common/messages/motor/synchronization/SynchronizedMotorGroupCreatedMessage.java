package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class SynchronizedMotorGroupCreatedMessage implements ILoggable {

    private boolean isSuccess = false;

    public SynchronizedMotorGroupCreatedMessage(){

    }

    public SynchronizedMotorGroupCreatedMessage(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
