package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class RotateToMessage implements ILoggable {
    private boolean immediateRetrun = false;
    private int angle;

    public RotateToMessage(){ }

    public RotateToMessage(int angle){
        this.angle = angle;
    }

    public RotateToMessage(int angle,boolean immediateRetrun){
        this.angle = angle;
        this.immediateRetrun = immediateRetrun;
    }

    public int getAngle() {
        return angle;
    }

    public boolean isImmediateRetrun() {
        return immediateRetrun;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}