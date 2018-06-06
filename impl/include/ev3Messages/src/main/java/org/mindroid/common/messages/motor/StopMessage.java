package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class StopMessage implements ILoggable {

    boolean immidiateReturn = false;

    public StopMessage(){

    }

    public StopMessage(boolean immidiateReturn){
        this.immidiateReturn = immidiateReturn;
    }

    public boolean isImmidiateReturn() {
        return immidiateReturn;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
