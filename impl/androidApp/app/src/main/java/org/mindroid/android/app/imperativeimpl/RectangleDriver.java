package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;

public class RectangleDriver extends ImperativeWorkshopAPI {


    /**
     *  Robot drives a Rectangle
     */
    public RectangleDriver() {
        super("RectangleDriver");
    }


    @Override
    public void run() {
        forward(25f);
        turnRight(90);

        forward(25f);
        turnRight(90);

        forward(25f);
        turnRight(90);

        forward(25f);
        turnRight(90);
    }
}
