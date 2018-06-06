package org.mindroid.common.messages.motor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class RotateMessage implements ILoggable {
    private int angle;
    private boolean immediateReturn = false;

    public RotateMessage(){ }

    public RotateMessage(int angle){
        this.angle = angle;
    }

    public RotateMessage(int angle,boolean immediateReturn){
        this.angle = angle;
        this.immediateReturn = immediateReturn;
    }

    public int getAngle() {
        return angle;
    }

    public boolean isImmediateReturn() {
        return immediateReturn;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}