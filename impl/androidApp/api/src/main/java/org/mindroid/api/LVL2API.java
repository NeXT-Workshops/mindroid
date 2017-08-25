package org.mindroid.api;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.robot.MotorDirection;
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
 * * Make speed during turnLeft/turnRight configuraable
 * * Consistency: There is forward(); delay(...); but turnRight() and turnRight(time);
 * * There are numerous comments that suggest that this class is not ready, yet.
 * * A lot of code duplication regarding "missing state machine creation"
 * * To discuss: Remove all the !isInterrupted method invocations? Better leave it to the students?
 * * Revise all "System.err.println"s -> often, a RuntimeException would be better to signal an error!
 */

/**
 * Created by Torbe on 03.05.2017.
 */
public abstract class LVL2API extends LVL1API {

    private static final String IMPERATIVE_STATEMACHINE_ID = "Imperative Statemachine Implementation";
    private static final String IMPERATIVE_GROUP_ID = "Mindrobot";
    private static final String SM_KEY_COLLISION_DETECTED = "collisionDetection";
    private static final String SM_KEY_ANGLE = "angleSM";
    private static final String SM_KEY_COLOR = "colorSM";
    private static final String SM_KEY_PREFIX_DISTANCE_GREATER_THAN = "distanceGreaterThan";
    private static final String SM_KEY_PREFIX_DISTANCE_LESS_THAN = "distanceLessThan";

    private final Motor leftMotor;
    private final Motor rightMotor;
    /**
     * Maps a state machine identifier to the registered state machine
     * See also: all constants with prefix 'SM_'.
     */
    private HashMap<String, Statemachine> sensorEvaluatingStatemachines = new HashMap<>();
    private ImperativeStatemachine imperativeStatemachine;


    public LVL2API() {
        leftMotor = new Motor(motorController, getLeftMotorPort());
        rightMotor = new Motor(motorController, getRightMotorPort());

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
     * @return true if the imperative statemachine got interrupted (by stopping it)
     */
    public final boolean isInterrupted() {
        return imperativeStatemachine.isInterrupted();
    }

    public final boolean isCollisionDetected() {
        if (sensorEvaluatingStatemachines.containsKey(SM_KEY_COLLISION_DETECTED) && sensorEvaluatingStatemachines.get(SM_KEY_COLLISION_DETECTED) instanceof BooleanStatemachine) {
            return ((BooleanStatemachine) sensorEvaluatingStatemachines.get(SM_KEY_COLLISION_DETECTED)).getResult();
        } else {
            BooleanStatemachine collisionDetection = new BooleanStatemachine(SM_KEY_COLLISION_DETECTED, false, new LT(getCollisionDetectionThresholdInMeters(), new Distance(getUltrasonicSensorPort())), new GT(getCollisionDetectionThresholdInMeters(), new Distance(getUltrasonicSensorPort())));
            sensorEvaluatingStatemachines.put(SM_KEY_COLLISION_DETECTED, collisionDetection);
            registerStatemachine(collisionDetection);
            startStatemachine(SM_KEY_COLLISION_DETECTED);
            return ((BooleanStatemachine) sensorEvaluatingStatemachines.get(SM_KEY_COLLISION_DETECTED)).getResult();
        }
    }

    public final boolean distanceLessThan(float value) {
        if (sensorEvaluatingStatemachines.containsKey(SM_KEY_PREFIX_DISTANCE_LESS_THAN + value) && sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_LESS_THAN + value) instanceof BooleanStatemachine) {
            return ((BooleanStatemachine) sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_LESS_THAN + value)).getResult();
        } else {
            BooleanStatemachine sm = new BooleanStatemachine(SM_KEY_PREFIX_DISTANCE_LESS_THAN + value, false, new LT(value, new Distance(getUltrasonicSensorPort())), new GT(value, new Distance(getUltrasonicSensorPort())));
            sensorEvaluatingStatemachines.put(SM_KEY_PREFIX_DISTANCE_LESS_THAN + value, sm);
            registerStatemachine(sm);
            startStatemachine(SM_KEY_PREFIX_DISTANCE_LESS_THAN + value);
            return sm.getResult();
        }

