package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.hardware.EV3MotorPort;

public class SynchronizedMotorMessageFactory {

    public static SynchronizedOperationMessage createSynchronizedMotorOperation(SynchronizedMotorOperation opForPortA,SynchronizedMotorOperation opForPortB,SynchronizedMotorOperation opForPortC,SynchronizedMotorOperation opForPortD){
        if(opForPortA == null){
            opForPortA = SynchronizedMotorOperationFactory.createNoOperation();
        }

        if(opForPortB == null){
            opForPortB = SynchronizedMotorOperationFactory.createNoOperation();
        }

        if(opForPortC == null){
            opForPortC = SynchronizedMotorOperationFactory.createNoOperation();
        }

        if(opForPortD == null){
            opForPortD = SynchronizedMotorOperationFactory.createNoOperation();
        }

        return new SynchronizedOperationMessage(new SynchronizedMotorOperation[]{opForPortA,opForPortB,opForPortC,opForPortD});
    }

    public static CreateSynchronizedMotorsMessage createCreateSynchronizedMotorsMessage(EV3MotorPort[] ports){
        return new CreateSynchronizedMotorsMessage(ports);
    }
}
