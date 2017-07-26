package org.mindroid.impl.configuration;

import java.util.HashMap;


import org.mindroid.api.configuration.IRobotConfigurator;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.motor.IMotor;
import org.mindroid.impl.brick.EV3Brick;
import org.mindroid.impl.endpoint.ClientEndpointImpl;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.motor.EV3MotorManager;
import org.mindroid.impl.sensor.EV3Sensor;
import org.mindroid.impl.sensor.EV3SensorManager;

import org.mindroid.common.messages.EV3MotorPort;
import org.mindroid.common.messages.EV3SensorPort;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages.SensorMode_;
import org.mindroid.common.messages.Sensors;

/**
 *
 * Configurates the Robot in the mean of which hardware is added to which port.
 *
 */
public class RobotConfigurator implements IRobotConfigurator {

	private HashMap<EV3SensorPort,Sensors> sensorConfiguration = new HashMap<EV3SensorPort,Sensors>(4);
	private HashMap<EV3SensorPort,SensorMode_> sensorModeConfiguration = new HashMap<EV3SensorPort,SensorMode_>(4);
	private HashMap<EV3MotorPort,Motors> motorConfiguration = new HashMap<EV3MotorPort,Motors>(4);
	
	private HashMap<EV3SensorPort,EV3Sensor> sensors = new HashMap<EV3SensorPort,EV3Sensor>(4);
	private HashMap<EV3MotorPort,IMotor> motors = new HashMap<EV3MotorPort,IMotor>(4);
	
	/** Experimental Values - may need a change **/
	private final int DURATION_DEVICE_INITIALIZATION = 5000;
	private final int DURATION_TO_CONNECT_TO_BRICK = 3000;
	private final int DURATION_READY_GREENLIGHT = 4000;
	
	private EV3SensorManager sensorManager;
	private EV3MotorManager motorManager;
	
	private String robotIP;
	private int robotTCPPort;
	
	private EV3Brick brick;

	final static String DELIMITER = ":";

	/** Capsulate Information about the connection state (connected/disconnected) to the Endpoints (Motors and Sensors on the Brick) **/
	private StringBuffer endpointState;
	/** String that capsulate information about the current configuration (Sensor-Port;IMotor-Port)**/
	private StringBuffer str_config;
	/**
	 *
	 * @param robotIP
	 * @param robotTCPPort
     */
	public RobotConfigurator(String robotIP, int robotTCPPort){
		this.robotIP = robotIP;
		this.robotTCPPort = robotTCPPort;
		this.endpointState = new StringBuffer();
		this.str_config = new StringBuffer();

		//TODO Refactor this part. Remove dependency somehow
		brick = new EV3Brick(robotIP,robotTCPPort);
		sensorManager = brick.getSensorManager();
		motorManager = brick.getMotorManager();
	}

	/**
	 * Disconnectes the Kryo-Connection of the open devices
	 */
	public void disconnectDevices(){

	}
	
	@Override
	public void addSensorConfigurationSet(Sensors sensor_S1,Sensors sensor_S2, Sensors sensor_S3,Sensors sensor_S4){
		addSensorType(EV3SensorPort.S1,sensor_S1);
		addSensorType(EV3SensorPort.S2,sensor_S2);
		addSensorType(EV3SensorPort.S3,sensor_S3);
		addSensorType(EV3SensorPort.S4,sensor_S4);
	}
	
	@Override
	public void addMotorConfigurationSet(Motors motor_A,Motors motor_B, Motors motor_C,Motors motor_D){
		addMotorType(EV3MotorPort.A,motor_A);
		addMotorType(EV3MotorPort.B,motor_B);
		addMotorType(EV3MotorPort.C,motor_C);
		addMotorType(EV3MotorPort.D,motor_D);
	}

	@Override
	public String getConfiguration() {

		str_config.delete(0,str_config.length());


		for(EV3SensorPort key : sensorConfiguration.keySet()){
			str_config.append(key.getValue()).append(DELIMITER).append(sensorConfiguration.get(key).name()).append("\n");
		}

		for(EV3MotorPort key : motorConfiguration.keySet()){
			str_config.append(key.getValue()).append(DELIMITER).append(motorConfiguration.get(key).name()).append("\n");
		}

		return str_config.toString().trim();
	}

	@Override
	public String getEndpointConnectionState(){
		endpointState.delete(0,endpointState.length());

		for(EV3SensorPort key : sensorConfiguration.keySet()){
			endpointState.append(key.getValue()).append(DELIMITER).append(sensors.get(key).client.isConnected());
			endpointState.append("\n");
		}

		for(EV3MotorPort key : motorConfiguration.keySet()){
			if(motors.get(key) instanceof ClientEndpointImpl){
				endpointState.append(key.getValue()).append(DELIMITER).append( ((ClientEndpointImpl)motors.get(key)).client.isConnected()).append("\n");
			}
		}

		//System.out.println(endpointState.toString()+"\n------------");
		return endpointState.toString();
	}


	@Override
	public void addSensorType(EV3SensorPort sensorPort, Sensors sensor){
		if(sensorPort != null && sensor != null){
			sensorConfiguration.put(sensorPort, sensor);
		}
	}
	
	@Override
	public void addMotorType(EV3MotorPort motorPort, Motors motor){
		if(motorPort != null && motor != null){
			motorConfiguration.put(motorPort, motor);
		}
	}
		
