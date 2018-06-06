package org.mindroid.api.configuration;

import org.mindroid.api.motor.IRegulatedMotor;
import org.mindroid.common.messages.hardware.EV3MotorPort;
import org.mindroid.common.messages.hardware.EV3SensorPort;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.motor.EV3RegulatedMotorEndpoint;
import org.mindroid.impl.motor.SynchronizedMotorsEndpoint;
import org.mindroid.impl.sensor.EV3SensorEndpoint;


public interface IRobotConfigurator {

	/**
	 * Returns the Configuration of the Robot
	 *
	 * 	Format is: 'Port':'Sensor-/Motortype'
	 * 'Port' = [A-D] || [S1-S4]
	 * @return returns information about configuration state as a string
     */
	String getConfiguration();

	/**
	 * Returns the ConnectionState of the Sensor/IMotor Endpoints.
	 * Format is: 'Port':true/false
	 * 'Port' = [A-D] || [S1-S4]
	 *
	 * @return returns information about the connection state as a string
     */
	String getEndpointConnectionState();


	// ------------- Sensor Creation
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

	/**
	 * Creates the Endpoint for the sensor
	 * @param sensorPort the port the sensor is plugged in
	 * @return the Endpoint of the sensor
	 * @throws BrickIsNotReadyException - if brick is not connected or ready
	 * @throws PortIsAlreadyInUseException - if the port is already in use
	 */
	EV3SensorEndpoint createSensor(EV3PortID sensorPort) throws BrickIsNotReadyException, PortIsAlreadyInUseException;

	/**
	 * Add a Sensor Type to the RobotConfigurator.
	 *
	 * @param sensorPort - port the sensor is connected to
	 * @param sensor - Type of the Sensor
	 */
	void addSensorType(EV3SensorPort sensorPort, Sensors sensor);

	void addSensorConfigurationSet(Sensors sensor_S1, Sensors sensor_S2, Sensors sensor_S3, Sensors sensor_S4);

	//------------ Motor Creation

	/**
	 * Creates a Motor Endpoint
	 * @param motorPort - port the motor is connected to
	 * @return EV3RegulatedMotorEndpoint
	 * @throws BrickIsNotReadyException - if brick is not connected or ready
	 * @throws PortIsAlreadyInUseException - if the port is already in use
	 */
	EV3RegulatedMotorEndpoint createMotor(EV3PortID motorPort) throws BrickIsNotReadyException, PortIsAlreadyInUseException;


	Motors getMotorType(EV3MotorPort motorPort);

	/**
	 * Add a motorType to the RobotConfigurator.
	 *
	 * @param motorPort - port of the motor it is connected to the brick
	 * @param motor - motor type
	 */
	void addMotorType(EV3MotorPort motorPort, Motors motor);

	void addMotorConfigurationSet(Motors motor_A, Motors motor_B, Motors motor_C, Motors motor_D);


	//------------ Synchronized Motors Creation

	/**
	 * Creates the Endpoint for the Synchronized Motor Group
	 * @return SynchronizedMotorEndpoint
	 */
	SynchronizedMotorsEndpoint createSynchronizedMotorsEndpoint();

	//------------- Build the wholoe Configuration

	/**
	 * Build the Configuration
	 * @return true if configuration initialization is successful else false
	 * @throws BrickIsNotReadyException - if the brick is not connected or ready to receive initialization information
	 * @throws PortIsAlreadyInUseException - if a port already has a sensor/motor specified
	 */
	boolean initializeConfiguration() throws BrickIsNotReadyException, PortIsAlreadyInUseException;

	/**
	 * Interrupts the configuration process
	 */
    void interruptConfigurationProcess();
}
