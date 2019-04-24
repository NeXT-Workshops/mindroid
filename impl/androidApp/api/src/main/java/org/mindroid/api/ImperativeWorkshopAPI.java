package org.mindroid.api;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.robot.DifferentialPilot;
import org.mindroid.impl.sensor.Sensor;
import org.mindroid.impl.statemachine.properties.Colors;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This API is build on the Imperative API.
 *
 * It is used for the Mindroid Workshop of the TU-Darmstadt Real-Time Systems Lab and is used with
 * a specific robot-Setup.
 *
 * It provides Methods to control the robot properly.
 *
 * @author Torben Unzicker 17.09.17
 */
public abstract class ImperativeWorkshopAPI extends ImperativeAPI{
    //TODO: May add a constraint to specific RobotConfiguration.

    /**
     * The Differential pilot is used to execute synchronized motor operations and precise robot drive control.
     */
    private final DifferentialPilot diffPilot;

    /**
     * Used with unblocked drive methods, so they will be executed at least for given amount of time (ms)
     */
    private final int MIN_DRIVE_DELAY = 50;

    private static final Logger LOGGER = Logger.getLogger(ImperativeWorkshopAPI.class.getName());

    static {
        APILoggerManager.getInstance().registerLogger(LOGGER);
    }

    /**
     * @param implementationID  The ID of your Implementation. Necessary to run your implementation later on.
     */
    public ImperativeWorkshopAPI(String implementationID, int sessionRobotCount) {
        super(implementationID, sessionRobotCount);
        //This DiffPilot supports angle correction while turning
        this.diffPilot = new DifferentialPilot(this, getMotorProvider(), getLeftMotorPort(),getRightMotorPort(),getSensorProvider(),getGyroSensorPort(),5.6f,12.5f);
    }

    public ImperativeWorkshopAPI(String implementationID) {
        super(implementationID); // without session
        //This DiffPilot supports angle correction while turning
        this.diffPilot = new DifferentialPilot(this, getMotorProvider(), getLeftMotorPort(),getRightMotorPort(),getSensorProvider(),getGyroSensorPort(),5.6f,12.5f);
    }

    /**
     * This method waits until the given amount of time has passed.
     * This method is blocking.
     *
     * @param milliseconds the time in milliseconds
     */
    public final void delay(long milliseconds) {
        if (!isInterrupted()) {
            try {
                Thread.sleep(milliseconds);
            } catch (final InterruptedException e) {
                // Ignore
            }
        }
    }

    // --------------------- SENSOR EVALUATING METHODS: Display, LED ---------------------


    /**
     * Returns the color ID of the left color sensor.
     *
     * For supported color IDs, see {@link org.mindroid.impl.statemachine.properties.Colors}.
     *
     * @return left color ID
     */
    public final Colors getLeftColor() {
        if(getLeftColorSensor().getSensormode().equals(Sensormode.COLOR_ID)){
            return Colors.getColorByValue(getLeftColorSensor().getValue()[0]);
        }else{
            return Colors.NONE;
        }
    }

    /**
     * Returns the color ID of the right color sensor.
     *
     * For supported color IDs, see {@link Color}.
     *
     * @return right color ID
     */
    public Colors getRightColor() {
        if(getRightColorSensor().getSensormode().equals(Sensormode.COLOR_ID)){
            return Colors.getColorByValue(getRightColorSensor().getValue()[0]);
        }else{
            return Colors.NONE;
        }
    }

    /**
     * Returns the Distance measured by the Distance Sensor in centimeter
     *
     * @return distance value in centimeter
     */
    public float getDistance(){
        if(getUltrasonicSensor().getSensormode().equals(Sensormode.DISTANCE)){
            return getUltrasonicSensor().getValue()[0]*100;
        }else{
            return -1f;
        }

    }

    /**
     * Returns the current Angle of the robot given by the Gyrosensor.
     * @return angle in degree
     */
    public float getAngle(){
        return getGyroSensor().getValue()[0];
    }

