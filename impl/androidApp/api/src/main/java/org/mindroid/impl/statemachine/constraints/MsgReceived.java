package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.statemachine.constraints.AbstractMessageComparator;
import org.mindroid.api.statemachine.properties.IProperty;

/**
 * Created by torben on 16.03.2017.
 */
public class MsgReceived extends AbstractMessageComparator {
    public MsgReceived(IProperty property) {
        super(property);
    }

    @Override
    public boolean evaluate(IRobotContextState context) {

        //TODO implement
        //call evaluate(msg);
        return false;
    }

    /*
    private boolean evaluate(Message msg){
        //TODO implement
        return false;
    }*/


}
