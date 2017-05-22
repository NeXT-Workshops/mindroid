package org.mindroid.impl.robot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.mindroid.api.ev3.EV3RotationMotorEnabled;
import org.mindroid.api.ev3.EV3StatusLightEnabled;
import org.mindroid.api.statemachine.NoStartStateException;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.motor.UnregulatedMotor;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.impl.configuration.RobotConfigurator;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.sensor.EV3Sensor;

import org.mindroid.common.messages.EV3MotorPort;
import org.mindroid.common.messages.EV3SensorPort;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages.SensorMode_;
import org.mindroid.common.messages.Sensors;

@Deprecated
public abstract class AbstractRobodancer implements EV3RotationMotorEnabled, EV3StatusLightEnabled {
	private RobotConfigurator config;
	
	ArrayList<EV3PortID> ultraSonicSensors = new ArrayList<EV3PortID>(1);
	
	ArrayList<EV3PortID> motorPorts = new ArrayList<EV3PortID>(2);
	
	private boolean isBlocked = false;
	
	public UnregulatedMotor motor_links,motor_rechts;
	public EV3Sensor ultrasonic_front;
	public EV3Sensor colorSensor_links,colorSensor_rechts;


	private HashMap<String,IStatemachine> statemachines = new HashMap<String,IStatemachine>();
	
	/**
	 * 
	 * @param BRICK_IP
	 * @param BRICK_TCP_PORT
	 */
	public AbstractRobodancer(String BRICK_IP, int BRICK_TCP_PORT){
		// Configurate Robot
		config = new RobotConfigurator(BRICK_IP,BRICK_TCP_PORT);

	}

	private void startStatemachines() throws NoStartStateException {
		for(String key : statemachines.keySet()){
			statemachines.get(key).start();
		}
	}

	private void stopStatemachines(){
		for(String key : statemachines.keySet()){
			statemachines.get(key).reset();
			statemachines.get(key).stop();
		}


	}

	/**
	 * Statemachines are implemented in this methods
	 */
	abstract public void initStatemachines() throws StateAlreadyExists;

	public void start() throws StateAlreadyExists, NoStartStateException {
		initStatemachines();
		startStatemachines();
	}

	public void stop(){
		stopStatemachines();
		stopMotors();
	}

	/**
	 * Starts the Configuration of the Robot.
	 * Connection to the Brick will be established.
	 * Motors and sensors will be created afterwards using the connection.
	 */
	public boolean startConfiguration() {

		if(config.getBrick().isConnected()) {

			config.addMotorConfigurationSet(Motors.UnregulatedMotor, null, null, Motors.UnregulatedMotor);
			config.addSensorConfigurationSet(Sensors.EV3ColorSensor,Sensors.EV3UltrasonicSensor, null, Sensors.EV3ColorSensor);

			ultraSonicSensors.add(getEV3PortID(EV3SensorPort.S1));

			motorPorts.add(getEV3PortID(EV3MotorPort.A));
			motorPorts.add(getEV3PortID(EV3MotorPort.D));

			try {
				config.initializeConfiguration();
			} catch (BrickIsNotReadyException e1) {
				e1.printStackTrace();
			}

			config.setSensorModeSet(SensorMode_.RED, SensorMode_.DISTANCE, null, SensorMode_.RED);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//TODO Intelligente Sensor zuweisung -> Ansonsten kann es passieren das ein Handler einem falschen Sensortyp zugeordnet wird
			motor_links = (UnregulatedMotor) config.getMotor(getEV3MotorPort(EV3PortIDs.PORT_A));
			motor_rechts = (UnregulatedMotor) config.getMotor(getEV3MotorPort(EV3PortIDs.PORT_D));

			colorSensor_links = config.getSensor(EV3SensorPort.S1);
			colorSensor_rechts = config.getSensor(EV3SensorPort.S4);
			ultrasonic_front = config.getSensor(EV3SensorPort.S2);

			// Create Statemachine
			//statemachines.put(new Statemachine("main"));
			//TODO add as listener to event handlers

			// Initialize SensorEventHandler
			initEventHandler();

			return true;
		}

		return false;
	}