    //TODO May add further method to change sensormode.
    //TODO Add getter method for other possible sensormodes.

    // ------ Motor Controlling Methods ------


    /**
     * Rotate counter-clockwise until stop() is called
     */
    public final void turnLeft(){
        if(!isInterrupted){
            diffPilot.turnLeft();
        }
    }

    /**
     * The robot rotates counterclockwise by the given angle.
     * This method blocks until the rotation is completed.
     * Returns without action if system got interrupted.
     *
     * @param degrees the angle in degrees
     */
    public final void turnLeft(int degrees) {
        if(!isInterrupted()) {
            diffPilot.turnLeft(degrees);
        }
    }

    /**
     * The robot rotates counterclockwise by the given angle.
     * This method blocks until the rotation is completed.
     * Returns without action if system got interrupted.
     *
     * @param degrees degrees to turn
     * @param speed speed of the motors [0 to 1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    public final void turnLeft(int degrees,int speed) {
        if(!isInterrupted()) {
            diffPilot.turnLeft(degrees,speed);
        }
    }

    /**
     * Rotate clockwise until stop() is called
     */
    public final void turnRight(){
        if(!isInterrupted){
            diffPilot.turnRight();
        }
    }

    /**
     * The robot rotates clockwise by the given angle. The method blocks until the rotation is completed.
     * Returns without action if system got interrupted.
     * @param degrees angle
     */
    public final void turnRight(int degrees) {
        if(!isInterrupted()) {
            diffPilot.turnRight(degrees);
        }
    }

    /**
     * The robot rotates clockwise by the given angle. The method blocks until the rotation is completed.
     * Returns without action if system got interrupted.
     *
     * @param degrees  degrees to turn
     * @param speed speed of the motors [0 to 1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    public final void turnRight(int degrees,int speed) {
        if(!isInterrupted()) {
            diffPilot.turnRight(degrees,speed);
        }
    }

    /**
     * Stops the Motor at the given Port.
     * @param motorport  port of the motor to stop
     */
    public void stop(EV3PortID motorport) {
        getMotorProvider().getMotor(motorport).stop();
    }

    /**
     * Stops the left and right motor of the robot.
     * Synchronized and blocking operation.
     *
     */
    public void stop(){
        diffPilot.stop();
    }

    /**
     * Sets the left and right motor into float mode.
     * Synchronized and blocking operation.
     * Returns without action if system got interrupted.
     *
     */
    public void enableFloatMode(){
        if(!isInterrupted()) {
            diffPilot.flt();
        }
    }

    /**
     * Starts driving driveDistanceForward and returns immediately.
     * Returns without action if system got interrupted.
     *
     * Use {@link #stop()} to stop driving.
     * @param speed  speed of the motors [0 to 1000] deg/sec. Possible MaxSpeed depends on battery power!
     *
     *
     * Note: Executed for at least {@link #MIN_DRIVE_DELAY}
     */
    public void forward(int speed) {
        if(!isInterrupted()) {
            diffPilot.driveForward(speed);
            delay(MIN_DRIVE_DELAY);
        }
    }

    /**
     * Drives forward.
     * The current speed will be used.
     *
     * Note: Executed for at least {@link #MIN_DRIVE_DELAY}
     */
    public void forward(){
        if(!isInterrupted()){
            diffPilot.driveForward();
            delay(MIN_DRIVE_DELAY);
        }
    }

    /**
     * Drives forward.
     * Synchronized motor operation and blocking method.
     * Returns without action if system got interrupted.
     *
     * @param distance distance to drive in cm
     */
    public void driveDistanceForward(float distance) {
        if(!isInterrupted()) {
            diffPilot.driveDistanceForward(distance);
        }
    }

