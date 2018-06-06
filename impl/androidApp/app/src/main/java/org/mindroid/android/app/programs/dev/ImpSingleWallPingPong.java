package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class ImpSingleWallPingPong extends ImperativeWorkshopAPI {


    public ImpSingleWallPingPong() {
        super("ImpSingleWallPingPong");
    }

    @Override
    public void run() {
        do {
            forward(500);

            while (getDistance() > 0.15f && !isInterrupted()) {
                delay(25);
            }
            stop();

            driveDistanceBackward(10);

            turnLeft(180);

        }while(!isInterrupted());
    }
}
