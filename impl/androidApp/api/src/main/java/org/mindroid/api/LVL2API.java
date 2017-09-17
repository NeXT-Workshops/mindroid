package org.mindroid.api;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.statemachine.*;
import org.mindroid.impl.statemachine.constraints.GT;
import org.mindroid.impl.statemachine.constraints.LT;
import org.mindroid.impl.statemachine.constraints.MsgReceived;
import org.mindroid.impl.statemachine.constraints.Rotation;
import org.mindroid.impl.statemachine.properties.MessageProperty;
import org.mindroid.impl.statemachine.properties.sensorproperties.Angle;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

import java.util.HashMap;

/*
 * TODO@revise: Required improvements
 * * EV3 motors should be synchronized (When calling "backward()", one motor starts slightly prior to the other!)
 *   Could we create a new subtype of "IMotor"?
 * * Make speed during turnLeft/turnRight configuraable
 * * Consistency: There is forward(); delay(...); but turnRight() and turnRight(time);
 * * There are numerous comments that suggest that this class is not ready, yet.
 * * A lot of code duplication regarding "missing state machine creation"
 * * Revise all "System.err.println"s -> often, a RuntimeException would be better to signal an error!
 * * We definitely need access to (i) received messages (if any), (ii) current (Red) light value,...
 * * Make DiscreteStateMachine generic ("DiscreteValueStateMachine<T>"). This will also entail that EQ/... provides a generic interface
 * * Check all @Deprecated methods/classes/...
 * * Reduce usage of nested classes (esp. for *Messages).
 * * Check for unused classes
 * * Update docu to EN
 * * Javadoc should generate without warning (in all projects!)
 * * Class Color should be an Enum (providing access to float value + description)
 * * Avoid varargs and arrays in public API, rather use List<>, Collection<> or Iterable<>
 */

/**
 * This is a superclass for all robot implementations that use the imperative paradigm.
 *
 * @author Torben Unzicker - Initial implementation, 03.05.2017
 * @author Roland Kluge - Refactoring and documentation, Aug 2017
 */
public abstract class LVL2API extends StatemachineAPI {

    private static final String IMPERATIVE_STATEMACHINE_ID = "Imperative Statemachine Implementation";
    private static final String IMPERATIVE_GROUP_ID = "Mindrobot";
    private static final String SM_KEY_COLLISION_DETECTED = "collisionDetection";
    private static final String SM_KEY_ANGLE = "angleSM";
    private static final String SM_KEY_LEFT_COLOR = "colorLeftSM";
    private static final String SM_KEY_RIGHT_COLOR = "colorRightSM";
    private static final String SM_KEY_PREFIX_DISTANCE_GREATER_THAN = "distanceGreaterThan";
    private static final String SM_KEY_PREFIX_DISTANCE_LESS_THAN = "distanceLessThan";
    private static final float[] COLOR_VALUES = {Color.NONE, Color.BLACK, Color.BLUE, Color.BROWN, Color.GREEN, Color.RED, Color.WHITE, Color.YELLOW};

    private final Motor leftMotor;
    private final Motor rightMotor;
    /**
     * Maps a state machine identifier to the registered state machine
     * See also: all constants with prefix 'SM_'.
     */
    private HashMap<String, Statemachine> sensorEvaluatingStatemachines = new HashMap<>();
    private ImperativeStatemachine imperativeStatemachine;


    public LVL2API() {
        leftMotor = motorProvider.getMotor(getLeftMotorPort());
        rightMotor = motorProvider.getMotor(getRightMotorPort());

        initSensorStatemachines();
        statemachineCollection.addParallelStatemachines(IMPERATIVE_GROUP_ID, sensorEvaluatingStatemachines.values().toArray(new Statemachine[sensorEvaluatingStatemachines.values().size()]));
        imperativeStatemachine = (ImperativeStatemachine) initStatemachine();
        statemachineCollection.addParallelStatemachines(IMPERATIVE_GROUP_ID, imperativeStatemachine);
    }

