package org.mindroid.api.configuration;

import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.common.messages.hardware.EV3MotorPort;
import org.mindroid.common.messages.hardware.EV3SensorPort;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.sensor.EV3SensorEndpoint;


public interface IRobotConfigurator {

	void addSensorConfigurationSet(Sensors sensor_S1, Sensors sensor_S2, Sensors sensor_S3, Sensors sensor_S4);
	
	void addMotorConfigurationSet(Motors motor_A, Motors motor_B, Motors motor_C, Motors motor_D);

	/**
	 * Returns the Configuration of the Robot
	 *
	 * 	Format is: 'Port':'Sensor-/Motortype'
	 * 'Port' = [A-D] || [S1-S4]
	 * * @return
     */
	String getConfiguration();

	/**
	 * Returns the ConnectionState of the Sensor/IMotor Endpoints.
	 * Format is: 'Port':true/false
	 * 'Port' = [A-D] || [S1-S4]
	 *
	 * @return
     */
	String getEndpointConnectionState();

	void setSensorMode(EV3SensorPort port, Sensormode mode);
	
	/**
	 * 
	 * @param mode_S1 - set mode for sensor at port 1
	 * @param mode_S2 - set mode for sensor at port 2
	 * @param mode_S3 - set mode for sensor at port 3
	 * @param mode_S4 - set mode for sensor at port 4
	 */
	void setSensorModeSet(Sensormode mode_S1, Sensormode mode_S2, Sensormode mode_S3, Sensormode mode_S4);
	
	Sensors getSensorType(EV3SensorPort sensorPort);
	
	Motors getMotorType(EV3MotorPort motorPort);

	EV3SensorEndpoint createSensor(EV3PortID sensorPort) throws BrickIsNotReadyException, PortIsAlreadyInUseException;

	EV3SensorEndpoint getSensor(EV3SensorPort sensorPort);

	RegulatedMotor createMotor(EV3PortID motorPort) throws BrickIsNotReadyException, PortIsAlreadyInUseException;

	RegulatedMotor getMotor(EV3MotorPort motorPort);


	/**
	 * Add a Sensor Type to the RobotConfigurator.
	 * 
	 * @param sensorPort
	 * @param sensor
	 */
	void addSensorType(EV3SensorPort sensorPort, Sensors sensor);
	
	/**
	 * Add a motorType to the RobotConfigurator.
	 * 
	 * @param motorPort
	 * @param motor
	 */
	void addMotorType(EV3MotorPort motorPort, Motors motor);
	
	/**
	 * Build the Configuration
	 */
	boolean initializeConfiguration() throws BrickIsNotReadyException, PortIsAlreadyInUseException;


}
