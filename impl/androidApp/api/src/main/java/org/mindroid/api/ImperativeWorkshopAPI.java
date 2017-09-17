package org.mindroid.api;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.statemachine.BooleanStatemachine;
import org.mindroid.impl.statemachine.DiscreteValueStateMachine;
import org.mindroid.impl.statemachine.constraints.GT;
import org.mindroid.impl.statemachine.constraints.LT;
import org.mindroid.impl.statemachine.constraints.MsgReceived;
import org.mindroid.impl.statemachine.constraints.Rotation;
import org.mindroid.impl.statemachine.properties.MessageProperty;
import org.mindroid.impl.statemachine.properties.sensorproperties.Angle;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

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
public abstract class ImperativeWorkshopAPI extends ImperativeAPI {
    //TODO Create some Interface to add a specific robot Configurateion: Check how to use it on app-site when creating the Robot using the RobotFactory.

    /**
     * @param implementationID - The ID of your Implementation. Necessary to run your implementation later on.
     */
    public ImperativeWorkshopAPI(String implementationID) {
        super(implementationID);
    }

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

    /**
     * Returns the color ID of the left color sensor.
     *
     * For supported color IDs, see {@link Color}.
     *
     * @return left color ID
     */
    public final float getLeftColor() {
        //TODO implement // Return Color
        return 0f;
    }

    /**
     * Returns the color ID of the right color sensor.
     *
     * For supported color IDs, see {@link Color}.
     *
     * @return right color ID
     */
    public final float getRightColor() {
        //TODO implement // Return Color
        return 0f;
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

    /**
     * The robot rotates counterclockwise by the given angle.
     * This method blocks until the rotation is completed.
     *
     * @param degrees the angle in degrees
     */
    public final void turnLeft(int degrees) {
        //TODO implement
    }


    /**
     * The robot rotates clockwise by the given angle. The method blocks until the rotation is completed.
     *
     * @param degrees angle
     */
    public final void turnRight(int degrees) {
        //TODO implement
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
    public void stopMotor(EV3PortID motorport) {
        //TODO Implement
    }

    /**
     * Starts driving forward and returns immediately
     * Use {@link #forward()} to stop driving.
     */
    public void forward() {
        //TODO Implement
    }

    /**
     * Starts driving backward and returns immediately
     * Use {@link #backward()} to stop driving.
     */
    public void backward() {
        //TODO Implement
    }


    // ------ Getter-Method Hardware Ports ------
    /**
     * Returns the {@link EV3PortID} of the left unregulated motor
     */
    protected EV3PortID getLeftMotor() {
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
