package org.mindroid.impl.motor;

import com.esotericsoftware.kryonet.Connection;
import org.mindroid.api.motor.ISynchronizedMotors;
import org.mindroid.common.messages.motor.synchronization.SyncedMotorOpCompleteMessage;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorMessageFactory;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

public class SynchronizedMotorsEndpoint extends ClientEndpointImpl implements ISynchronizedMotors {

    public SynchronizedMotorsEndpoint(String ip, int tcpPort, int brickTimeout) {
        super(ip, tcpPort, brickTimeout);
    }

    private boolean isBlockedOperationComplete = true;

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof SyncedMotorOpCompleteMessage){
            setBlockedOperationComplete(true);
        }
    }

    @Override
    public boolean executeSynchronizedOperation(SynchronizedMotorOperation operationPortA, SynchronizedMotorOperation operationPortB, SynchronizedMotorOperation operationPortC, SynchronizedMotorOperation operationPortD, boolean isBlocked) {
        if(isClientReady()){
            if(isBlocked){ //TODO how to interuppt if i want to stop statemachine/impImpl by App?
                System.out.println("[SynchronizedMotorsEndpoint:execute] is blocked is true");
                //wait iff a blocked operation is currently running
                waitUntilBlockedOperationIsComplete();

                System.out.println("[SynchronizedMotorsEndpoint:execute] setBlocked operation false");
                setBlockedOperationComplete(false);
                System.out.println("[SynchronizedMotorsEndpoint:execute] sending Message");
                client.sendTCP(SynchronizedMotorMessageFactory.createSynchronizedMotorOperationMessage(operationPortA,operationPortB,operationPortC,operationPortD,isBlocked));

                //Wait until the current operation is complete
                waitUntilBlockedOperationIsComplete();

            }else{
                //wait iff a blocked operation is currently running
                waitUntilBlockedOperationIsComplete();
                
                client.sendTCP(SynchronizedMotorMessageFactory.createSynchronizedMotorOperationMessage(operationPortA,operationPortB,operationPortC,operationPortD,isBlocked));
            }

            //Message sent
            return true;

        }else{
            //Message NOT sent
            return false;
        }
    }

    private void waitUntilBlockedOperationIsComplete() {
        while(!isBlockedOperationComplete()){
            sleep(25);
        }
    }

    @Override
    public boolean executeSynchronizedOperation(SynchronizedMotorOperation[] operations, boolean isBlocked) {
        if(operations.length == 4) {
            executeSynchronizedOperation(operations[0], operations[1], operations[2], operations[3],isBlocked);
        }
    }

    private void sleep(long delay){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isBlockedOperationComplete() {
        return isBlockedOperationComplete;
    }

    public synchronized void setBlockedOperationComplete(boolean blockedOperationComplete) {
        this.isBlockedOperationComplete = blockedOperationComplete;
    }
}