    /**
     * Drives forward.
     * Synchronized motor operation and blocking method.
     * Returns without action if system got interrupted.
     *
     * @param distance distance to drive in cm
     * @param speed speed of the motors [0 to 1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    public void driveDistanceForward(float distance,int speed) {
        if(!isInterrupted()) {
            diffPilot.driveDistanceForward(distance,speed);
        }
    }

    /**
     * Starts driving backward and returns immediately.
     * Returns without action if system got interrupted.
     * Use {@link #stop()} to stop driving.
     * @param speed speed of the motors [0 to 1000] deg/sec. Possible MaxSpeed depends on battery power!
     *
     * Note: Executed for at least {@link #MIN_DRIVE_DELAY}
     */
    public void backward(int speed) {
        if(!isInterrupted()) {
            diffPilot.driveBackward(speed);
            delay(MIN_DRIVE_DELAY);
        }
    }

    /**
     * Drives Backward.
     * The current speed will be used.
     *
     * Note: Executed for at least {@link #MIN_DRIVE_DELAY}
     */
    public void backward(){
        if(!isInterrupted()){
            diffPilot.driveBackward();
            delay(MIN_DRIVE_DELAY);
        }
    }

    /**
     * Drives backward.
     * Synchronized motor operation and blocking method.
     * Returns without action if system got interrupted.
     *
     * @param distance distance to drive in cm
     */
    public void driveDistanceBackward(float distance) {
        if(!isInterrupted()) {
            diffPilot.driveDistanceBackward(distance);
        }
    }

    /**
     * Drives backward.
     * Synchronized motor operation and blocking method.
     * Returns without action if system got interrupted.
     *
     * @param distance distance to drive in cm
     * @param speed speed of the motors [0 to 1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    public void driveDistanceBackward(float distance,int speed) {
        if(!isInterrupted()) {
            diffPilot.driveDistanceBackward(distance,speed);
        }
    }

    /**
     * Sets the motorspeed of the left and right motors
     *
     * @param speed speed of the motors [0 to 1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    public void setMotorSpeed(int speed){
        if(!isInterrupted()){
            diffPilot.setMotorSpeed(speed);
        }
    }

    // ------ Getter Methods Sensors ------
    protected Sensor getLeftColorSensor(){
        return getSensorProvider().getSensor(getLeftColorSensorPort());
    }

    protected Sensor getRightColorSensor(){
        return getSensorProvider().getSensor(getRightColorSensorPort());
    }

    protected Sensor getUltrasonicSensor(){
        return getSensorProvider().getSensor(getUltrasonicSensorPort());
    }

    protected Sensor getGyroSensor(){
        return getSensorProvider().getSensor(getGyroSensorPort());
    }

    // ------ Getter-Method Hardware Ports ------
    /**
     * Returns the {@link EV3PortID} of the left unregulated motor
     * @return port of the left motor
     */
    protected EV3PortID getLeftMotorPort() {
        return EV3PortIDs.PORT_A;
    }

    /**
     * Returns the {@link EV3PortID} of the right unregulated motor
     * @return  port of the right motor
     */
    protected EV3PortID getRightMotorPort() {
        return EV3PortIDs.PORT_D;
    }

    /**
     * Returns the {@link EV3PortID} of the left color sensor
     * @return port of the left color sensor
     */
    protected EV3PortID getLeftColorSensorPort() {
        return EV3PortIDs.PORT_1;
    }

    /**
     * Returns the {@link EV3PortID} of the ultrasonic sensor
     * @return port of the ultrasonic sensor
     */
    protected EV3PortID getUltrasonicSensorPort() {
        return EV3PortIDs.PORT_2;
    }

    /**
     * Returns the port to which the gyro sensor is connected
     *
     * @return the gyro sensor port
     */
    protected EV3PortID getGyroSensorPort() {
        return EV3PortIDs.PORT_3;
    }

    /**
     * Returns the {@link EV3PortID} of the left color sensor
     * @return right color sensor port
     */
    protected EV3PortID getRightColorSensorPort() {
        return EV3PortIDs.PORT_4;
    }

}
