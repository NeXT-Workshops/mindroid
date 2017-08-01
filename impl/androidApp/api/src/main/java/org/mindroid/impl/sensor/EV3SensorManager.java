package org.mindroid.impl.sensor;


import java.util.HashMap;
import java.util.Map;


import org.mindroid.common.messages.*;
import org.mindroid.impl.brick.EV3Brick;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * 
 * Manages the Sensors.
 * Has a connection to the Brick.
 * 
 * @author Torben
 *
 */
public class EV3SensorManager extends Listener{

	EV3Brick ev3Brick;	
	
    private Map<EV3SensorPort, EV3Sensor> sensors;

    private HashMap<EV3SensorPort,Integer> portToTCPPort;
    
    private Client brickClient = null;
    
    public EV3SensorManager(EV3Brick ev3Brick) {
        this.ev3Brick = ev3Brick;
        
        portToTCPPort = new HashMap<EV3SensorPort,Integer>(4);
        portToTCPPort.put(EV3SensorPort.S1, NetworkPortConfig.SENSOR_PORT_1);
        portToTCPPort.put(EV3SensorPort.S2, NetworkPortConfig.SENSOR_PORT_2);
        portToTCPPort.put(EV3SensorPort.S3, NetworkPortConfig.SENSOR_PORT_3);
        portToTCPPort.put(EV3SensorPort.S4, NetworkPortConfig.SENSOR_PORT_4);
        
       
        sensors = new HashMap<>(4);
        
    }
    

    /**
     * Creates a Sensor.
     * 
     * @param sensorType
     * @param sensorPort
     * @return
     * @throws BrickIsNotReadyException 
     * @throws PortIsAlreadyInUseException 
     */
    public EV3Sensor createSensor(EV3PortID brick_port, Sensors sensorType, EV3SensorPort sensorPort, SensorMessages.SensorMode_ mode) throws PortIsAlreadyInUseException{
		if(sensorType != null && sensorPort != null){
			if(sensors.containsKey(sensorPort)){
				throw new PortIsAlreadyInUseException(sensorPort.toString());
			}else{
				EV3Sensor ev3Sensor = new EV3Sensor(ev3Brick.EV3Brick_IP, portToTCPPort.get(sensorPort), EV3Brick.BRICK_TIMEOUT,sensorType,brick_port, mode);
				sensors.put(sensorPort, ev3Sensor);
				return ev3Sensor;
			}
		}else{
			return null;
		}
    }

	public void initializeSensor(Sensors sensorType,EV3SensorPort sensorPort) throws BrickIsNotReadyException {
		System.out.println("Local-EV3SensorManager: initSensor called");
		if(ev3Brick.isBrickReady()){
			if(sensorType != null && sensorPort != null){
				if(sensors.containsKey(sensorPort)){
					brickClient.sendTCP(BrickMessages.createSensor(sensorPort.getValue(), sensorType, portToTCPPort.get(sensorPort)));
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
		    	if(object.getClass() == BrickMessages.EndpointCreatedMessage.class){
					BrickMessages.EndpointCreatedMessage ecmsg = (BrickMessages.EndpointCreatedMessage)object;
					
					if(ecmsg.isSensor()){
						System.out.println("Local-EV3SensorManager: Received a EndpointCreatedMessage! -> "+ecmsg.toString());
						
						if(ecmsg.isSuccess()){
							System.out.println("Local-EV3SensorManager: isSuccess at port "+ecmsg.getPort()+"#");
							if(sensors.containsKey(EV3SensorPort.getPort(ecmsg.getPort()))){
								System.out.println("Local-EV3SensorManager: Endpoint creation successfull - connect to endpoint!");
								sensors.get(EV3SensorPort.getPort(ecmsg.getPort())).connect();
								sensors.get(EV3SensorPort.getPort(ecmsg.getPort())).initSensorMode();
							}else{
								System.out.println("Local-EV3SensorManager: Sensor does not exist");
							}
						}else{
							//TODO Tell Sensor/IMotor Manager that endpoint creation failed
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
		for(EV3SensorPort key:sensors.keySet()){
			if(sensors.get(key) != null){
				sensors.get(key).disconnect();
			}
		}
	}
    
    void close(EV3Sensor sensor) {
        /*List<Integer> tmpPorts = new ArrayList<>(4);
        for (Integer port : sensors.keySet()) {
            if (sensors.containsKey(port) && sensors.get(port).equals(sensor)) {
                tmpPorts.add(port);
            }
        }
        for (Integer port : tmpPorts) {
            sensors.remove(port);
        }*/
    }
    
	/** 
	 * Checks if a Port is a valid SensorPort of the EV3Brick
	 * 
	 * @param
	 * @return
	 *//*
	boolean isValidSensorPort(Port port){
    	if(port == SensorPort.S1 || port == SensorPort.S2 || port == SensorPort.S3 || port == SensorPort.S4){
    		return true;
    	}else{
    		return false;
    	}		
	}*/

	public void setBrickClient(Client brickClient) {
		this.brickClient = brickClient;
	}




}
