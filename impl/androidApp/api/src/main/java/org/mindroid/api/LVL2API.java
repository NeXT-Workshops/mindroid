package org.mindroid.api;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.impl.brick.LED;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.Statemachine;
import org.mindroid.impl.statemachine.Transition;
import org.mindroid.impl.statemachine.constraints.GT;
import org.mindroid.impl.statemachine.constraints.LT;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

/**
 * Created by Torbe on 03.05.2017.
 */
public abstract class LVL2API extends LVL1API {

    public Motor motorA = null;
    public Motor motorB = null;
    public Motor motorC = null;
    public Motor motorD = null;

    private boolean collision = false;


    public LVL2API() throws StateAlreadyExists {
        motorA = new Motor(motorController,EV3PortIDs.PORT_A);
        motorB = new Motor(motorController,EV3PortIDs.PORT_B);
        motorC = new Motor(motorController,EV3PortIDs.PORT_C);
        motorD = new Motor(motorController,EV3PortIDs.PORT_D);

        statemachineCollection.addParallelStatemachines("LVL2APIMachine",getCollisionDetectionStatemachine()/*, getXX(), getXY(), ... */);

        statemachineCollection.addParallelStatemachines("LVL2APIMachine",initStatemachine());
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

    public LED led; //TODO

    public final void forward(/** distance in cm **/){
        //TODO implement
    }

    public final void backward(/** distance in cm **/){
        //TODO implement
    }

    public final void turnLeft(/** degrees**/){
        //TODO implement
    }

    public final void turnRight(/** degrees**/){
        //TODO implement
    }

    private final IStatemachine getCollisionDetectionStatemachine() throws StateAlreadyExists {
        //TODO implement Statemachine to detect collision
        Statemachine sm_coll_detection = new Statemachine("Collision Detection");
        IState checking_collision = new State("checking_collision");

        IConstraint constr_collision = new LT(0.15f,new Distance(EV3PortIDs.PORT_2));
        IConstraint constr_no_collision = new GT(0.15f,new Distance(EV3PortIDs.PORT_2));

        ITransition trans_collision = new Transition(constr_collision){
            @Override
            public void run(){
                collision = true;
            }
        };

        ITransition trans_no_collision = new Transition(constr_no_collision){
            @Override
            public void run(){
                collision = false;
            }
        };

        sm_coll_detection.addState(checking_collision);
        sm_coll_detection.setStartState(checking_collision);

        sm_coll_detection.addTransition(trans_collision,checking_collision,checking_collision);
        sm_coll_detection.addTransition(trans_no_collision,checking_collision,checking_collision);

        return sm_coll_detection;
    }

    public final boolean isCollisionDetected() {
        return collision;
    }

    public final boolean hasLineDetected(){
        //TODO implement
        return false;
    }

    public final boolean isColor(int color){
        //TODO implement
        return false;
    }

    //TODO and so one

}
