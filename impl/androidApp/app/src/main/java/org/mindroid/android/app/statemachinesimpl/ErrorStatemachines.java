package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL1API;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.Statemachine;

/**
 * Created by torben on 09.08.2017.
 */
public class ErrorStatemachines extends LVL1API{

    public ErrorStatemachines(){

    }


    public void initStatemachines(){
        IStatemachine tmpStatemachine;
        //Statemachine rotate 90 degrees
        tmpStatemachine = errorMachine1();
        statemachineCollection.addStatemachine(tmpStatemachine.getID(),tmpStatemachine);
        tmpStatemachine = errorMachine2();
        statemachineCollection.addStatemachine(tmpStatemachine.getID(),tmpStatemachine);
    }

    /**
     *
     * @return
     */
    public IStatemachine errorMachine1(){
        IStatemachine sm = new Statemachine("NoStartStateMachine");

        State s1 = new State("State_1");
        State s2 = new State("State_2");

        sm.addState(s1);
        sm.addState(s2);

        return sm;
    }

    /**
     *
     * @return
     */
    public IStatemachine errorMachine2(){
        IStatemachine sm = new Statemachine("DupicateStateMachine");

        State s1 = new State("State_1");
        State s2 = new State("State_1");

        sm.addState(s1);
        sm.setStartState(s1);
        sm.addState(s2);

        return sm;
    }
}
