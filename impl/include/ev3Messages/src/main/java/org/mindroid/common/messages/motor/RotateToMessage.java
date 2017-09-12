package org.mindroid.common.messages.motor;

public class RotateToMessage{
    private boolean immidiateReturn;
    private int angle;

    public RotateToMessage(){ }

    public RotateToMessage(int angle){
        this.angle = angle;
    }

    public RotateToMessage(int angle,boolean immidiateReturn){
        this.angle = angle;
        this.immidiateReturn = immidiateReturn;
    }

    public int getAngle() {
        return angle;
    }

    public boolean isImmidiateReturn() {
        return immidiateReturn;
    }
}