    /**
     * The logic behind this robot.
     */
    public abstract void run();

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
     * Evaluates whether the state machine has been stopped
     *
     * @return true if the imperative state machine got interrupted (by stopping it)
     */
    public final boolean isInterrupted() {
        return imperativeStatemachine.isInterrupted();
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
     * Evaluates whether a collision is imminent
     * This method is equivalent to invoking {@link #distanceLessThan(float)} with the threshold {@link #getCollisionDetectionThresholdInMeters()}
     *
     * @return true if an collision has been detected
     */
    public final boolean isCollisionDetected() {
        //TODO@revise: Use the following structure to simplify the check-and-create pattern for sensor state machines
        if (!hasStateMachine(SM_KEY_COLLISION_DETECTED)) {
            initializeCollisionDetectionStateMachine();
        }
        return ((BooleanStatemachine) sensorEvaluatingStatemachines.get(SM_KEY_COLLISION_DETECTED)).getResult();
    }

    /**
     * Evaluates whether the measured distance is below the given threshold
     *
     * @param threshold the threshold in meters
     * @return true if the measured distance is smaller than threshold
     */
    public final boolean distanceLessThan(float threshold) {
        if (sensorEvaluatingStatemachines.containsKey(SM_KEY_PREFIX_DISTANCE_LESS_THAN + threshold) && sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_LESS_THAN + threshold) instanceof BooleanStatemachine) {
            return ((BooleanStatemachine) sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_LESS_THAN + threshold)).getResult();
        } else {
            BooleanStatemachine sm = new BooleanStatemachine(SM_KEY_PREFIX_DISTANCE_LESS_THAN + threshold, false, new LT(threshold, new Distance(getUltrasonicSensorPort())), new GT(threshold, new Distance(getUltrasonicSensorPort())));
            sensorEvaluatingStatemachines.put(SM_KEY_PREFIX_DISTANCE_LESS_THAN + threshold, sm);
            registerStatemachine(sm);
            startStatemachine(SM_KEY_PREFIX_DISTANCE_LESS_THAN + threshold);
            return sm.getResult();
        }

        //TODO maybe remove or stop statemachines when they are not needed anymore
    }

