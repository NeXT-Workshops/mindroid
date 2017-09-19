package mindroid.common.ev3.app;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Listener;

import lejos.internal.ev3.EV3Port;
import mindroid.common.ev3.endpoints.SyncedMotorGroupEndpoint;
import mindroid.common.ev3.endpoints.motors.ev3.AbstractMotor;
import mindroid.common.ev3.endpoints.motors.ev3.AbstractRegulatedIMotor;
import mindroid.common.ev3.endpoints.motors.ev3.synchronization.SynchronizedRegulatedMotorGroup;
import org.mindroid.common.messages.NetworkPortConfig;
import org.mindroid.common.messages.hardware.EV3MotorPort;
import org.mindroid.common.messages.hardware.Motors;
import lejos.hardware.port.Port;
import mindroid.common.ev3.endpoints.MotorEndpoint;
import mindroid.common.ev3.endpoints.motors.ev3.LargeRegulatedIMotor;
import mindroid.common.ev3.endpoints.motors.ev3.MediumRegulatedIMotor;
import mindroid.common.ev3.server.BrickServerImpl;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.common.messages.motor.synchronization.SynchronizedOperationMessage;

public class MotorManager extends HardwareInterfaceManager{


	public MotorManager(BrickType bType) {
		super(bType);
	}

	private SynchronizedRegulatedMotorGroup syncedMotorGroup = null;
	private SyncedMotorGroupEndpoint syncedMotorEndpoint = null;

	public boolean createMotorEndpoint(Port port, Motors motortype, int networkPort) throws IOException {
		if(endpoints.containsKey(port)){
			//Ports bereits offen
			if(((MotorEndpoint)endpoints.get(port)).getMotor() != null) {
				if(((MotorEndpoint)endpoints.get(port)).getMotor().getMotortype() == motortype){
					return true;
				}
				//System.out.println("Motor.createMotorEndpoint() at "+port.toString()+" -> aktueller motortyp passt nicht");
			}
			
			((MotorEndpoint)endpoints.get(port)).getMotor().close();
			//System.out.println("Motor.createMotorEndpoint() at "+port.toString()+" -> aktueller motortyp passt nicht - erzeuge neuen motortyp");
			switch(motortype){
				case MediumRegulatedMotor:
					((MotorEndpoint)endpoints.get(port)).setMotor(new MediumRegulatedIMotor(port));
					return true;
				case LargeRegulatedMotor:
					((MotorEndpoint)endpoints.get(port)).setMotor(new LargeRegulatedIMotor(port));
					return true;
				default: return false;
			}
		}
		
		Listener endpoint = null;

		switch(motortype){
			case MediumRegulatedMotor:
				endpoint = new MotorEndpoint(new MediumRegulatedIMotor(port));
				break;
			case LargeRegulatedMotor:
				endpoint = new MotorEndpoint(new LargeRegulatedIMotor(port));
				break;
			default: return false;
		}
		
		if(endpoint != null){
			endpoints.put(port,endpoint);
			
			//Create Server for Motor //TODO remove to ServerBuilder/-Manager.class
			BrickServerImpl brickServer = new BrickServerImpl(networkPort);
			brickServer.addListener(endpoint);
			
			return true;
		}else{
			return false;
		}
	}

	public boolean createSynchronizedMotorEndpoint(){
		//Create Synchronized Motor Group
		if(syncedMotorGroup == null) {
			syncedMotorGroup = new SynchronizedRegulatedMotorGroup(this);
		}

		if(syncedMotorEndpoint == null) {
			syncedMotorEndpoint = new SyncedMotorGroupEndpoint(syncedMotorGroup);
			BrickServerImpl brickServer = new BrickServerImpl(NetworkPortConfig.SYNCED_MOTOR_GROUP);
			brickServer.addListener(syncedMotorEndpoint);
		}

		return true;
	}


	public MotorEndpoint getMotorEndpoint(Port motorport){
		return (MotorEndpoint) endpoints.get(motorport);
	}

}
