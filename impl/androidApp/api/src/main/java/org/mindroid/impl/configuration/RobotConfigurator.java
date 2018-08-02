package org.mindroid.impl.configuration;

import java.sql.Time;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.mindroid.api.configuration.IRobotConfigurator;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.motor.IRegulatedMotor;
import org.mindroid.common.messages.hardware.EV3MotorPort;
import org.mindroid.common.messages.hardware.EV3SensorPort;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.brick.EV3Brick;
import org.mindroid.impl.brick.EV3BrickEndpoint;
import org.mindroid.impl.endpoint.ClientEndpointImpl;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.motor.EV3MotorManager;
import org.mindroid.impl.motor.EV3RegulatedMotorEndpoint;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.motor.SynchronizedMotorsEndpoint;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.sensor.EV3SensorEndpoint;
import org.mindroid.impl.sensor.EV3SensorManager;
import org.mindroid.impl.sensor.Sensor;


/**
 *
 * Configurates the Robot in the mean of which hardware is plugged in which port.
 *
 */
public class RobotConfigurator implements IRobotConfigurator {

	//TIMEOUT AFTER 45 Sec
	public static final int INITIALIZATION_TIMEOUT =45000;
	private boolean isInitializationTimedOut = false;

	private HashMap<EV3SensorPort,Sensors> sensorConfiguration = new HashMap<EV3SensorPort,Sensors>(4);
	private HashMap<EV3SensorPort,Sensormode> sensorModeConfiguration = new HashMap<EV3SensorPort,Sensormode>(4);
	private HashMap<EV3MotorPort,Motors> motorConfiguration = new HashMap<EV3MotorPort,Motors>(4);
	
	private HashMap<EV3SensorPort,EV3SensorEndpoint> sensors = new HashMap<EV3SensorPort,EV3SensorEndpoint>(4);
	private HashMap<EV3MotorPort,EV3RegulatedMotorEndpoint> motors = new HashMap<EV3MotorPort,EV3RegulatedMotorEndpoint>(4);
	
	/** Experimental Values - may need a change **/
	private final int DURATION_READY_GREENLIGHT = 4000;
	
	private EV3SensorManager sensorManager;
	private EV3MotorManager motorManager;

	private EV3Brick brick;

	final static String DELIMITER = ":";
	final static String CREATION_FAILED = "ERROR";

	/** Capsulate Information about the connection state (connected/disconnected) to the Endpoints (Motors and Sensors on the Brick) **/
	private StringBuffer endpointState;
	/** String that capsulate information about the current configuration (Sensor-Port;IMotor-Port)**/
	private StringBuffer str_config;
	private EV3PortID[] syncedMotorPorts;
	private SynchronizedMotorsEndpoint syncedMotorsEndpoint;

	/** If this var is true, the configurationprocess should be interrupted
	 *
	 **/
	private boolean isConfigurationInterrupted = false;

	private static final Logger LOGGER = Logger.getLogger(RobotConfigurator.class.getName());

	static{
		APILoggerManager.getInstance().registerLogger(LOGGER);
	}

	/**
	 *
	 * @param robotIP - brick IP
	 * @param robotTCPPort - brick TCP port to connect to
     */
	public RobotConfigurator(String robotIP, int robotTCPPort){
		this.endpointState = new StringBuffer();
		this.str_config = new StringBuffer();

		//TODO Refactor this part. Remove dependency somehow
		brick = new EV3Brick(new EV3BrickEndpoint(robotIP,robotTCPPort));
		sensorManager = brick.getSensorManager();
		motorManager = brick.getMotorManager();
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
			endpointState.append(key.getValue()).append(DELIMITER).append(sensors.get(key).client.isConnected() && sensors.get(key).isFirstSensEventReceived());
			endpointState.append("\n");

			if(sensors.get(key).hasCreationFailed()){
				endpointState.append(DELIMITER).append(CREATION_FAILED);

				//With the error string added the initialization process will stop and has to be started again by the user. Therefore stopAllMotors the value for the next iteration
				(sensors.get(key)).setHasCreationFailed(false);
			}
		}

