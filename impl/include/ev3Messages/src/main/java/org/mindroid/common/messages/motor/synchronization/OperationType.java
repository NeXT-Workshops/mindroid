package org.mindroid.common.messages.motor.synchronization;

/**
 * Operation type specifying the SynchronizedMotorOperation {@link SynchronizedMotorOperation}
 */
public enum OperationType {
    NO_OPERATION,
    SET_SPEED,
    FORWARD,
    BACKWARD,
    STOP,
    FLT,
    ROTATE,
    ROTATE_TO;
}
