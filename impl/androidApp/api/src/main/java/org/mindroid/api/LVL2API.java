package org.mindroid.api;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.impl.brick.LED;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.statemachine.*;
import org.mindroid.impl.statemachine.constraints.GT;
import org.mindroid.impl.statemachine.constraints.LT;
import org.mindroid.impl.statemachine.constraints.Rotation;
import org.mindroid.impl.statemachine.constraints.TimeExpired;
import org.mindroid.impl.statemachine.properties.Seconds;
import org.mindroid.impl.statemachine.properties.sensorproperties.Angle;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

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

    public LED led = new LED(brickController); //TODO: LED not fully implemented

    public LVL2API() throws StateAlreadyExists {
        motorA = new Motor(motorController,EV3PortIDs.PORT_A);
        motorB = new Motor(motorController,EV3PortIDs.PORT_B);
        motorC = new Motor(motorController,EV3PortIDs.PORT_C);
        motorD = new Motor(motorController,EV3PortIDs.PORT_D);

        initSensorStatemachines();
        statemachineCollection.addParallelStatemachines("LVL2APIMachine", sensorEvaluatingStatemachines.values().toArray(new Statemachine[sensorEvaluatingStatemachines.values().size()]));
        statemachineCollection.addParallelStatemachines("LVL2APIMachine",initStatemachine());
    }

    /**
     * Register a Statemachine.
     *
     * @param sm
     */
    private void registerStatemachine(IStatemachine sm){
        StatemachineCollection sc = new StatemachineCollection();
        sc.addStatemachine(sm);
        StatemachineManager.getInstance().addStatemachines(sc);
    }

    /**
     * Starting a Statemachine/group of Statemachines with the id.
     *
     * @param id
     */
    private void startStatemachine(String id){
        StatemachineManager.getInstance().startStatemachine(id);
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
        Statemachine sm = new Statemachine("Implementation");

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
        brickController.setEV3StatusLight(color,interval);
    }


    private void initSensorStatemachines() {
        sensorEvaluatingStatemachines.clear();
        BooleanStatemachine collisionDetection = new BooleanStatemachine("collisionDetection", false, new LT(0.15f,new Distance(EV3PortIDs.PORT_2)),new GT(0.15f,new Distance(EV3PortIDs.PORT_2)));
        sensorEvaluatingStatemachines.put("collisionDetection", collisionDetection);

        float[] colorValues =  {Color.NONE,Color.BLACK,Color.BLUE,Color.BROWN,Color.GREEN,Color.RED,Color.WHITE,Color.YELLOW};
        DiscreteValueStateMachine colorSM = new DiscreteValueStateMachine("colorSM", new Color(EV3PortIDs.PORT_1), colorValues );
        sensorEvaluatingStatemachines.put("colorSM",colorSM );

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
            BooleanStatemachine sm = new BooleanStatemachine("distanceLessThan"+ value, false, new LT(value,new Distance(EV3PortIDs.PORT_2)),new GT(value,new Distance(EV3PortIDs.PORT_2)));
            registerStatemachine(sm);
            startStatemachine("distanceLessThan"+ value);
            //TODO how long?
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }

        return sm.getResult();


        //TODO maybe delete or stop statemachines when they are not needed anymore
    }

    public final boolean distanceGreaterThan(float value) {
        if (sensorEvaluatingStatemachines.containsKey("distanceGreaterThan"+ value) && sensorEvaluatingStatemachines.get("distanceGreaterThan"+ value) instanceof BooleanStatemachine) {
            return ((BooleanStatemachine)sensorEvaluatingStatemachines.get("distanceGreaterThan"+ value)).getResult();
        } else {
            BooleanStatemachine sm = new BooleanStatemachine("distanceGreaterThan"+ value, true, new GT(value,new Distance(EV3PortIDs.PORT_2)),new LT(value,new Distance(EV3PortIDs.PORT_2)));
            sensorEvaluatingStatemachines.put("distanceGreaterThan"+ value, sm);
            statemachineCollection.addParallelStatemachines("LVL2APIMachine", sm);
            return ((BooleanStatemachine)sensorEvaluatingStatemachines.get("distanceGreaterThan"+ value)).getResult();
        }

        //TODO maybe remove statemachines after a certain timeout when they are not needed anymore
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
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    public final void turnLeft(int degrees) {
        BooleanStatemachine angleSM = new BooleanStatemachine("angleSM",false, new Rotation(degrees,new Angle(EV3PortIDs.PORT_3)),new TimeExpired(new Seconds(100)));
        registerStatemachine(angleSM);
        startStatemachine("angleSM");
        motorController.setMotorDirection(EV3PortIDs.PORT_A, IMotorControl.MOTOR_BACKWARD);
        motorController.setMotorDirection(EV3PortIDs.PORT_D,IMotorControl.MOTOR_FORWARD);
        motorController.setMotorSpeed(EV3PortIDs.PORT_A,50);
        motorController.setMotorSpeed(EV3PortIDs.PORT_D,50);
        while(!angleSM.getResult()){
            try {
                //TODO how long?
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopStatemachine("angleSM");
        motorController.stop(EV3PortIDs.PORT_A);
        motorController.stop(EV3PortIDs.PORT_D);
    }

    public final void turnLeftTime(int milliseconds) {
        motorController.setMotorDirection(EV3PortIDs.PORT_A, IMotorControl.MOTOR_BACKWARD);
        motorController.setMotorDirection(EV3PortIDs.PORT_D,IMotorControl.MOTOR_FORWARD);
        motorController.setMotorSpeed(EV3PortIDs.PORT_A,50);
        motorController.setMotorSpeed(EV3PortIDs.PORT_D,50);
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        motorController.stop(EV3PortIDs.PORT_A);
        motorController.stop(EV3PortIDs.PORT_D);
    }

    public void stop() {
        motorA.stop();
        motorB.stop();
        motorC.stop();
        motorD.stop();
    }

}
