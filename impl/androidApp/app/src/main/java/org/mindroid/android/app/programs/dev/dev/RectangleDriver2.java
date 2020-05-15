package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class RectangleDriver2 extends ImperativeWorkshopAPI {
    /**
     */
    public RectangleDriver2() {
        super("RectangleDriver_2");
    }

    @Override
    public void run() {
        driveDistanceForward(25f);
        turnLeft(90);

        driveDistanceBackward(25f);
        turnRight(270);

        driveDistanceForward(25f);
        turnRight(90);

        driveDistanceForward(12.5f);
        turnLeft(180);

        driveDistanceBackward(12.5f);
        turnLeft(90);
    }
}
