package org.mindroid.common.messages.sensor;

import org.mindroid.common.messages.hardware.Sensormode;

public class ModeChangedSuccessfullyMessage {
    private Sensormode newMode;

    public ModeChangedSuccessfullyMessage(){}

    public void setNewMode(Sensormode m){
        this.newMode = m;
    }

    public Sensormode getMode(){
        return this.newMode;
    }
}