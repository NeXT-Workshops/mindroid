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
        //forward(10f);
        //TODO implement blocking
        //turnLeft(90);
        turnRight(90);
    }
}
