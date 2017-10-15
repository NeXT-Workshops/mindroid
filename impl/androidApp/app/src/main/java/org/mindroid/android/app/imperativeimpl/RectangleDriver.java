package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;

public class RectangleDriver extends ImperativeWorkshopAPI {


    /**
     *  Robot drives a Rectangle
     */
    public RectangleDriver() {
        super("RectangleDriverClockwise");
    }


    @Override
    public void run() {
        driveDistanceForward(25f);
        turnRight(90);

        driveDistanceForward(25f);
        turnRight(90);

        driveDistanceForward(25f);
        turnRight(90);

        driveDistanceForward(25f);
        turnRight(90);
    }
}
