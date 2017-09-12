package mindroid.common.ev3.endpoints;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import lejos.hardware.port.PortException;
import lejos.robotics.RegulatedMotor;
import mindroid.common.ev3.endpoints.motors.ev3.synchronization.SynchronizedRegulatedMotorGroup;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.common.messages.motor.synchronization.SynchronizedOperationMessage;

public class SyncedMotorGroupEndpoint extends Listener {

    Connection conn;
    SynchronizedRegulatedMotorGroup syncedMotorGroup;

    public SyncedMotorGroupEndpoint(SynchronizedRegulatedMotorGroup syncedMotorGroup){
        this.syncedMotorGroup = syncedMotorGroup;
    }

    @Override
    public void connected(Connection connection) {
        try {
            conn = connection;
        } catch (PortException e) {
            connection.close();
            e.printStackTrace();
            throw new RuntimeException("SynchronizedMotor Group had an Error: "+e.getMessage());
        }
    }

    @Override
    public void received(Connection connection, Object msg){
        if(msg instanceof SynchronizedOperationMessage){
            runSynchronizedMotorOperations( ((SynchronizedOperationMessage) msg).getOperations() );
        }
    }

    @Override
    public void disconnected(Connection connection) {
        conn.close();
    }

    private void runSynchronizedMotorOperations(SynchronizedMotorOperation[] operations){
        syncedMotorGroup.executeSynchronizedOperation(operations);
    }
}
