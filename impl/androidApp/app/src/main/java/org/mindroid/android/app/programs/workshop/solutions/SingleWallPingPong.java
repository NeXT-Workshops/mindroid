package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;

public class SingleWallPingPong extends ImperativeWorkshopAPI {


    public SingleWallPingPong() {
        super("Single Wall Ping-Pong [sol]");
    }

    @Override
    public void run() {
        do {
            forward(500);
            while (getDistance() > 15f && !isInterrupted()) {
                delay(25);
            }
            stop();
            driveDistanceBackward(10);
            turnLeft(180);
        }while(!isInterrupted());
    }
}
