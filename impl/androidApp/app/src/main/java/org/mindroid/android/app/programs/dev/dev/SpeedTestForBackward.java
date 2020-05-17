package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class SpeedTestForBackward extends ImperativeWorkshopAPI{

    public SpeedTestForBackward(){
        super("SpeedTestForwardBackward");
    }

    @Override
    public void run() {
        forward(0);
        delay(2000);
        enableFloatMode();
        backward(0);
        delay(2000);
        enableFloatMode();


        forward(250);
        delay(2000);
        enableFloatMode();
        backward(250);
        delay(2000);
        enableFloatMode();

        forward(500);
        delay(2000);
        enableFloatMode();
        backward(500);
        delay(2000);
        enableFloatMode();

        forward(750);
        delay(2000);
        enableFloatMode();
        backward(750);
        delay(2000);
        enableFloatMode();

        forward(1000);
        delay(2000);
        enableFloatMode();
        backward(1000);
        delay(2000);
        enableFloatMode();
    }
}
