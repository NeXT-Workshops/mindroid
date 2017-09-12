package org.mindroid.common.messages.motor.synchronization;

public class SynchronizedMotorGroupCreatedMessage {

    private boolean isSuccess = false;

    public SynchronizedMotorGroupCreatedMessage(){

    }

    public SynchronizedMotorGroupCreatedMessage(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
