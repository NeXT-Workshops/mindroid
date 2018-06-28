package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class AccelerationTest extends ImperativeWorkshopAPI {

    public AccelerationTest() {
        super("Acceeration Test");
    }

    @Override
    public void run() {
        accelerate(200);
        stop();
        accelerate(500);
        stop();
    }

    private void accelerate(int targetSpeed){
        int step = targetSpeed/10;
        int speed = 0;
        for (int i = 0; i<10; i++){
            speed += step;
            forward(speed);
            delay(10);
        }
        forward(targetSpeed);
    }
}
