package org.mindroid.impl.statemachine;

import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.exception.StateAlreadyExistsException;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.impl.statemachine.constraints.EQ;

import java.util.ArrayList;

/**
 * Created by Felicia Ruppel on 09.05.17.
 */
@Deprecated //Not in use anymore as this got replaced by ImperativeAPI
public class DiscreteValueStateMachine extends Statemachine {
    private float result;
    public DiscreteValueStateMachine(String ID, SimpleEV3SensorProperty property, final float[] values) {
        super(ID);

        setIsMessageingAllowed(false);

        if(values.length<1) {
            result = 0;
            return;
        }

        result = values[0];

        ArrayList<IState> states = new ArrayList<>();
        for (int i = 0; i< values.length; i++) {
            IState state = new State("Value "+ values[i]);
            states.add(state);

            this.addState(state);
            if (i==0) {this.setStartState(state);}
        }

        for (int j=0; j< values.length; j++) {
            final float value = values[j];
            ITransition transition = new Transition(new EQ(value,property)){
                @Override
                public void run(){
                    result = value;
                }
            };

            for (int k=0; k< values.length; k++) {
                if (k!=j) {
                    this.addTransition(transition, states.get(k), states.get(j));
                }
            }
        }
    }

    public float getResult() {
        return result;
    }
}
