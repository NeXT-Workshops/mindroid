package org.mindroid.common.messages.sensor;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.hardware.Sensormode;

public class ChangeSensorModeMessage implements ILoggable {
    private Sensormode newMode = null;

    public ChangeSensorModeMessage(){}

    public void setNewMode(Sensormode m){
        this.newMode = m;
    }

    public Sensormode getNewMode(){
        return this.newMode;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
