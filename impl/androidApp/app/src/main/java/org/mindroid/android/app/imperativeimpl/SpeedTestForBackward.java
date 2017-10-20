package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;

public class SpeedTestForBackward extends ImperativeWorkshopAPI{

    public SpeedTestForBackward(){
        super("SpeedTestForwardBackward");
    }

    @Override
    public void run() {
        forward(0);
        delay(2000);
        flt();
        backward(0);
        delay(2000);
        flt();


        forward(250);
        delay(2000);
        flt();
        backward(250);
        delay(2000);
        flt();

        forward(500);
        delay(2000);
        flt();
        backward(500);
        delay(2000);
        flt();

        forward(750);
        delay(2000);
        flt();
        backward(750);
        delay(2000);
        flt();

        forward(1000);
        delay(2000);
        flt();
        backward(1000);
        delay(2000);
        flt();
    }
}