    /**
     * Evaluates whether the measured distance is above the given threshold
     *
     * @param threshold the threshold in meters
     * @return true if the measured distance is larger than threshold
     */
    public final boolean distanceGreaterThan(float threshold) {
        if (sensorEvaluatingStatemachines.containsKey(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + threshold) && sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + threshold) instanceof BooleanStatemachine) {
            return ((BooleanStatemachine) sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + threshold)).getResult();
        } else {
            BooleanStatemachine sm = new BooleanStatemachine(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + threshold, true, new GT(threshold, new Distance(getUltrasonicSensorPort())), new LT(threshold, new Distance(getUltrasonicSensorPort())));
            sensorEvaluatingStatemachines.put(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + threshold, sm);
            registerStatemachine(sm);
            startStatemachine(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + threshold);
            return sm.getResult();
        }

        //TODO maybe remove or stop statemachines when they are not needed anymore
    }

    /** //TODO check port config before creating SM
     * //TODO method does not work yet
     *
     **/
    /*public final boolean wasTouched() {
        if (sensorEvaluatingStatemachines.containsKey("touched") && sensorEvaluatingStatemachines.get("touched") instanceof DiscreteValueStateMachine) {
            return (((DiscreteValueStateMachine) sensorEvaluatingStatemachines.get("touched")).getResult() == Touch.HIT);
        } else {
            float[] touchValues = {Touch.NO_HIT, Touch.HIT};
            DiscreteValueStateMachine touchedSM = new DiscreteValueStateMachine("touched",  new Touch(EV3PortIDs.PORT_3), touchValues);
            sensorEvaluatingStatemachines.put("touched", touchedSM);
            registerStatemachine(touchedSM);
            startStatemachine("touched");
            return (touchedSM.getResult() == Touch.HIT);
        }
    }*/

    /**
     * Checks if a specified message was received
     *
     * @param message expected message
     * @param source  expected source of the message
     * @return true - if message received , otherwise false
     */
    public final boolean wasMsgReceived(String message, String source) {
        //TODO needs to be tested
        final String stateMachineKey = "Msg" + message + source;
        if (sensorEvaluatingStatemachines.containsKey(stateMachineKey) && sensorEvaluatingStatemachines.get(stateMachineKey) instanceof BooleanStatemachine) {
            //Statemachine has already been started.
            boolean result = ((BooleanStatemachine) sensorEvaluatingStatemachines.get(stateMachineKey)).getResult();
            if (!result) {
                //SM will keep running as the message was not received yet.
                return false;
            } else {
                //SM is stopped and removed.
                stopStatemachine(stateMachineKey);
                sensorEvaluatingStatemachines.remove(stateMachineKey);
                return true;
            }
        } else {
            //create new SM
            BooleanStatemachine sm = new BooleanStatemachine(stateMachineKey, false, new MsgReceived(new MessageProperty(message, source)), null);
            sensorEvaluatingStatemachines.put(stateMachineKey, sm);
            registerStatemachine(sm);
            startStatemachine(stateMachineKey);
            boolean result = sm.getResult();

            if (!result) {
                //SM will keep running as the message was not received yet.
                return false;

            } else {
                //SM is stopped and removed.
                stopStatemachine(stateMachineKey);
                sensorEvaluatingStatemachines.remove(stateMachineKey);
                return true;
            }
        }

    }

    /**
     * Returns the color ID of the left color sensor.
     *
     * For supported color IDs, see {@link Color}.
     *
     * @return left color ID
     */
    public final float getLeftColor() {
        //TODO@revise: Is this check necessary? We actually know that this state machine is started during initStateMachines()
        if (!hasStateMachine(SM_KEY_LEFT_COLOR)) {
            initializeLeftColorStateMachine();
        }
        return ((DiscreteValueStateMachine) sensorEvaluatingStatemachines.get(SM_KEY_LEFT_COLOR)).getResult();
    }

    /**
     * Returns the color ID of the right color sensor.
     *
     * For supported color IDs, see {@link Color}.
     *
     * @return right color ID
     */
    public final float getRightColor() {
        //TODO@revise: Is this check necessary? We actually know that this state machine is started during initStateMachines()
        if (!hasStateMachine(SM_KEY_RIGHT_COLOR)) {
            initializeRightColorStateMachine();
        }
        return ((DiscreteValueStateMachine) sensorEvaluatingStatemachines.get(SM_KEY_RIGHT_COLOR)).getResult();
    }

    @Deprecated // Too specialized //TODO@revise
    public final boolean isColorBlack() {
        return (getLeftColor() == Color.BLACK);
    }

    @Deprecated // Too specialized //TODO@revise
    public final boolean isColor(float color) {
        return (getLeftColor() == color);
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
        if (!isInterrupted()) {
            BooleanStatemachine angleSM = new BooleanStatemachine(SM_KEY_ANGLE, false, new Rotation((-1) * degrees, new Angle(getGyroSensorPort())), null);
            registerStatemachine(angleSM);
            startStatemachine(angleSM.getID());
            leftMotor.backward();
            rightMotor.forward();
            leftMotor.setSpeed(getMotorSpeedDuringRotation());
            rightMotor.setSpeed(getMotorSpeedDuringRotation());
            while (!angleSM.getResult() && !isInterrupted()) {
                delay(getTurnAngleCheckIntervalInMilliseconds());
            }
            stopStatemachine(angleSM.getID());
            leftMotor.stop();
            rightMotor.stop();
        }
    }


    /**
     * The robot rotates clockwise by the given angle. The method blocks until the rotation is completed.
     *
     * @param degrees angle
     */
    public final void turnRight(int degrees) {
        if (!isInterrupted()) {
            BooleanStatemachine angleSM = new BooleanStatemachine(SM_KEY_ANGLE, false, new Rotation(degrees, new Angle(getGyroSensorPort())), null);
            registerStatemachine(angleSM);
            startStatemachine(SM_KEY_ANGLE);
            leftMotor.forward();
            rightMotor.backward();
            leftMotor.setSpeed(getMotorSpeedDuringRotation());
            rightMotor.setSpeed(getMotorSpeedDuringRotation());
            while (!angleSM.getResult() && !isInterrupted()) {
                delay(getTurnAngleCheckIntervalInMilliseconds());
            }
            stopStatemachine(SM_KEY_ANGLE);
            leftMotor.stop();
            rightMotor.stop();
        }
    }

    /**
     * The robot rotates counterclockwise for the specified time. The method blocks until the rotation is completed.
     *
     * @param milliseconds time in milliseconds
     */
    public final void turnLeftTime(int milliseconds) {
        if (!isInterrupted()) {
            leftMotor.backward();
            rightMotor.forward();
            leftMotor.setSpeed(getMotorSpeedDuringRotation());
            rightMotor.setSpeed(getMotorSpeedDuringRotation());
            delay(milliseconds);
            leftMotor.stop();
            rightMotor.stop();
        }
    }


    /**
     * The robot rotates clockwise for the specified time. The method blocks until the rotation is completed.
     *
     * @param milliseconds time in milliseconds
     */
    public final void turnRightTime(int milliseconds) {
        if (!isInterrupted()) {
            leftMotor.forward();
            rightMotor.backward();
            leftMotor.setSpeed(getMotorSpeedDuringRotation());
            rightMotor.setSpeed(getMotorSpeedDuringRotation());
            delay(milliseconds);
            leftMotor.stop();
            rightMotor.stop();
        }
    }

    /**
     * Stops all motors
     */
    public void stopMotors() {
        leftMotor.stop();
        rightMotor.stop();
    }

    /**
     * Starts driving forward and returns immediately
     * Use {@link #stopMotors()} to stop driving.
     */
    @Override
    public void forward() {
        if (!isInterrupted()) {
            super.forward();
        }
    }

    /**
     * Starts driving backward and returns immediately
     * Use {@link #stopMotors()} to stop driving.
     */
    @Override
    public void backward() {
        if (!isInterrupted()) {
            super.backward();
        }
    }

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

    /**
     * Returns the motor speed that is used for turn methods
     *
     * @see Motor#setSpeed(int)
     */
    protected int getMotorSpeedDuringRotation() {
        return 50;
    }

    /**
     * Returns the distance below which a collision is detected
     *
     * @return the distance in meters
     */
    protected float getCollisionDetectionThresholdInMeters() {
        return 0.15f;
    }

    /**
     * Returns the interval between consecutive checks whether the robot has reached the end of a turn
     *
     * @return the check interval in milliseconds
     * @see #turnLeft(int)
     * @see #turnRight(int)
     */
    protected int getTurnAngleCheckIntervalInMilliseconds() {
        return 100;
    }

    /**
     * Register a Statemachine.
     *
     * @param sm
     */
    private void registerStatemachine(IStatemachine sm) {
        StatemachineCollection sc = new StatemachineCollection();
        sc.addStatemachine(sm.getID(), sm);
        StatemachineManager.getInstance().addStatemachines(sc);
    }

    /**
     * Starting a Statemachine/group of Statemachines with the id.
     *
     * @param id
     */
    private void startStatemachine(String id) {
        StatemachineManager.getInstance().startStatemachines(id);
    }

    /**
     * Stopping a statemachine
     * Note: All motors will be stopped.
     *
     * @param id
     */
    private void stopStatemachine(String id) {
        StatemachineManager.getInstance().stopStatemachines(id);
    }

    private void initSensorStatemachines() {
        sensorEvaluatingStatemachines.clear();
        initializeCollisionDetectionStateMachine();
        initializeLeftColorStateMachine();
        initializeRightColorStateMachine();
    }

    /**
     * Returns true if a state machine is registered with the given key
     */
    private boolean hasStateMachine(final String smKey) {
        return this.sensorEvaluatingStatemachines.containsKey(smKey);
    }

    /**
     * Initializes the state machine for detecting collisions based on the ultrasonic sensor
     */
    private void initializeCollisionDetectionStateMachine() {
        BooleanStatemachine collisionDetectionStateMachine = new BooleanStatemachine(SM_KEY_COLLISION_DETECTED,
                false,
                new LT(getCollisionDetectionThresholdInMeters(), new Distance(getUltrasonicSensorPort())),
                new GT(getCollisionDetectionThresholdInMeters(), new Distance(getUltrasonicSensorPort())));
        sensorEvaluatingStatemachines.put(SM_KEY_COLLISION_DETECTED, collisionDetectionStateMachine);
        registerStatemachine(collisionDetectionStateMachine);
        startStatemachine(SM_KEY_COLLISION_DETECTED);
    }

    /**
     * Initializes the state machine for the left light sensor in color mode
     */
    private void initializeLeftColorStateMachine() {
        final DiscreteValueStateMachine leftColorStateMachine = new DiscreteValueStateMachine(SM_KEY_LEFT_COLOR, new Color(getLeftColorSensorPort()), COLOR_VALUES);
        sensorEvaluatingStatemachines.put(SM_KEY_LEFT_COLOR, leftColorStateMachine);
        statemachineCollection.addParallelStatemachines(IMPERATIVE_GROUP_ID, leftColorStateMachine);
    }

    /**
     * Initializes the state machine for the left light sensor in color mode
     */
    private void initializeRightColorStateMachine() {
        final DiscreteValueStateMachine leftColorStateMachine = new DiscreteValueStateMachine(SM_KEY_RIGHT_COLOR, new Color(getRightColorSensorPort()), COLOR_VALUES);
        sensorEvaluatingStatemachines.put(SM_KEY_RIGHT_COLOR, leftColorStateMachine);
        statemachineCollection.addParallelStatemachines(IMPERATIVE_GROUP_ID, leftColorStateMachine);
    }

    /**
     * Creates the imperative state machine.
     * This state machine has only one state, which is invoking {@link #run()}
     *
     * @return the configured state machine
     */
    private final IStatemachine initStatemachine() {
        final ImperativeStatemachine sm = new ImperativeStatemachine(IMPERATIVE_STATEMACHINE_ID);

        final IState state_start = new State("Running state") {
            @Override
            public void run() {
                LVL2API.this.run();
            }
        };

        sm.addState(state_start);
        sm.setStartState(state_start);

        return sm;
    }
}