		for(EV3MotorPort key : motorConfiguration.keySet()){
			if(motors.get(key) instanceof ClientEndpointImpl){
				endpointState.append(key.getValue()).append(DELIMITER).append( ((ClientEndpointImpl)motors.get(key)).client.isConnected()).append("\n");
				if(((EV3RegulatedMotorEndpoint)motors.get(key)).hasCreationFailed()){
					endpointState.append(DELIMITER).append(CREATION_FAILED);

					//With the error string added the initialization process will stop and has to be started again by the user. Therefore stopAllMotors the value for the next iteration
					((EV3RegulatedMotorEndpoint)motors.get(key)).setHasCreationFailed(false);
				}
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
	public void addMotorType(EV3MotorPort motorPort, Motors motortype){
		if(motorPort != null && motortype != null){
			motorConfiguration.put(motorPort, motortype);
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
	public EV3SensorEndpoint createSensor(EV3PortID sensPort) throws PortIsAlreadyInUseException {
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
			EV3SensorEndpoint sensor = sensorManager.createSensor(sensPort,getSensorType(sensorPort),sensorPort,sensorModeConfiguration.get(sensorPort));
			sensors.put(sensorPort, sensor);
			return sensor;
		}else{
			return null;
		}
	}

	@Override
	public EV3RegulatedMotorEndpoint createMotor(EV3PortID motorPort) throws PortIsAlreadyInUseException {
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
			EV3RegulatedMotorEndpoint motor = motorManager.createMotor(getMotorType(motPort), motPort);
			motors.put(motPort, motor);
			return motor;
		}else{
			return null;
		}
	}

	@Override
	public SynchronizedMotorsEndpoint createSynchronizedMotorsEndpoint() {
		this.syncedMotorsEndpoint = motorManager.createSynchronizedMotorsEndpoint();
		return syncedMotorsEndpoint;
	}



	/**
	 * Talks to the Brick - tells how the motor and sensor port configuration looks like.
	 * The brick will then start to initialize its motors and sensors and also creates proper endpoints to connect those with the phone.
	 * Waits until the sensor and motor endpoints are coneected to the brick and the sensors received their first SensorEvent-Msg.
	 *
	 * If an error during the initialization process on the brick occurs. All sensor and motor endpoints
	 * on API-Side will be closed and removed and the process will be aborted and has to be restarted.
	 *
	 * Process times out after {@link #INITIALIZATION_TIMEOUT}.
	 *
	 * @return true - if initialization was successful
	 * @throws BrickIsNotReadyException - when the brick is not connected or ready to receive configuration information
	 */
	@Override
	public boolean initializeConfiguration() throws BrickIsNotReadyException {
		LOGGER.log(Level.INFO,"Start initializing the Configuration: "+getConfiguration());

		//Timeout for the initialization
		startTimeoutTimer();

		if(brick.isConnected()) {
			//Reset Interruption-state to not_interrupted
			setConfigurationInterrupted(false);

			for (EV3SensorPort senPort : sensorConfiguration.keySet()) {
				Sensors type = getSensorType(senPort);
				if (type != null && !isConfigurationInterrupted() && !isInitializationTimedOut) {
					sensorManager.initializeSensor(type, senPort);
				}
			}

			for (EV3MotorPort motorPort : motorConfiguration.keySet()) {
				Motors type = getMotorType(motorPort);
				if (type != null && !isConfigurationInterrupted() && !isInitializationTimedOut) {
					motorManager.initializeMotor(type,motorPort);
				}
			}

			// Wait until Sensor And motors are Created
			boolean init_complete = false;
			boolean hasCreationFailed = false;
			while(!init_complete){
				//Get endpoint state
				String endpointState = getEndpointConnectionState();

				//get bool-val whether a creation on brick has failed
				hasCreationFailed = hasEndpointCreationFailed(endpointState);

				//get bool-val whether the initialization is completed
				init_complete = getEndpointConnectionState(endpointState);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if(hasCreationFailed || isConfigurationInterrupted() || isInitializationTimedOut){
					LOGGER.log(Level.WARNING,"Initialization of Sensor/Motors Failed: \r\n hasCreationFailed="+ hasCreationFailed+"\r\n isInitComplete="+init_complete+" \n\r endpointState= " +endpointState);
					resetConfigurationInitialization();
					if(isInitializationTimedOut ){
						isInitializationTimedOut = false;
						logTimeout();
					}
					return false;
				}
			}

			if(isInitializationTimedOut){
				resetConfigurationInitialization();
				isInitializationTimedOut = false;
				logTimeout();
			}else {
				//Initialize Synchronized Motor Group
				motorManager.initializeSyncedMotorGroup();

				//Wait until Synced Motor group is created
				while (!this.syncedMotorsEndpoint.isClientReady()) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Runnable readyLight = new Runnable() {
					@Override
					public void run() {
						brick.setEV3StatusLight(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);
						pause(DURATION_READY_GREENLIGHT);
						brick.setLEDOff();
					}
				};
				new Thread(readyLight).start();
			}
			//Validate
			validateRobot();

			return true;
		}
		return false;
	}

	private void logTimeout() {
		String timeoutLog = ("TimeoutException incomming: The Initialization Process should timed out after "+INITIALIZATION_TIMEOUT+"ms");
		LOGGER.log(Level.WARNING,timeoutLog);
	}


	/**
	 * Call if the initialization process got interrupted to reset the init state.
	 */
	private void resetConfigurationInitialization() {
		//Abort initialization, because the creation of a sensor/motor on the brick failed or the process got interrupted
		motorManager.disconnectMotors();
		sensorManager.disconnectSensors();
		//Remove all initialized sensor endpoints, as they should be recreated, when trying to retry init-process
		sensors.clear();
		motors.clear();

		//Reset interruption state to not_interrupted
		setConfigurationInterrupted(false);
	}

	/**
	 * Starts the Initialization timeout timer.
	 * Timeout is set by {@link #INITIALIZATION_TIMEOUT}
	 *
	 */
	private void startTimeoutTimer() {
		TimerTask timeoutTrigger = new TimerTask(){
			@Override
			public void run() {
				isInitializationTimedOut = true;
			}
		};
		Timer timeoutTimer = new Timer();
		timeoutTimer.schedule(timeoutTrigger,INITIALIZATION_TIMEOUT);

	}

	@Override
	public void interruptConfigurationProcess() {
		setConfigurationInterrupted(true);
	}

	/**
	 * Validate the Sensors and motors.
	 *
	 * Checks if SensorProvider is correctly initialized
	 * Checks if the MotorProvider is correctly initialized.
	 *
	 * The idea is to protect the API from null-pointer exceptions, if sensors or motors in the provider-classes are null or in a unexpected mode.
	 */
	private void validateRobot() {
		boolean validateionResult = validateSensor(EV3PortIDs.PORT_1);
		validateionResult &= validateSensor(EV3PortIDs.PORT_2);
		validateionResult &= validateSensor(EV3PortIDs.PORT_3);
		validateionResult &= validateSensor(EV3PortIDs.PORT_4);

		validateionResult &= validateMotor(EV3PortIDs.PORT_A);
		validateionResult &= validateMotor(EV3PortIDs.PORT_B);
		validateionResult &= validateMotor(EV3PortIDs.PORT_C);
		validateionResult &= validateMotor(EV3PortIDs.PORT_D);

		if (!validateionResult) {
			ErrorHandlerManager.getInstance().handleError(new IllegalStateException("Robot validation failed!"), this.getClass(), "The validation of the created Robot failed. Try again!");
		}
	}

	private boolean validateMotor(EV3PortID port){
		Motor motor = Robot.getRobotController().getMotorProvider().getMotor(port);
		Motors expectedMotor = getExpectedMotor(port);
		if(motor == null && expectedMotor == null){
			//motor is null, but expected
			return true;
		}

		if(motor == null){
			//motor is null, but unexpected
			return false;
		}
		//motor is initialized as expected
		return true;
	}

	private Motors getExpectedMotor(EV3PortID port){
		if(port.getNumber() == EV3PortIDs.PORT_A.getNumber()){
			return motorConfiguration.get(EV3MotorPort.A);
		}else if(port.getNumber() == EV3PortIDs.PORT_B.getNumber()){
			return motorConfiguration.get(EV3MotorPort.B);
		}else if(port.getNumber() == EV3PortIDs.PORT_C.getNumber()){
			return motorConfiguration.get(EV3MotorPort.C);
		}else if(port.getNumber() == EV3PortIDs.PORT_D.getNumber()){
			return motorConfiguration.get(EV3MotorPort.D);
		}
		return null;
	}

	private boolean validateSensor(EV3PortID port){
		Sensor sensor = Robot.getRobotController().getSensorProvider().getSensor(port);
		Sensors expectedSensor = getExpectedSensor(port);
		Sensormode expectedMode = getExptectedMode(port);
		if(sensor == null && expectedSensor == null){
			//sensor is null as expected
			return true;
		}

		if(sensor == null){
			//sensor is null, but shouldn't be
			return false;
		}

		if(sensor.getSensorType().equals(expectedSensor) && sensor.getSensormode().equals(expectedMode)){
			//sensor is initialized an is running the expected mode
			return true;
		}else{
			//sensor is initialized, but is not running the expected mode is is not the expected sensortype
			return false;
		}
	}

	private Sensors getExpectedSensor(EV3PortID port){
		if(port.getNumber() == EV3PortIDs.PORT_1.getNumber()){
			return sensorConfiguration.get(EV3SensorPort.S1);
		}else if(port.getNumber() == EV3PortIDs.PORT_2.getNumber()){
			return sensorConfiguration.get(EV3SensorPort.S2);
		}else if(port.getNumber() == EV3PortIDs.PORT_3.getNumber()){
			return sensorConfiguration.get(EV3SensorPort.S3);
		}else if(port.getNumber() == EV3PortIDs.PORT_4.getNumber()){
			return sensorConfiguration.get(EV3SensorPort.S4);
		}
		return null;
	}

	private Sensormode getExptectedMode(EV3PortID port){
		if(port.getNumber() == EV3PortIDs.PORT_1.getNumber()){
			return sensorModeConfiguration.get(EV3SensorPort.S1);
		}else if(port.getNumber() == EV3PortIDs.PORT_2.getNumber()){
			return sensorModeConfiguration.get(EV3SensorPort.S2);
		}else if(port.getNumber() == EV3PortIDs.PORT_3.getNumber()){
			return sensorModeConfiguration.get(EV3SensorPort.S3);
		}else if(port.getNumber() == EV3PortIDs.PORT_4.getNumber()){
			return sensorModeConfiguration.get(EV3SensorPort.S4);
		}
		return null;
	}


	/**
	 *
	 * @param endpntState - contains information about all endpoint connection states (connected/disconnected) and if sensors have received their first sensor-event
	 * @return true - if all endpoints are connected
	 * 		   false - if not all endpoints are connected to the brick
     */
	private boolean getEndpointConnectionState(String endpntState){
		if(endpntState.contains("false") /*|| endpntState.contains("null")*/){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Checks if an creation on brick site error is reported.
	 * Returns true if an error has occured. false otherwise.
	 * @param endpntState call getEndpointState
	 * @return true - if creation failed.
	 */
	private boolean hasEndpointCreationFailed(String endpntState){
		if(endpntState.contains(CREATION_FAILED)){
			return true;
		}else{
			return false;
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
	public void setSensorMode(EV3SensorPort port, Sensormode mode) {
		if(sensors.containsKey(port)){
			sensors.get(port).changeSensorToMode(mode);
		}
	}



	@Override
	public void setSensorModeSet(Sensormode mode_S1, Sensormode mode_S2, Sensormode mode_S3, Sensormode mode_S4) {
			sensorModeConfiguration.put(EV3SensorPort.S1,mode_S1);
			sensorModeConfiguration.put(EV3SensorPort.S2,mode_S2);
			sensorModeConfiguration.put(EV3SensorPort.S3,mode_S3);
			sensorModeConfiguration.put(EV3SensorPort.S4,mode_S4);
	}


	private void setConfigurationInterrupted(boolean configurationInterrupted) {
		isConfigurationInterrupted = configurationInterrupted;
	}

	public boolean isConfigurationInterrupted() {
		return isConfigurationInterrupted;
	}
}
