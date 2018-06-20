package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class PlatooningFollower extends ImperativeWorkshopAPI {

    public PlatooningFollower(){
        super("Platooning");
    };
    enum State {
        FAST,
        MED,
        SLOW
    }
    State prevState;

    @Override
    public void run() {
      while(!isInterrupted()) {
            clearDisplay();
            float distance = getDistance();
            drawString("Dist: " + distance, Textsize.MEDIUM, 10,50);
            if (prevState != State.FAST && distance > 0.30f) {
                forward(300);
                prevState = State.FAST;
                setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);
            } else if (prevState != State.SLOW && distance < 0.20f) {
                forward(100);
                prevState = State.SLOW;
                setLED(EV3StatusLightColor.RED, EV3StatusLightInterval.ON);
            } else if (prevState != State.MED && distance > 0.20f && distance < 0.30f) {
                forward(200);
                prevState = State.MED;
                setLED(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.ON);
            }
            delay(50);
        }
        stop();
    }
}
