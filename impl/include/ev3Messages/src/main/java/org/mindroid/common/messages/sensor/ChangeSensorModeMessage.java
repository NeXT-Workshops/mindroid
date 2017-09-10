package org.mindroid.common.messages.sensor;

import org.mindroid.common.messages.hardware.Sensormode;

public class ChangeSensorModeMessage {
    private Sensormode newMode = null;

    public ChangeSensorModeMessage(){}

    public void setNewMode(Sensormode m){
        this.newMode = m;
    }

    public Sensormode getNewMode(){
        return this.newMode;
    }
}
