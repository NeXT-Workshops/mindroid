package org.mindroid.impl.motor;

import org.mindroid.api.motor.ISynchronizedMotors;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;

public class SynchronizedMotors implements ISynchronizedMotors {

    private SynchronizedMotorsEndpoint syncedMotorsEndpoint;

    public SynchronizedMotors(SynchronizedMotorsEndpoint syncedMotorsEndpoint){
        this.syncedMotorsEndpoint = syncedMotorsEndpoint;
    }

    @Override
    public void executeSynchronizedOperation(SynchronizedMotorOperation operationPortA, SynchronizedMotorOperation operationPortB, SynchronizedMotorOperation operationPortC, SynchronizedMotorOperation operationPortD) {
        syncedMotorsEndpoint.executeSynchronizedOperation(operationPortA,operationPortB,operationPortC,operationPortD);
    }
}
