package org.mindroid.impl.statemachine;

import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.StateAlreadyExistsException;

/**
 * Created by Felicia Ruppel on 09.05.17.
 */
@Deprecated //Not in use anymore as this got replaced by ImperativeAPI
public class BooleanStatemachine extends Statemachine {

    private boolean result;
    public BooleanStatemachine(String ID, boolean defaultValue, IConstraint returnTrue, IConstraint returnFalse) {
        super(ID);

        setIsMessageingAllowed(false);

        result = defaultValue;
        IState resultTrue = new State(ID + ": True");
        IState resultFalse = new State(ID + ": False");

        this.addState(resultTrue);
        this.addState(resultFalse);

        if (defaultValue) {
            this.setStartState(resultTrue);
        } else {
            this.setStartState(resultFalse);
        }


        if (returnTrue!=null) {
            ITransition transReturnsTrue = new Transition(returnTrue) {
                @Override
                public void run() {
                    result = true;
                }
            };
            this.addTransition(transReturnsTrue,resultFalse,resultTrue);
        }

        if (returnFalse!= null) {
            ITransition transReturnsFalse = new Transition(returnFalse) {
                @Override
                public void run() {
                    result = false;
                }
            };
            this.addTransition(transReturnsFalse,resultTrue,resultFalse);
        }




    }

    public boolean getResult() {
        return result;
    }


}