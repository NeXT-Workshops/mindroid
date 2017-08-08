package org.mindroid.api;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.statemachine.*;
import org.mindroid.impl.statemachine.constraints.GT;
import org.mindroid.impl.statemachine.constraints.LT;
import org.mindroid.impl.statemachine.constraints.MsgReceived;
import org.mindroid.impl.statemachine.constraints.Rotation;
import org.mindroid.impl.statemachine.constraints.TimeExpired;
import org.mindroid.impl.statemachine.properties.MessageProperty;
import org.mindroid.impl.statemachine.properties.Seconds;
import org.mindroid.impl.statemachine.properties.sensorproperties.Angle;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;
import org.mindroid.impl.statemachine.properties.sensorproperties.Touch;

import java.util.HashMap;

/**
 * Created by Torbe on 03.05.2017.
 */
public abstract class LVL2API extends LVL1API {

    public Motor motorA = null;
    public Motor motorB = null;
    public Motor motorC = null;
    public Motor motorD = null;

    private HashMap<String, Statemachine> sensorEvaluatingStatemachines = new HashMap<>();

    public static final String IMPERATIVE_STATEMACHINE_ID = "Imperative Statemachine Implementation";
    public static final String IMPERATIVE_GROUP_ID = "LVL2APIMACHINE";
    private ImperativeStatemachine imperativeStatemachine;


    public LVL2API() throws StateAlreadyExists {
        motorA = new Motor(motorController,EV3PortIDs.PORT_A);
        motorB = new Motor(motorController,EV3PortIDs.PORT_B);
        motorC = new Motor(motorController,EV3PortIDs.PORT_C);
        motorD = new Motor(motorController,EV3PortIDs.PORT_D);

        initSensorStatemachines();
        statemachineCollection.addParallelStatemachines(IMPERATIVE_GROUP_ID, sensorEvaluatingStatemachines.values().toArray(new Statemachine[sensorEvaluatingStatemachines.values().size()]));
        imperativeStatemachine = (ImperativeStatemachine)initStatemachine();
        statemachineCollection.addParallelStatemachines(IMPERATIVE_GROUP_ID,imperativeStatemachine);
    }

    /**
     * Register a Statemachine.
     *
     * @param sm
     */
    private void registerStatemachine(IStatemachine sm){
        StatemachineCollection sc = new StatemachineCollection();
        sc.addStatemachine(sm.getID(),sm);
        StatemachineManager.getInstance().addStatemachines(sc);
    }

    /**
     * Starting a Statemachine/group of Statemachines with the id.
     *
     * @param id
     */
    private void startStatemachine(String id){
        StatemachineManager.getInstance().startStatemachines(id);
    }

    /**
     * Stopping a statemachine
     * Note: All motors will be stopped.
     *
     * @param id
     */
    private void stopStatemachine(String id){
        StatemachineManager.getInstance().stopStatemachines(id);
    }




    public final IStatemachine initStatemachine() throws StateAlreadyExists {
        ImperativeStatemachine sm = new ImperativeStatemachine(IMPERATIVE_STATEMACHINE_ID);

        IState state_start = new State("Running state"){
            @Override
            public void run(){
                LVL2API.this.run();
            }
        };

        sm.addState(state_start);
        sm.setStartState(state_start);

        return sm;
    }

    public abstract void run();

    public final void setLED(EV3StatusLightColor color, EV3StatusLightInterval interval){
        if(!isInterrupted()) {
            brickController.setEV3StatusLight(color, interval);
        }
    }


    private void initSensorStatemachines() {
        sensorEvaluatingStatemachines.clear();
        BooleanStatemachine collisionDetection = new BooleanStatemachine("collisionDetection", false, new LT(0.15f,new Distance(EV3PortIDs.PORT_2)),new GT(0.15f,new Distance(EV3PortIDs.PORT_2)));
        sensorEvaluatingStatemachines.put("collisionDetection", collisionDetection);

        float[] colorValues =  {Color.NONE,Color.BLACK,Color.BLUE,Color.BROWN,Color.GREEN,Color.RED,Color.WHITE,Color.YELLOW};
        DiscreteValueStateMachine colorSM = new DiscreteValueStateMachine("colorSM", new Color(EV3PortIDs.PORT_1), colorValues );
        sensorEvaluatingStatemachines.put("colorSM",colorSM );

    }

    /**
     *
     *
     * @return true if the imperative Statemachine got interrupted (by stopping it)
     */
    public final boolean isInterrupted(){
        return imperativeStatemachine.isInterrupted();
    }



