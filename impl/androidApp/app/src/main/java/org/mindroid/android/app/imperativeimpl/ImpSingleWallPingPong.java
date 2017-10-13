package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;

public class ImpSingleWallPingPong extends ImperativeWorkshopAPI {


    public ImpSingleWallPingPong() {
        super("ImpSingleWallPingPong");
    }

    @Override
    public void run() {
        do {
            forward(); //Forward is not implemented yet!

            while (getDistance() > 0.15f && !isInterrupted()) {
                delay(25);
            }

            backward(10);
            turnLeft(180);

        }while(!isInterrupted());
    }
}