        //TODO maybe remove or stop statemachines when they are not needed anymore
    }

    public final boolean distanceGreaterThan(float value) {
        if (sensorEvaluatingStatemachines.containsKey(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + value) && sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + value) instanceof BooleanStatemachine) {
            return ((BooleanStatemachine) sensorEvaluatingStatemachines.get(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + value)).getResult();
        } else {
            BooleanStatemachine sm = new BooleanStatemachine(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + value, true, new GT(value, new Distance(getUltrasonicSensorPort())), new LT(value, new Distance(getUltrasonicSensorPort())));
            sensorEvaluatingStatemachines.put(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + value, sm);
            registerStatemachine(sm);
            startStatemachine(SM_KEY_PREFIX_DISTANCE_GREATER_THAN + value);
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

    public final float getColor() {
        if (sensorEvaluatingStatemachines.containsKey(SM_KEY_COLOR) && sensorEvaluatingStatemachines.get(SM_KEY_COLOR) instanceof DiscreteValueStateMachine) {
            return ((DiscreteValueStateMachine) sensorEvaluatingStatemachines.get(SM_KEY_COLOR)).getResult();
        } else {
            float[] colorValues = {Color.NONE, Color.BLACK, Color.BLUE, Color.BROWN, Color.GREEN, Color.RED, Color.WHITE, Color.YELLOW};
            DiscreteValueStateMachine colorSM = new DiscreteValueStateMachine(SM_KEY_COLOR, new Color(getLeftColorSensorPort()), colorValues);
            sensorEvaluatingStatemachines.put(SM_KEY_COLOR, colorSM);
            statemachineCollection.addParallelStatemachines(IMPERATIVE_GROUP_ID, colorSM);
            return ((DiscreteValueStateMachine) sensorEvaluatingStatemachines.get(SM_KEY_COLOR)).getResult();
        }
    }

    @Deprecated // Too specialized //TODO@revise
    public final boolean isColorBlack() {
        return (getColor() == Color.BLACK);
    }

    @Deprecated // Too specialized //TODO@revise
    public final boolean isColor(float color) {
        return (getColor() == color);
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
            motorController.setMotorDirection(getLeftMotorPort(), MotorDirection.BACKWARD);
            motorController.setMotorDirection(getRightMotorPort(), MotorDirection.FORWARD);
            motorController.setMotorSpeed(getLeftMotorPort(), getMotorSpeedDuringRotation());
            motorController.setMotorSpeed(getRightMotorPort(), getMotorSpeedDuringRotation());
            while (!angleSM.getResult() && !isInterrupted()) {
                delay(getTurnAngleCheckIntervalInMilliseconds());
            }
            stopStatemachine(angleSM.getID());
            motorController.stop(getLeftMotorPort());
            motorController.stop(getRightMotorPort());
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
            motorController.setMotorDirection(getLeftMotorPort(), MotorDirection.FORWARD);
            motorController.setMotorDirection(getRightMotorPort(), MotorDirection.BACKWARD);
            motorController.setMotorSpeed(getLeftMotorPort(), getMotorSpeedDuringRotation());
            motorController.setMotorSpeed(getRightMotorPort(), getMotorSpeedDuringRotation());
            while (!angleSM.getResult() && !isInterrupted()) {
                delay(getTurnAngleCheckIntervalInMilliseconds());
            }
            stopStatemachine(SM_KEY_ANGLE);
            motorController.stop(getLeftMotorPort());
            motorController.stop(getRightMotorPort());
        }
    }

    /**
     * The robot rotates counterclockwise for the specified time. The method blocks until the rotation is completed.
     *
     * @param milliseconds time in milliseconds
     */
    public final void turnLeftTime(int milliseconds) {
        if (!isInterrupted()) {
            motorController.setMotorDirection(getLeftMotorPort(), MotorDirection.BACKWARD);
            motorController.setMotorDirection(getRightMotorPort(), MotorDirection.FORWARD);
            motorController.setMotorSpeed(getLeftMotorPort(), getMotorSpeedDuringRotation());
            motorController.setMotorSpeed(getRightMotorPort(), getMotorSpeedDuringRotation());
            delay(milliseconds);
            motorController.stop(getLeftMotorPort());
            motorController.stop(getRightMotorPort());
        }
    }


    /**
     * The robot rotates clockwise for the specified time. The method blocks until the rotation is completed.
     *
     * @param milliseconds time in milliseconds
     */
    public final void turnRightTime(int milliseconds) {
        if (!isInterrupted()) {
            motorController.setMotorDirection(getLeftMotorPort(), MotorDirection.FORWARD);
            motorController.setMotorDirection(getRightMotorPort(), MotorDirection.BACKWARD);
            motorController.setMotorSpeed(getLeftMotorPort(), getMotorSpeedDuringRotation());
            motorController.setMotorSpeed(getRightMotorPort(), getMotorSpeedDuringRotation());
            delay(milliseconds);
            motorController.stop(getLeftMotorPort());
            motorController.stop(getRightMotorPort());
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
     * @see org.mindroid.impl.robot.MotorController#setMotorSpeed(EV3PortID, int)
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
        BooleanStatemachine collisionDetection = new BooleanStatemachine(SM_KEY_COLLISION_DETECTED,
                false,
                new LT(getCollisionDetectionThresholdInMeters(), new Distance(getUltrasonicSensorPort())),
                new GT(getCollisionDetectionThresholdInMeters(), new Distance(getUltrasonicSensorPort())));
        sensorEvaluatingStatemachines.put(SM_KEY_COLLISION_DETECTED, collisionDetection);

        float[] colorValues = {Color.NONE, Color.BLACK, Color.BLUE, Color.BROWN, Color.GREEN, Color.RED, Color.WHITE, Color.YELLOW};
        DiscreteValueStateMachine colorSM = new DiscreteValueStateMachine(SM_KEY_COLOR, new Color(getLeftColorSensorPort()), colorValues);
        sensorEvaluatingStatemachines.put(SM_KEY_COLOR, colorSM);

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
