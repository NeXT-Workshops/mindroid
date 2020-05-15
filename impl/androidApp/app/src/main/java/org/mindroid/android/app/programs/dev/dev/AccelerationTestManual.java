package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

public class AccelerationTestManual extends ImperativeWorkshopAPI {

    public AccelerationTestManual() {
        super("Accel Test Man");
    }

    @Override
    public void run() {
        setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.BLINKING);

        accelerate(200, 10);
        delay(2000);
        stop();

        setLED(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.BLINKING);
        delay(1000);

        setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.BLINKING);
        accelerate(500,50);
        delay(2000);
        stop();
    }

    private void accelerate(int targetSpeed, int steps){

        int accelTime = 1000;
        int delay = accelTime / steps;
        int step = targetSpeed / steps;
        int speed = 0;

        for (int i = 0; i<10; i++){
            speed += step;
            forward(speed);
            delay(delay);
        }
        forward(targetSpeed);
    }
}
