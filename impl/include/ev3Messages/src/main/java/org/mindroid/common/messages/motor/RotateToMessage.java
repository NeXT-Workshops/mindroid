package org.mindroid.common.messages.motor;

public class RotateToMessage{
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
}