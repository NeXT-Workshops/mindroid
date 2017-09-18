package org.mindroid.api.motor;

import org.mindroid.common.messages.motor.synchronization.OperationType;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;

public interface ISynchronizedMotors {

    /**
     * Execute synchronized operations on Motors
     *
     * @param operationPortA operation executed on Motor at Port A
     * @param operationPortB operation executed on Motor at Port B
     * @param operationPortC operation executed on Motor at Port C
     * @param operationPortD operation executed on Motor at Port D
     */
    void executeSynchronizedOperation(SynchronizedMotorOperation operationPortA, SynchronizedMotorOperation operationPortB, SynchronizedMotorOperation operationPortC, SynchronizedMotorOperation operationPortD);

    /**
     * The length of operations has to be 4
     * @param operations - operations as array. Has to be 4 sorted by port A to D.
     */
    void executeSynchronizedOperation(SynchronizedMotorOperation[] operations);
}
