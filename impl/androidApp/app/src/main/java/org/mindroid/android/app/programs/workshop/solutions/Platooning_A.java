package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;

public class Platooning_A extends ImperativeWorkshopAPI {

    public Platooning_A() {
        super("Platooning A");
    }

    @Override
    public void run() {
        setMotorSpeed(200);
        forward();
        while (!isInterrupted()) {
            delay(10);
        }
        stop();
    }
}
