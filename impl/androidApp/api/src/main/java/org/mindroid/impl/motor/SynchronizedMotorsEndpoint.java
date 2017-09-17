package org.mindroid.impl.motor;

import com.esotericsoftware.kryonet.Connection;
import org.mindroid.api.motor.ISynchronizedMotors;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorMessageFactory;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

public class SynchronizedMotorsEndpoint extends ClientEndpointImpl implements ISynchronizedMotors {

    public SynchronizedMotorsEndpoint(String ip, int tcpPort, int brickTimeout) {
        super(ip, tcpPort, brickTimeout);
    }

    @Override
    public void received(Connection connection, Object object) {

    }

    @Override
    public void executeSynchronizedOperation(SynchronizedMotorOperation operationPortA, SynchronizedMotorOperation operationPortB, SynchronizedMotorOperation operationPortC, SynchronizedMotorOperation operationPortD) {
        if(isClientReady()){
            client.sendTCP(SynchronizedMotorMessageFactory.createSynchronizedMotorOperationMessage(operationPortA,operationPortB,operationPortC,operationPortD));
            System.out.println("[SynchronizedMotorEndpoint:executeSynchronizedOperation] operation message sent");
        }else{
            System.out.println("[SynchronizedMotorEndpoint:executeSynchronizedOperation] operation message NOT sent");
        }
    }
}
