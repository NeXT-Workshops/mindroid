package org.mindroid.impl.motor;

import java.util.HashMap;
import java.util.Map;

import org.mindroid.api.endpoint.ClientEndpoint;
import org.mindroid.api.motor.IRegulatedMotor;
import org.mindroid.common.messages.brick.EndpointCreatedMessage;
import org.mindroid.common.messages.brick.BrickMessagesFactory;
import org.mindroid.common.messages.hardware.EV3MotorPort;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorGroupCreatedMessage;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorMessageFactory;
import org.mindroid.impl.brick.EV3Brick;
import org.mindroid.impl.brick.EV3BrickEndpoint;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


import org.mindroid.common.messages.NetworkPortConfig;


public class EV3MotorManager extends Listener {

    private Map<EV3MotorPort, IRegulatedMotor> motors;
    private Map<EV3MotorPort, ClientEndpoint> endpoints;

    private ClientEndpoint syncedMotorsEndpoint = null;

    private HashMap<EV3MotorPort,Integer> portToTCPPort;
    
    EV3BrickEndpoint ev3BrickEndpoint = null;
    private Client brickClient = null;
    
    public EV3MotorManager(EV3BrickEndpoint ev3BrickEndpoint) {
        this.ev3BrickEndpoint = ev3BrickEndpoint;
        
        portToTCPPort = new HashMap<EV3MotorPort,Integer>(4);
        portToTCPPort.put(EV3MotorPort.A, NetworkPortConfig.MOTOR_PORT_A);
        portToTCPPort.put(EV3MotorPort.B, NetworkPortConfig.MOTOR_PORT_B);
        portToTCPPort.put(EV3MotorPort.C, NetworkPortConfig.MOTOR_PORT_C);
        portToTCPPort.put(EV3MotorPort.D, NetworkPortConfig.MOTOR_PORT_D);
        
        endpoints = new HashMap<>(4);
        motors = new HashMap<>(4);
    }

    public EV3RegulatedMotorEndpoint createMotor(Motors motorType, EV3MotorPort motorPort) throws PortIsAlreadyInUseException {
		if(motorType != null && motorPort != null){
			if(motors.containsKey(motorPort)){
				throw new PortIsAlreadyInUseException(motorPort.toString());
			}else{
				//System.out.println("Local-EV3MotorManager: creating IMotor");
				EV3RegulatedMotorEndpoint ev3IMotor = null;
				switch(motorType){ //TODO may remove switch case?
					case MediumRegulatedMotor: ev3IMotor = new EV3RegulatedMotorEndpoint(ev3BrickEndpoint.EV3Brick_IP, portToTCPPort.get(motorPort), EV3BrickEndpoint.BRICK_TIMEOUT); break;
					case LargeRegulatedMotor: ev3IMotor = new EV3RegulatedMotorEndpoint(ev3BrickEndpoint.EV3Brick_IP, portToTCPPort.get(motorPort), EV3BrickEndpoint.BRICK_TIMEOUT); break;
					default: ev3IMotor = null;
				}

				motors.put(motorPort, ev3IMotor);
				endpoints.put(motorPort, (ClientEndpoint) ev3IMotor);
				return ev3IMotor;
			}


		}else{
			return null;
		}
    }

    public SynchronizedMotorsEndpoint createSynchronizedMotorsEndpoint(){
		return (SynchronizedMotorsEndpoint)(syncedMotorsEndpoint = (ClientEndpoint) new SynchronizedMotorsEndpoint(ev3BrickEndpoint.EV3Brick_IP,NetworkPortConfig.SYNCED_MOTOR_GROUP,EV3BrickEndpoint.BRICK_TIMEOUT));
	}

	/**
	 * Sends message to the Brick to create/initialize the IMotor at Brickside.
	 * @param motorType
	 * @param motorPort
     */
	public void initializeMotor(Motors motorType, EV3MotorPort motorPort) throws BrickIsNotReadyException {
		if(ev3BrickEndpoint.isBrickReady()){
			if(motorType != null && motorPort != null) {
				if (motors.containsKey(motorPort)) {
					brickClient.sendTCP(BrickMessagesFactory.createMotor(motorPort.getValue(), motorType, portToTCPPort.get(motorPort)));
				}else{
					//TODO throw Exception IMotor wurde noch nicht created!
				}
			}else{
				//TODO throw Illegal Argument Exception
			}
		}else{
			throw new BrickIsNotReadyException("Can't create a IMotor, because the Brick is not ready. Check Connection and/or try again!");
		}
	}

	/**
	 * Initializes the Synced Motor group to execute Synchronized motor operations
	 *
	 */
	public void initializeSyncedMotorGroup(){
		brickClient.sendTCP(SynchronizedMotorMessageFactory.createCreateSynchronizedMotorsMessage());
	}

	public void disconnectMotors(){
		for(EV3MotorPort key: motors.keySet()){
			if(motors.get(key) != null && (motors.get(key) instanceof ClientEndpoint)){
				((ClientEndpoint)motors.get(key)).disconnect();
			}
		}
	}

    @Override
    public void received(Connection connection, Object object){
		/** Message if the Endpoint-creation was successful or not **/
    	System.out.println("Local-EV3MotorManager: received a message!");
    	
		if(object.getClass() == EndpointCreatedMessage.class){
			EndpointCreatedMessage ecmsg = (EndpointCreatedMessage)object;
			System.out.println("Local-EV3MotorManager: Received a EndpointCreatedMessage! -> "+ecmsg.toString());
			if(ecmsg.isMotor()){
				if(ecmsg.isSuccess()){
					System.out.println("Local-EV3MotorManager: isSuccess at port "+ecmsg.getPort()+"#");
					if(motors.containsKey(EV3MotorPort.getPort(ecmsg.getPort()))){
						System.out.println("Local-EV3MotorManager: Endpoint creation successfull - connect to endpoint!");
						endpoints.get(EV3MotorPort.getPort(ecmsg.getPort())).connect();

						//Set that creation was a success on brick site. will be evaluated by configurator
						((EV3RegulatedMotorEndpoint)endpoints.get(EV3MotorPort.getPort(ecmsg.getPort()))).setHasCreationFailed(false);
					}else{
						System.out.println("Local-EV3MotorManager: Sensor does not exist");
					}
				}else{
					//Set that creation failed on brick site. will be evaluated by configurator
					((EV3RegulatedMotorEndpoint)endpoints.get(EV3MotorPort.getPort(ecmsg.getPort()))).setHasCreationFailed(true);
				}
			}
		}

		if(object.getClass() == SynchronizedMotorGroupCreatedMessage.class){
			SynchronizedMotorGroupCreatedMessage msg = (SynchronizedMotorGroupCreatedMessage)object;

			System.out.println("Local-EV3MotorManager: Received a SynchronizedMotorGroupCreatedMessage! -> "+msg.toString());
				if(msg.isSuccess() && syncedMotorsEndpoint != null){
					syncedMotorsEndpoint.connect();
				}else{
					//TODO Tell Sensor/IMotor Manager that endpoint creation failed
				}
		}
    }

	public void setBrickClient(Client brickClient) {
		this.brickClient = brickClient;
	}
}
