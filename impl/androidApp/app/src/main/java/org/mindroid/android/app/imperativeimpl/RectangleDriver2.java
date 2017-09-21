package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;

public class RectangleDriver2 extends ImperativeWorkshopAPI {
    /**
     */
    public RectangleDriver2() {
        super("RectangleDriver_2");
    }

    @Override
    public void run() {
        forward(25f);
        turnLeft(90);

        backward(25f);
        turnRight(270);

        forward(25f);
        turnRight(90);

        forward(12.5f);
        turnLeft(180);

        backward(12.5f);
        turnLeft(90);
    }
}