    public final boolean isCollisionDetected() {
        if (sensorEvaluatingStatemachines.containsKey("collisionDetection") && sensorEvaluatingStatemachines.get("collisionDetection") instanceof BooleanStatemachine) {
            return ((BooleanStatemachine)sensorEvaluatingStatemachines.get("collisionDetection")).getResult();
        } else {
            BooleanStatemachine collisionDetection = new BooleanStatemachine("collisionDetection", false, new LT(0.15f,new Distance(EV3PortIDs.PORT_2)),new GT(0.15f,new Distance(EV3PortIDs.PORT_2)));
            sensorEvaluatingStatemachines.put("collisionDetection",collisionDetection);
            registerStatemachine(collisionDetection);
            startStatemachine("collisionDetection");
            return ((BooleanStatemachine)sensorEvaluatingStatemachines.get("collisionDetection")).getResult();
        }
    }

    public final boolean distanceLessThan(float value) {
            if  (sensorEvaluatingStatemachines.containsKey("distanceLessThan"+ value) && sensorEvaluatingStatemachines.get("distanceLessThan"+ value) instanceof BooleanStatemachine) {
                return ((BooleanStatemachine)sensorEvaluatingStatemachines.get("distanceLessThan"+ value)).getResult();
            } else {
                BooleanStatemachine sm = new BooleanStatemachine("distanceLessThan"+ value, false, new LT(value,new Distance(EV3PortIDs.PORT_2)),new GT(value,new Distance(EV3PortIDs.PORT_2)));
                sensorEvaluatingStatemachines.put("distanceLessThan"+ value, sm);
                registerStatemachine(sm);
                startStatemachine("distanceLessThan"+ value);
                return sm.getResult();
            }

        //TODO maybe remove or stop statemachines when they are not needed anymore
    }

