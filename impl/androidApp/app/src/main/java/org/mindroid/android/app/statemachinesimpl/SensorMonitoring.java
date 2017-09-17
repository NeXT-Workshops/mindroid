package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.StatemachineAPI;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.Statemachine;

/**
 * Created by torben on 17.08.2017.
 */
public class SensorMonitoring extends StatemachineAPI {

    public SensorMonitoring(){
        IStatemachine sm = getSensorMonitoringStatemachine();
        getStatemachineCollection().addStatemachine(sm.getID(),sm);
    }

    public IStatemachine getSensorMonitoringStatemachine(){
        IStatemachine sm = new Statemachine("SensorMonitoring");

        State state_idle = new State("state_idle"){

        };

        sm.addState(state_idle);
        sm.setStartState(state_idle);

        return sm;
    }
}
