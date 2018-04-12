package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class FltMessage implements ILoggable {

    private boolean immediateRetrun = false;

    public FltMessage(){

    }

    public FltMessage(boolean immediateRetrun) {
        this.immediateRetrun = immediateRetrun;
    }

    public boolean isImmediateRetrun() {
        return immediateRetrun;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
