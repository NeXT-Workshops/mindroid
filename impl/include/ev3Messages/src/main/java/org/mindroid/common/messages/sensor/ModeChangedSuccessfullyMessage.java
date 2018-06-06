package org.mindroid.common.messages.sensor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.hardware.Sensormode;

public class ModeChangedSuccessfullyMessage implements ILoggable {
    private Sensormode newMode;

    public ModeChangedSuccessfullyMessage(){}

    public void setNewMode(Sensormode m){
        this.newMode = m;
    }

    public Sensormode getMode(){
        return this.newMode;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}