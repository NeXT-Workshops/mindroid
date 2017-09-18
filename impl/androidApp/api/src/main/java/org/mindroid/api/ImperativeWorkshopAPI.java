package org.mindroid.api;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.IDifferentialPilot;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.DifferentialPilot;
import org.mindroid.impl.sensor.Sensor;
import org.mindroid.impl.statemachine.properties.Colors;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;

/**
 * This API is build on the Imperative API.
 *
 * It is used for the Mindroid Workshop of the TU-Darmstadt Real-Time Systems Lab and is used with
 * a specific Robot-Setup.
 *
 * It provides Methods to control the Robot properly.
 *
 * @author Torben Unzicker 17.09.17
 */
public abstract class ImperativeWorkshopAPI extends ImperativeAPI implements IDifferentialPilot {
    //TODO Create some Interface to add a specific robot Configurateion: Check how to use it on app-site when creating the Robot using the RobotFactory.
    //TODO Maybe introduce an selfmade DifferentialPilot for precision controlling of the robot (Interface and Class)
    //TODO Methods for Messageing

    private DifferentialPilot diffPilot;

    /**
     * @param implementationID - The ID of your Implementation. Necessary to run your implementation later on.
     */
    public ImperativeWorkshopAPI(String implementationID) {
        super(implementationID);
        this.diffPilot = new DifferentialPilot(motorProvider.getSynchronizedMotors(), getLeftMotorPort(),getRightMotorPort(),0.56f,12.5f);
    }



    // --------------------- BRICK CONTROLLING METHODS: Display, LED ---------------------


    /**
     * Sets the LED to the given color
     *
     * @param color    the {@link EV3StatusLightColor} to use
     * @param interval the blink interval in milliseconds
     */
    public final void setLED(EV3StatusLightColor color, EV3StatusLightInterval interval) {
        if (!isInterrupted()) {
            brickController.setEV3StatusLight(color, interval);
        }
    }

    /**
     * Displays the given text onto the EV3 display at the given position (xPosition, yPosition).
     *
     * The coordinate (0,0) is at the top-left corner of the display.
     *
     * @param text the text to display
     * @param xPosition the x position
     * @param yPosition the y position
     */
    public void drawString(final String text, final int xPosition, final int yPosition) {
        this.brickController.drawString(text, xPosition, yPosition);
    }

    /**
     * Removes everything from the EV3 display
     */
    public void clearDisplay() {
        this.brickController.clearDisplay();
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
            return getColor(getLeftColorSensor().getValue()[0]);
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
            return getColor(getRightColorSensor().getValue()[0]);
        }else{
            return Colors.NONE;
        }
    }

    /**
     * Returns the Color {@link Colors} fitting to the given value
     * @param value - float value meassured by the color sensor
     * @return the color fitting the given value
     */
    private Colors getColor(float value){
        if(getLeftColorSensor().getValue()[0] == Colors.BLACK.getValue()) {
            return Colors.BLACK;
        }else if(getLeftColorSensor().getValue()[0] == Colors.BLUE.getValue()) {
            return Colors.BLUE;
        }else if(getLeftColorSensor().getValue()[0] == Colors.GREEN.getValue()) {
            return Colors.GREEN;
        }else if(getLeftColorSensor().getValue()[0] == Colors.YELLOW.getValue()) {
            return Colors.YELLOW;
        }else if(getLeftColorSensor().getValue()[0] == Colors.RED.getValue()) {
            return Colors.RED;
        }else if(getLeftColorSensor().getValue()[0] == Colors.WHITE.getValue()) {
            return Colors.WHITE;
        }else if(getLeftColorSensor().getValue()[0] == Colors.BROWN.getValue()) {
            return Colors.BROWN;
        }else{
            return Colors.NONE;
        }
    }

    /**
     * Returns the Distance measured by the Distance Sensor
     *
     * @return distance value in meter
     */
    public float getDistance(){
        if(getUltrasonicSensor().getSensormode().equals(Sensormode.DISTANCE)){
            return getUltrasonicSensor().getValue()[0];
        }else{
            return -1f;
        }

    }



    /**
     * Returns the current Angle of the Robot given by the Gyrosensor.
     * @return angle in degree
     */
    public float getAngle(){
        return getGyroSensor().getValue()[0];
    }



    // ------ Motor Controlling Methods ------

    /**
     * The robot rotates counterclockwise by the given angle.
     * This method blocks until the rotation is completed.
     *
     * @param degrees the angle in degrees
     */
    @Override
    public final void turnLeft(int degrees) {
        diffPilot.turnLeft(degrees);
    }

    /**
     * The robot rotates clockwise by the given angle. The method blocks until the rotation is completed.
     *
     * @param degrees angle
     */
    @Override
    public final void turnRight(int degrees) {
        diffPilot.turnRight(degrees);
    }

    /**
     * The robot rotates counterclockwise for the specified time. The method blocks until the rotation is completed.
     *
     * @param milliseconds time in milliseconds
     */
    public final void turnLeftTime(int milliseconds) {
        //TODO Implement
    }

    /**
     * The robot rotates clockwise for the specified time. The method blocks until the rotation is completed.
     *
     * @param milliseconds time in milliseconds
     */
    public final void turnRightTime(int milliseconds) {
        //TODO Implement
    }

    /**
     * Stops all motors
     */
    public void stop(EV3PortID motorport) {
        //TODO Implement
    }

    /**
     * Starts driving forward and returns immediately
     * Use {@link #stop()} to stop driving.
     */
    public void forward() {
        //TODO Implement
    }

    @Override
    public void forward(float distance) {
        diffPilot.forward(distance);
    }

    /**
     * Starts driving backward and returns immediately
     * Use {@link #stop()} to stop driving.
     */
    public void backward() {
        //TODO Implement
    }

    @Override
    public void backward(float distance) {
        diffPilot.backward(distance);
    }


    // ------ Methods to add some code sugar ------


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



    // ------ Getter Methods Sensors ------
    protected Sensor getLeftColorSensor(){
        return sensorProvider.getSensor(getLeftColorSensorPort());
    }

    protected Sensor getRightColorSensor(){
        return sensorProvider.getSensor(getRightColorSensorPort());
    }

    protected Sensor getUltrasonicSensor(){
        return sensorProvider.getSensor(getUltrasonicSensorPort());
    }

    protected Sensor getGyroSensor(){
        return sensorProvider.getSensor(getGyroSensorPort());
    }

    // ------ Getter-Method Hardware Ports ------
    /**
     * Returns the {@link EV3PortID} of the left unregulated motor
     */
    protected EV3PortID getLeftMotorPort() {
        return EV3PortIDs.PORT_A;
    }

    /**
     * Returns the {@link EV3PortID} of the right unregulated motor
     */
    protected EV3PortID getRightMotorPort() {
        return EV3PortIDs.PORT_D;
    }

    /**
     * Returns the {@link EV3PortID} of the left color sensor
     */
    protected EV3PortID getLeftColorSensorPort() {
        return EV3PortIDs.PORT_1;
    }

    /**
     * Returns the {@link EV3PortID} of the ultrasonic sensor
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
     */
    protected EV3PortID getRightColorSensorPort() {
        return EV3PortIDs.PORT_4;
    }

}
