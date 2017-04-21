package org.mindroid.impl.statemachine.constraints;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.statemachine.constraints.AbstractMessageComparator;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.statemachine.properties.MessageProperty;

/**
 * Created by torben on 16.03.2017.
 */
public class MsgReceived extends AbstractMessageComparator {
    public MsgReceived(IProperty property) {
        super(property);
    }

    @Override
    public boolean evaluate(IRobotContextState context) {
        for(MindroidMessage msg: context.getMessages()){
            if(evaluate(msg)){
                return true;
            }
        }
        return false;
    }


    private boolean evaluate(MindroidMessage msg){
        boolean satisfiedSource = (msg.getSource().getValue().equals(((MessageProperty)getProperty()).getSource()));
        boolean satisfiedDestination = true; //TODO check if the message is actually for me? broadcast?
        boolean satisfiedContent = msg.getContent().equals(((MessageProperty)getProperty()).getContent());
        return  satisfiedSource && satisfiedContent && satisfiedDestination;
    }


}