	@Override
	public Sensors getSensorType(EV3SensorPort sensorPort) {
		return sensorConfiguration.get(sensorPort);
	}

	@Override
	public Motors getMotorType(EV3MotorPort motorPort) {
		return motorConfiguration.get(motorPort);
	}

	@Override
	public EV3Sensor createSensor(EV3PortID sensPort) throws PortIsAlreadyInUseException {
		EV3SensorPort sensorPort = null;
		if(EV3PortIDs.PORT_1 == sensPort){
			sensorPort = EV3SensorPort.S1;
		}else if(EV3PortIDs.PORT_2 == sensPort){
			sensorPort = EV3SensorPort.S2;
		}else if(EV3PortIDs.PORT_3 == sensPort){
			sensorPort = EV3SensorPort.S3;
		}else if(EV3PortIDs.PORT_4 == sensPort){
			sensorPort = EV3SensorPort.S4;
		}
		if(sensorPort != null && !sensors.containsKey(sensorPort)){
			EV3Sensor sensor = sensorManager.createSensor(sensPort,getSensorType(sensorPort),sensorPort,sensorModeConfiguration.get(sensorPort));
			sensors.put(sensorPort, sensor);
			return sensor;
		}else{
			return null;
		}
	}

	@Override
	public EV3Sensor getSensor(EV3SensorPort sensorPort) {
		return sensors.get(sensorPort);
	}

	@Override
	public IMotor createMotor(EV3PortID motorPort) throws PortIsAlreadyInUseException {
		EV3MotorPort motPort = null;
		if(EV3PortIDs.PORT_A == motorPort){
			motPort = EV3MotorPort.A;
		}else if(EV3PortIDs.PORT_B == motorPort){
			motPort = EV3MotorPort.B;
		}else if(EV3PortIDs.PORT_C == motorPort){
			motPort = EV3MotorPort.C;
		}else if(EV3PortIDs.PORT_D == motorPort){
			motPort = EV3MotorPort.D;
		}
		if(motPort != null && !motors.containsKey(motPort)){
			IMotor IMotor = motorManager.createMotor(getMotorType(motPort), motPort);
			motors.put(motPort, IMotor);
			return IMotor;
		}else{
			return null;
		}
	}

	@Override
	public IMotor getMotor(EV3MotorPort motorPort) {
		return motors.get(motorPort);
	}

	// TODO MAY CAN BE REMOVED COMPLETELY, REPLACED BY createMotor; createSensor
	/** Talks to the Brick - tell him what motor/sensor is connected to its port **/
	@Override
	public boolean initializeConfiguration() throws BrickIsNotReadyException {
		if(brick.isConnected()) {
			//sensorManager = brick.getSensorManager();
			//motorManager = brick.getMotorManager();

			for (EV3SensorPort senPort : sensorConfiguration.keySet()) {
				Sensors type = getSensorType(senPort);
				if (type != null) {
					if(!sensors.containsKey(senPort)) {
						//sensors.put(senPort, sensorManager.createSensor(type, senPort));
						//TODO Throw Exception: Sensor that should be there wasn't created!
					}
					sensorManager.initializeSensor(type,senPort);
				}
			}

			for (EV3MotorPort motorPort : motorConfiguration.keySet()) {
				Motors type = getMotorType(motorPort);
				if (type != null) {
					if(!motors.containsKey(motorPort)){
						//motors.put(motorPort, motorManager.createMotor(type, motorPort));
						//TODO Throw Exception: Sensor that should be there wasn't created!
					}
					motorManager.initializeMotor(type,motorPort);
				}
			}

			boolean init_complete = false;
			while(!init_complete){

				init_complete = checkEndpointConnectionState(getEndpointConnectionState());

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			Runnable readyLight = new Runnable(){
				@Override
				public void run(){
				brick.setEV3StatusLight(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);
				pause(DURATION_READY_GREENLIGHT);
				brick.resetEV3StatusLight();
				}
			};
			new Thread(readyLight).start();

			return true;
		}
		return false;
	}

	/**
	 *
	 * @param endpntState - contains information about all endpoint connection states (connected/disconnected)
	 * @return true - if all endpoints are connected
	 * 		   false - if not all endpoints are connected to the brick
     */
	private boolean checkEndpointConnectionState(String endpntState){
		if(endpntState.contains("false") /*|| endpntState.contains("null")*/){
			return false;
		}else{
			return true;
		}
	}

	private void pause(long duraton_in_millis){
		try {
			Thread.sleep(duraton_in_millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	public EV3Brick getBrick() {
		return brick;
	}



	@Override
	public void setSensorMode(EV3SensorPort port, SensorMode_ mode) {
		if(sensors.containsKey(port)){
			sensors.get(port).changeSensorToMode(mode);
		}
	}



	@Override
	public void setSensorModeSet(SensorMode_ mode_S1, SensorMode_ mode_S2, SensorMode_ mode_S3, SensorMode_ mode_S4) {
			sensorModeConfiguration.put(EV3SensorPort.S1,mode_S1);
			sensorModeConfiguration.put(EV3SensorPort.S2,mode_S2);
			sensorModeConfiguration.put(EV3SensorPort.S3,mode_S3);
			sensorModeConfiguration.put(EV3SensorPort.S4,mode_S4);
	}



}
