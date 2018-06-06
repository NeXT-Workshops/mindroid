package org.mindroid.impl.motor;

import org.mindroid.api.motor.ISynchronizedMotors;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;

public class SynchronizedMotors implements ISynchronizedMotors {

    private SynchronizedMotorsEndpoint syncedMotorsEndpoint;

    public SynchronizedMotors(SynchronizedMotorsEndpoint syncedMotorsEndpoint){
        this.syncedMotorsEndpoint = syncedMotorsEndpoint;
    }

    @Override
    public boolean executeSynchronizedOperation(SynchronizedMotorOperation operationPortA, SynchronizedMotorOperation operationPortB, SynchronizedMotorOperation operationPortC, SynchronizedMotorOperation operationPortD, boolean isBlocked) {
        return syncedMotorsEndpoint.executeSynchronizedOperation(operationPortA,operationPortB,operationPortC,operationPortD,isBlocked);
    }

    @Override
    public boolean executeSynchronizedOperation(SynchronizedMotorOperation[] operations, boolean isBlocked) {
        if(operations.length == 4) {
            return syncedMotorsEndpoint.executeSynchronizedOperation(operations,isBlocked);
        }else{
            return false;
        }
    }
}
