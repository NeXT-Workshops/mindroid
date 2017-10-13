package org.mindroid.android.app.imperativeimpl;

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
        forward(25f);
        turnLeft(90);

        forward(25f);
        turnLeft(90);

        forward(25f);
        turnLeft(90);

        forward(25f);
        turnLeft(90);
    }
}
