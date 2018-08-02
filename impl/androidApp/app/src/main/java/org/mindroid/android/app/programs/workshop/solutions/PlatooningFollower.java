package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class PlatooningFollower extends ImperativeWorkshopAPI {

    public PlatooningFollower(){
        super("Platooning Follower [sol]l");
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
            if (prevState != State.FAST && distance > 30f) {
                forward(300);
                prevState = State.FAST;
                setLED(LED_GREEN_ON);
            } else if (prevState != State.SLOW && distance < 20f) {
                forward(100);
                prevState = State.SLOW;
                setLED(LED_RED_ON);
            } else if (prevState != State.MED && distance > 20f && distance < 30f) {
                forward(200);
                prevState = State.MED;
                setLED(LED_YELLOW_ON);
            }
            delay(50);
        }
        stop();
    }
}