    public final boolean distanceGreaterThan(float value) {
        if (sensorEvaluatingStatemachines.containsKey("distanceGreaterThan"+ value) && sensorEvaluatingStatemachines.get("distanceGreaterThan"+ value) instanceof BooleanStatemachine) {
            return ((BooleanStatemachine)sensorEvaluatingStatemachines.get("distanceGreaterThan"+ value)).getResult();
        } else {
            BooleanStatemachine sm = new BooleanStatemachine("distanceGreaterThan"+ value, true, new GT(value,new Distance(EV3PortIDs.PORT_2)),new LT(value,new Distance(EV3PortIDs.PORT_2)));
            sensorEvaluatingStatemachines.put("distanceGreaterThan"+ value, sm);
            registerStatemachine(sm);
            startStatemachine("distanceGreaterThan"+value);
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
     * @param message expected message
     * @param source expected source of the message
     * @return true - if message received , otherwise false
     */
    public final boolean wasMsgReceived(String message, String source) {
        //TODO needs to be tested
        if (sensorEvaluatingStatemachines.containsKey("Msg"+ message + source) && sensorEvaluatingStatemachines.get("Msg"+ message + source) instanceof BooleanStatemachine) {
            //Statemachine has already been started.
            boolean result = ((BooleanStatemachine)sensorEvaluatingStatemachines.get("Msg"+ message + source)).getResult();
            if (!result) {
                //SM will keep running as the message was not received yet.
                return false;
            } else {
                //SM is stopped and removed.
                stopStatemachine("Msg"+ message + source);
                sensorEvaluatingStatemachines.remove("Msg"+ message + source);
                return true;
            }
        } else {
            //create new SM
            BooleanStatemachine sm = new BooleanStatemachine("Msg"+ message + source, false, new MsgReceived(new MessageProperty(message,source)),null);
            sensorEvaluatingStatemachines.put("Msg"+ message + source, sm);
            registerStatemachine(sm);
            startStatemachine("Msg"+ message + source);
            boolean result = sm.getResult();

            if (!result) {
                //SM will keep running as the message was not received yet.
                return false;

            } else {
                //SM is stopped and removed.
                stopStatemachine("Msg"+ message + source);
                sensorEvaluatingStatemachines.remove("Msg"+ message + source);
                return true;
            }
        }

    }

    public final float getColor() {
        if (sensorEvaluatingStatemachines.containsKey("colorSM") && sensorEvaluatingStatemachines.get("colorSM") instanceof DiscreteValueStateMachine) {
            return ((DiscreteValueStateMachine)sensorEvaluatingStatemachines.get("colorSM")).getResult();
        } else {
            float[] colorValues =  {Color.NONE,Color.BLACK,Color.BLUE,Color.BROWN,Color.GREEN,Color.RED,Color.WHITE,Color.YELLOW};
            DiscreteValueStateMachine colorSM = new DiscreteValueStateMachine("colorSM", new Color(EV3PortIDs.PORT_1), colorValues );
            sensorEvaluatingStatemachines.put("colorSM",colorSM );
            statemachineCollection.addParallelStatemachines("LVL2APIMachine", colorSM);
            return ((DiscreteValueStateMachine)sensorEvaluatingStatemachines.get("colorSM")).getResult();
        }
    }

    public final boolean isColorBlack() {
        return (getColor()==Color.BLACK);
    }

    public final boolean isColor(float color){
        return (getColor()==color);
    }


    public final void delay(long milliseconds) {
        if(!isInterrupted()) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * The robot rotates counterclockwise by the given angle. The method blocks until the rotation is completed.
     * @param degrees angle
     */
    public final void turnLeft(int degrees) {
        if(!isInterrupted()) {
            //TODO check port config before creating SM
            BooleanStatemachine angleSM = new BooleanStatemachine("angleSM", false, new Rotation((-1) * degrees, new Angle(EV3PortIDs.PORT_3)), null);
            registerStatemachine(angleSM);
            startStatemachine(angleSM.getID());
            motorController.setMotorDirection(EV3PortIDs.PORT_A, IMotorControl.MOTOR_BACKWARD);
            motorController.setMotorDirection(EV3PortIDs.PORT_D, IMotorControl.MOTOR_FORWARD);
            motorController.setMotorSpeed(EV3PortIDs.PORT_A, 50);
            motorController.setMotorSpeed(EV3PortIDs.PORT_D, 50);
            while (!angleSM.getResult() && !isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopStatemachine(angleSM.getID());
            motorController.stop(EV3PortIDs.PORT_A);
            motorController.stop(EV3PortIDs.PORT_D);
        }
    }

    /**
     * The robot rotates clockwise by the given angle. The method blocks until the rotation is completed.
     * @param degrees angle
     */
    public final void turnRight(int degrees) {
        if(!isInterrupted()) {
            BooleanStatemachine angleSM = new BooleanStatemachine("angleSM", false, new Rotation(degrees, new Angle(EV3PortIDs.PORT_3)), null);
            registerStatemachine(angleSM);
            startStatemachine("angleSM");
            motorController.setMotorDirection(EV3PortIDs.PORT_A, IMotorControl.MOTOR_FORWARD);
            motorController.setMotorDirection(EV3PortIDs.PORT_D, IMotorControl.MOTOR_BACKWARD);
            motorController.setMotorSpeed(EV3PortIDs.PORT_A, 50);
            motorController.setMotorSpeed(EV3PortIDs.PORT_D, 50);
            while (!angleSM.getResult() && !isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopStatemachine("angleSM");
            motorController.stop(EV3PortIDs.PORT_A);
            motorController.stop(EV3PortIDs.PORT_D);
        }
    }

    /**
     * The robot rotates counterclockwise for the specified time. The method blocks until the rotation is completed.
     * @param milliseconds time in milliseconds
     */
    public final void turnLeftTime(int milliseconds) {
        if(!isInterrupted()) {
            motorController.setMotorDirection(EV3PortIDs.PORT_A, IMotorControl.MOTOR_BACKWARD);
            motorController.setMotorDirection(EV3PortIDs.PORT_D, IMotorControl.MOTOR_FORWARD);
            motorController.setMotorSpeed(EV3PortIDs.PORT_A, 50);
            motorController.setMotorSpeed(EV3PortIDs.PORT_D, 50);
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            motorController.stop(EV3PortIDs.PORT_A);
            motorController.stop(EV3PortIDs.PORT_D);
        }
    }

    /**
     * The robot rotates clockwise for the specified time. The method blocks until the rotation is completed.
     * @param milliseconds time in milliseconds
     */
    public final void turnRightTime(int milliseconds) {
        if(!isInterrupted()) {
            motorController.setMotorDirection(EV3PortIDs.PORT_A, IMotorControl.MOTOR_FORWARD);
            motorController.setMotorDirection(EV3PortIDs.PORT_D, IMotorControl.MOTOR_BACKWARD);
            motorController.setMotorSpeed(EV3PortIDs.PORT_A, 50);
            motorController.setMotorSpeed(EV3PortIDs.PORT_D, 50);
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            motorController.stop(EV3PortIDs.PORT_A);
            motorController.stop(EV3PortIDs.PORT_D);
        }
    }


    public void stopMotors() {
        motorA.stop();
        motorB.stop();
        motorC.stop();
        motorD.stop();
    }

    @Override
    public void forward(){
        if(!isInterrupted()){
            super.forward();
        }
    }

    @Override
    public void backward(){
        if(!isInterrupted()){
            super.backward();
        }
    }

}
