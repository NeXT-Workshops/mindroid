package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.statemachine.ITimeEvent;
import org.mindroid.api.statemachine.constraints.AbstractComparator;
import org.mindroid.api.statemachine.properties.ITimeProperty;
import org.mindroid.impl.statemachine.properties.Milliseconds;
import org.mindroid.impl.statemachine.properties.Seconds;

/**
 * Created by torben on 21.03.2017.
 */
public class TimeExpired extends AbstractComparator {

    private long delta = 50;

    public TimeExpired(ITimeProperty property) {
        super(property);
    }

    private boolean evaluate(long t_stateActivated, ITimeEvent timeEvent){
        if(timeEvent.getDelay() == ((ITimeProperty)getProperty()).getTime() && timeEvent.getOwner().equals(((ITimeProperty)getProperty()).getSource())){
            if( (System.currentTimeMillis() - t_stateActivated - delta) < timeEvent.getDelay()){ //State has to be longer active since the delay of occuring timeevent
                return false; //False Satisfied TimeEvent Constraint
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean evaluate(IRobotContextState context) {
        long t_stateActivated = context.getStartCondition().getStateActiveTime();
        boolean satisfied = false;

        for (ITimeEvent iTimeEvent : context.getTimeEvents()) {
            satisfied = evaluate(t_stateActivated,iTimeEvent);
            if(satisfied){
                break;
            }
        }
        return satisfied;
    }

}
