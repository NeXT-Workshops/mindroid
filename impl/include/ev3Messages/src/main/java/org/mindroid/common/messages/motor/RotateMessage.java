package org.mindroid.common.messages.motor;

public class RotateMessage{
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
}