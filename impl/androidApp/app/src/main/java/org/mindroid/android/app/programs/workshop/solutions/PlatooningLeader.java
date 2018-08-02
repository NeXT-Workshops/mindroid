package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class PlatooningLeader extends ImperativeWorkshopAPI {

    public PlatooningLeader() {
        super("Platooning Leader [sol]");
    }

    ;

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (isButtonClicked(Button.ENTER)) {
                setMotorSpeed(200);
                forward();
                while (!isInterrupted()) {
                    delay(50);
                }
                stop();
            }
        }
    }
}