	private void initEventHandler(){
		/*
		UltrasonicEventHandler ultrasonicEventHandler = new UltrasonicEventHandler(ultrasonic_front);
		ultrasonicEventHandler.addListener((IRobotEventListener) statemachines.get("main"));
		ultrasonic_front.registerListener(ultrasonicEventHandler);
	
		ColorsensorEventHandler colorEventHandler_left = new ColorsensorEventHandler(colorSensor_links);
		colorEventHandler_left.addListener((IRobotEventListener) statemachines.get("main"));
		colorSensor_links.registerListener(colorEventHandler_left);
		
		ColorsensorEventHandler colorEventHandler_right = new ColorsensorEventHandler(colorSensor_rechts);
		colorEventHandler_right.addListener((IRobotEventListener) statemachines.get("main"));
		colorSensor_rechts.registerListener(colorEventHandler_right);*/
	}


	public IStatemachine getStatemachine(String name){
		return statemachines.get(name);
	}


	@Override
	public Collection<EV3PortID> getMotorPorts() {
		return motorPorts;
	}

	@Override
	public void setMotorSpeed(int motorSpeed) {
		//motor_links.setPower(motorSpeed);
		//motor_rechts.setPower(motorSpeed);
		
		/*
		for(EV3PortID port: getMotorPorts()){
			if(config.getMotor(getEV3MotorPort(port)) instanceof UnregulatedMotor){
				((UnregulatedMotor)config.getMotor(getEV3MotorPort(port))).setPower(motorSpeed);
			}
		}*/
	}

	@Override
	public void setMotorSpeed(int motorSpeed, EV3PortID port) {
		if(config.getMotor(getEV3MotorPort(port)) instanceof UnregulatedMotor){
			////((UnregulatedMotor) config.getMotor(getEV3MotorPort(port))).setPower(motorSpeed);
		}
	}

	@Override
	public void stopMotor(EV3PortID port) {
		if(config.getMotor(getEV3MotorPort(port)) instanceof UnregulatedMotor){
			((UnregulatedMotor) config.getMotor(getEV3MotorPort(port))).stop();
		}		
	}

	@Override
	public void stopMotors() {
		for(EV3PortID port: getMotorPorts()){
			if(config.getMotor(getEV3MotorPort(port)) instanceof UnregulatedMotor){
				((UnregulatedMotor)config.getMotor(getEV3MotorPort(port))).stop();
			}
		}	
	}

	
	@Override
	public void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval) {
		config.getBrick().setEV3StatusLight(color, interval);		
	}

	@Override
	public void resetEV3StatusLight() {
		config.getBrick().resetEV3StatusLight();
	}
	
	private EV3PortID getEV3PortID(EV3SensorPort port){
		switch(port){
			case S1: return EV3PortIDs.PORT_1;
			case S2: return EV3PortIDs.PORT_2;
			case S3: return EV3PortIDs.PORT_3;
			case S4: return EV3PortIDs.PORT_4;
			default: return null;
		}
	}
	
	private EV3PortID getEV3PortID(EV3MotorPort port){
		switch(port){
			case A: return EV3PortIDs.PORT_A;
			case B: return EV3PortIDs.PORT_B;
			case C: return EV3PortIDs.PORT_C;
			case D: return EV3PortIDs.PORT_D;
			default: return null;
		}
	}
 
	private EV3SensorPort getEV3SensorPort(EV3PortID port){
		if(port.getNumber() == EV3PortIDs.PORT_1.getNumber())
			return EV3SensorPort.S1;
		
		if(port.getNumber() == EV3PortIDs.PORT_2.getNumber())
			return EV3SensorPort.S2;
		
		if(port.getNumber() == EV3PortIDs.PORT_3.getNumber())
			return EV3SensorPort.S3;
		
		if(port.getNumber() == EV3PortIDs.PORT_4.getNumber())
			return EV3SensorPort.S4;
		
		return null;
	}
	
	private EV3MotorPort getEV3MotorPort(EV3PortID port){
		if(port.getNumber() == EV3PortIDs.PORT_A.getNumber())
			return EV3MotorPort.A;
		
		if(port.getNumber() == EV3PortIDs.PORT_B.getNumber())
			return EV3MotorPort.B;
		
		if(port.getNumber() == EV3PortIDs.PORT_C.getNumber())
			return EV3MotorPort.C;
		
		if(port.getNumber() == EV3PortIDs.PORT_D.getNumber())
			return EV3MotorPort.D;
		
		return null;
	}
	
	public void blockActions(){
		isBlocked = true;
	}

	public RobotConfigurator getRobotconfiguration() {
		return config;
	}

	public void setRobotconfiguration(RobotConfigurator config) {
		this.config = config;
	}
}
