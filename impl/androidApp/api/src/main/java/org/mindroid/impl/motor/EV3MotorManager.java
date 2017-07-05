package org.mindroid.impl.motor;

import java.util.HashMap;
import java.util.Map;

import org.mindroid.api.endpoint.ClientEndpoint;
import org.mindroid.api.motor.Motor;
import org.mindroid.impl.brick.EV3Brick;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.mindroid.common.messages.BrickMessages;
import org.mindroid.common.messages.EV3MotorPort;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.NetworkPortConfig;


public class EV3MotorManager extends Listener {

    private Map<EV3MotorPort, Motor> motors;
    private Map<EV3MotorPort, ClientEndpoint> endpoints;

    private HashMap<EV3MotorPort,Integer> portToTCPPort;
    
    EV3Brick ev3Brick = null;
    private Client brickClient = null;
    
    public EV3MotorManager(EV3Brick ev3Brick) {
        this.ev3Brick = ev3Brick;
        
        portToTCPPort = new HashMap<EV3MotorPort,Integer>(4);
        portToTCPPort.put(EV3MotorPort.A, NetworkPortConfig.MOTOR_PORT_A);
        portToTCPPort.put(EV3MotorPort.B, NetworkPortConfig.MOTOR_PORT_B);
        portToTCPPort.put(EV3MotorPort.C, NetworkPortConfig.MOTOR_PORT_C);
        portToTCPPort.put(EV3MotorPort.D, NetworkPortConfig.MOTOR_PORT_D);
        
        endpoints = new HashMap<>(4);
        motors = new HashMap<>(4);
    }

    public Motor createMotor(Motors motorType, EV3MotorPort motorPort) throws PortIsAlreadyInUseException {
		if(motorType != null && motorPort != null){
			if(motors.containsKey(motorPort)){
				throw new PortIsAlreadyInUseException(motorPort.toString());
			}else{
				//System.out.println("Local-EV3MotorManager: creating Motor");
				Motor ev3Motor = null;
				switch(motorType){
					case UnregulatedMotor: ev3Motor = new EV3UnregulatedMotor(ev3Brick.EV3Brick_IP, portToTCPPort.get(motorPort), EV3Brick.BRICK_TIMEOUT); break;
					case MediumRegulatedMotor: ev3Motor = new EV3RegulatedMotor(ev3Brick.EV3Brick_IP, portToTCPPort.get(motorPort), EV3Brick.BRICK_TIMEOUT); break;
					default: ev3Motor = null;
				}

				motors.put(motorPort, ev3Motor);
				endpoints.put(motorPort, (ClientEndpoint)ev3Motor);
				return ev3Motor;
			}


		}else{
			return null;
		}
    }

	/**
	 * Sends message to the Brick to initialize the Motor at Brickside.
	 * @param motorType
	 * @param motorPort
     * @return
     */
	public void initializeMotor(Motors motorType, EV3MotorPort motorPort) throws BrickIsNotReadyException {
		if(ev3Brick.isBrickReady()){
			if(motorType != null && motorPort != null) {
				if (motors.containsKey(motorPort)) {
					brickClient.sendTCP(BrickMessages.createMotor(motorPort.getValue(), motorType, portToTCPPort.get(motorPort)));
				}else{
					//TODO throw Exception Motor wurde noch nicht created!
				}
			}else{
				//TODO throw Illegal Argument Exception
			}
		}else{
			throw new BrickIsNotReadyException("Can't create a Motor, because the Brick is not ready. Check Connection and/or try again!");
		}
	}

    @Override
    public void received(Connection connection, Object object){
		/** Message if the Endpoint-creation was successful or not **/
    	System.out.println("Local-EV3MotorManager: received a message!");
    	
		if(object.getClass() == BrickMessages.EndpointCreatedMessage.class){
			BrickMessages.EndpointCreatedMessage ecmsg = (BrickMessages.EndpointCreatedMessage)object;
			System.out.println("Local-EV3MotorManager: Received a EndpointCreatedMessage! -> "+ecmsg.toString());
			if(ecmsg.isMotor()){
				if(ecmsg.isSuccess()){
					System.out.println("Local-EV3MotorManager: isSuccess at port "+ecmsg.getPort()+"#");
					if(motors.containsKey(EV3MotorPort.getPort(ecmsg.getPort()))){
						System.out.println("Local-EV3MotorManager: Endpoint creation successfull - connect to endpoint!");
						endpoints.get(EV3MotorPort.getPort(ecmsg.getPort())).connect();
					}else{
						System.out.println("Local-EV3MotorManager: Sensor does not exist");
					}
				}else{
					//TODO Tell Sensor/Motor Manager that endpoint creation failed
				}
			}
		}
    }

	public void setBrickClient(Client brickClient) {
		this.brickClient = brickClient;
	}
}
