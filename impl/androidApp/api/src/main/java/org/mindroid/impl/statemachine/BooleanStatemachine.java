package org.mindroid.impl.statemachine;

import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;

/**
 * Created by Felicia Ruppel on 09.05.17.
 */

public class BooleanStatemachine extends Statemachine {

    private boolean result;
    public BooleanStatemachine(String ID, boolean defaultValue, IConstraint returnTrue, IConstraint returnFalse) {
        super(ID);
        result = defaultValue;
        IState resultTrue = new State("True");
        IState resultFalse = new State("False");

        try {
            this.addState(resultTrue);
            this.addState(resultFalse);
        } catch (StateAlreadyExists stateAlreadyExists) {
        }

        if (defaultValue) {
            this.setStartState(resultTrue);
        } else {
            this.setStartState(resultFalse);
        }

        ITransition transReturnsTrue = new Transition(returnTrue){
            @Override
            public void run(){
                result = true;
            }
        };

        ITransition transReturnsFalse = new Transition(returnFalse){
            @Override
            public void run(){
                result = false;
            }
        };

        this.addTransition(transReturnsFalse,resultTrue,resultFalse);
        this.addTransition(transReturnsTrue,resultFalse,resultTrue);

    }

    public boolean getResult() {
        return result;
    }


}