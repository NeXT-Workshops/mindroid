package org.mindroid.impl.sensor;


import java.util.HashMap;
import java.util.Map;


import org.mindroid.common.messages.*;
import org.mindroid.common.messages.brick.BrickMessagesFactory;
import org.mindroid.common.messages.brick.EndpointCreatedMessage;
import org.mindroid.common.messages.hardware.EV3SensorPort;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.brick.EV3Brick;
import org.mindroid.impl.brick.EV3BrickEndpoint;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * 
 * Manages the SensorEndpoints.
 * Has a connection to the Brick.
 * 
 * @author Torben
 *
 */
public class EV3SensorManager extends Listener{

	EV3BrickEndpoint ev3BrickEndpoint;
	
    private Map<EV3SensorPort, EV3SensorEndpoint> sensorEndpoints;

    private HashMap<EV3SensorPort,Integer> portToTCPPort;
    
    private Client brickClient = null;
    
    public EV3SensorManager(EV3BrickEndpoint ev3Brick) {
        this.ev3BrickEndpoint = ev3Brick;
        
        portToTCPPort = new HashMap<EV3SensorPort,Integer>(4);
        portToTCPPort.put(EV3SensorPort.S1, NetworkPortConfig.SENSOR_PORT_1);
        portToTCPPort.put(EV3SensorPort.S2, NetworkPortConfig.SENSOR_PORT_2);
        portToTCPPort.put(EV3SensorPort.S3, NetworkPortConfig.SENSOR_PORT_3);
        portToTCPPort.put(EV3SensorPort.S4, NetworkPortConfig.SENSOR_PORT_4);
        
       
        sensorEndpoints = new HashMap<>(4);
        
    }
    

    /**
     * Creates a Sensor.
     * 
     * @return the sensor handle
     * @throws PortIsAlreadyInUseException if the specified {@link EV3SensorPort} is already in use
     */
    public EV3SensorEndpoint createSensor(EV3PortID brick_port, Sensors sensorType, EV3SensorPort sensorPort, Sensormode mode) throws PortIsAlreadyInUseException{
		if(sensorType != null && sensorPort != null){
			if(sensorEndpoints.containsKey(sensorPort)){
				throw new PortIsAlreadyInUseException(sensorPort.toString());
			}else{
				EV3SensorEndpoint ev3SensorEndpoint = new EV3SensorEndpoint(ev3BrickEndpoint.EV3Brick_IP, portToTCPPort.get(sensorPort), EV3BrickEndpoint.BRICK_TIMEOUT,sensorType,brick_port, mode);
				sensorEndpoints.put(sensorPort, ev3SensorEndpoint);
				return ev3SensorEndpoint;
			}
		}else{
			return null;
		}
    }

	public void initializeSensor(Sensors sensorType, EV3SensorPort sensorPort) throws BrickIsNotReadyException {
		System.out.println("Local-EV3SensorManager: initSensor called");
		if(ev3BrickEndpoint.isBrickReady()){
			if(sensorType != null && sensorPort != null){
				if(sensorEndpoints.containsKey(sensorPort)){
					brickClient.sendTCP(BrickMessagesFactory.createSensor(sensorPort.getValue(), sensorType, portToTCPPort.get(sensorPort)));
				}else{
					//TODO throw SensorPort is not defined Exception
				}
			}else{
				//TODO throw illegal Argument Exception
			}
		}else{
			throw new BrickIsNotReadyException("Can't create a Sensor, because the Brick is not ready. Check Connection and/or try again!");
		}
	}

    @Override
    public void received(Connection connection, final Object object){
		/** Message if the Endpoint-creation was successful or not **/
    	
    	Runnable handleMessage = new Runnable(){
			@Override
			public void run(){
		    	if(object.getClass() == EndpointCreatedMessage.class){
					EndpointCreatedMessage ecmsg = (EndpointCreatedMessage)object;
					
					if(ecmsg.isSensor()){
						System.out.println("Local-EV3SensorManager: Received a EndpointCreatedMessage! -> "+ecmsg.toString());
						
						if(ecmsg.isSuccess()){
							System.out.println("Local-EV3SensorManager: isSuccess at port "+ecmsg.getPort()+"#");
							if(sensorEndpoints.containsKey(EV3SensorPort.getPort(ecmsg.getPort()))){
								System.out.println("Local-EV3SensorManager: Endpoint creation successfull - connect to endpoint!");
								sensorEndpoints.get(EV3SensorPort.getPort(ecmsg.getPort())).connect();
								sensorEndpoints.get(EV3SensorPort.getPort(ecmsg.getPort())).initSensorMode();

								//Set that creation was a success on brick site. will be evaluated by configurator
								((EV3SensorEndpoint) sensorEndpoints.get(EV3SensorPort.getPort(ecmsg.getPort()))).setHasCreationFailed(false);
							}else{
								System.out.println("Local-EV3SensorManager: Sensor does not exist");
							}
						}else{
							//Set that creation hast failed on brick site. will be evaluated by configurator
							((EV3SensorEndpoint) sensorEndpoints.get(EV3SensorPort.getPort(ecmsg.getPort()))).setHasCreationFailed(true);
						}
					}
				}
			}
    	};
    	new Thread(handleMessage).start();
    }

	/**
	 * disconnects the Sensor Connections
	 */
	public void disconnectSensors(){
		for(EV3SensorPort key: sensorEndpoints.keySet()){
			if(sensorEndpoints.get(key) != null){
				sensorEndpoints.get(key).disconnect();
			}
		}
	}

	public void setBrickClient(Client brickClient) {
		this.brickClient = brickClient;
	}




}
