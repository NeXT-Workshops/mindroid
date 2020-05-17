package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class RectangleDriver3 extends ImperativeWorkshopAPI {


    /**
     *  Robot drives a Rectangle
     */
    public RectangleDriver3() {
        super("RectangleDriverCounterclockwise");
    }


    @Override
    public void run() {
        driveDistanceForward(25f);
        turnLeft(90);

        driveDistanceForward(25f);
        turnLeft(90);

        driveDistanceForward(25f);
        turnLeft(90);

        driveDistanceForward(25f);
        turnLeft(90);
    }
}
