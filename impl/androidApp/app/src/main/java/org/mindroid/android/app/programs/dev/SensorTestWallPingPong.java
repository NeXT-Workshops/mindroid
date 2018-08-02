package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.statemachine.properties.Colors;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;

public class SensorTestWallPingPong extends ImperativeWorkshopAPI {
    public SensorTestWallPingPong() {
        super("SensorTestWallPingPong");
    }

    @Override
    public void run() {

        forward(500);
        while(!isInterrupted()){
            if(getDistance() < 15f || getLeftColor() == Colors.WHITE || getRightColor() == Colors.WHITE){
                stop();
                delay(50);
                turnLeft(180);
                forward(500);
            }
            delay(10);
        }
    }
}
