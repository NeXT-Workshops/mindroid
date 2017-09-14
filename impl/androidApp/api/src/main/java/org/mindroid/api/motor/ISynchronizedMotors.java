package org.mindroid.api.motor;

import org.mindroid.common.messages.motor.synchronization.OperationType;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;

public interface ISynchronizedMotors {

    void executeSynchronizedOperation(SynchronizedMotorOperation operationPortA, SynchronizedMotorOperation operationPortB, SynchronizedMotorOperation operationPortC, SynchronizedMotorOperation operationPortD);

}
