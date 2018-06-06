package org.mindroid.impl.motor;

import com.esotericsoftware.kryonet.Connection;
import org.mindroid.api.motor.ISynchronizedMotors;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.motor.synchronization.SyncedMotorOpCompleteMessage;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorMessageFactory;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.common.messages.motor.synchronization.SynchronizedOperationMessage;
import org.mindroid.impl.endpoint.ClientEndpointImpl;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.logging.EV3MsgLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SynchronizedMotorsEndpoint extends ClientEndpointImpl implements ISynchronizedMotors {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private final EV3MsgLogger msgRcvdLogger;
    private final EV3MsgLogger msgSendLogger;

    public SynchronizedMotorsEndpoint(String ip, int tcpPort, int brickTimeout) {
        super(ip, tcpPort, brickTimeout);

        //Init Loggers
        APILoggerManager.getInstance().registerLogger(LOGGER);
        msgRcvdLogger = new EV3MsgLogger(LOGGER,"Received ");
        msgSendLogger = new EV3MsgLogger(LOGGER,"Send ");
    }

    private boolean isBlockedOperationComplete = true;



    @Override
    public void received(Connection connection, Object object) {
        //Log msg
        if(object instanceof ILoggable){
            ((ILoggable) object).accept(msgRcvdLogger);
        }

        if(object instanceof SyncedMotorOpCompleteMessage){
            setBlockedOperationComplete(true);
        }
    }

    @Override
    public boolean executeSynchronizedOperation(SynchronizedMotorOperation operationPortA, SynchronizedMotorOperation operationPortB, SynchronizedMotorOperation operationPortC, SynchronizedMotorOperation operationPortD, boolean isBlocked) {
        if(isClientReady()){
            if(isBlocked){ //TODO how to interuppt if i want to stop statemachine/impImpl by App?
                //wait iff a blocked operation is currently running
                waitUntilBlockedOperationIsComplete();

                setBlockedOperationComplete(false);
                ILoggable msg = SynchronizedMotorMessageFactory.createSynchronizedMotorOperationMessage(operationPortA,operationPortB,operationPortC,operationPortD,isBlocked);
                msg.accept(msgSendLogger);
                client.sendTCP(msg);

                //Wait until the current operation is complete
                waitUntilBlockedOperationIsComplete();

            }else{
                //wait iff a blocked operation is currently running
                waitUntilBlockedOperationIsComplete();

                ILoggable msg = SynchronizedMotorMessageFactory.createSynchronizedMotorOperationMessage(operationPortA,operationPortB,operationPortC,operationPortD,isBlocked);
                msg.accept(msgSendLogger);

                client.sendTCP(msg);
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
            return executeSynchronizedOperation(operations[0], operations[1], operations[2], operations[3],isBlocked);
        }
        return false;
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
