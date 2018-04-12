package org.mindroid.common.messages.led;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class SetStatusLightMessage implements ILoggable {

    public int val = 0;
    public SetStatusLightMessage(){ };
    public int getVal() {
        return val;
    }
    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}