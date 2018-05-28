package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.brick.Textsize;

public class Platooning_B extends ImperativeWorkshopAPI {

    enum State {
        FAST,
        MED,
        SLOW
    }

    State prevState;


    public Platooning_B() {
        super("Platooning B");
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            float distance = getDistance();
            clearDisplay();
            if (prevState != State.FAST && distance > 0.35) {
                forward(250);
                prevState = State.FAST;
                setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);
            }
            else if (prevState != State.SLOW && distance < 0.15f) {
                forward(150);
                prevState = State.SLOW;
                setLED(EV3StatusLightColor.RED, EV3StatusLightInterval.ON);
            }
            else if (prevState != State.MED) {
                forward(200);
                prevState = State.MED;
                setLED(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.ON);
            }
            delay(500);
        }
        stop();
    }
}
