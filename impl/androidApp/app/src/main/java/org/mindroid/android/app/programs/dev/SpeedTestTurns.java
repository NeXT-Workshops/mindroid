package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class SpeedTestTurns extends ImperativeWorkshopAPI {
    /**

     */
    public SpeedTestTurns() {
        super("SpeedTestTurns");
    }

    @Override
    public void run() {
        turnLeft(90,250);
        delay(1000);
        turnLeft(90,500);
        delay(1000);
        turnLeft(90,750);
        delay(1000);
        turnLeft(90,1000);

        delay(1000);

        turnRight(90,250);
        delay(1000);
        turnRight(90,500);
        delay(1000);
        turnRight(90,750);
        delay(1000);
        turnRight(90,1000);
    }
}
