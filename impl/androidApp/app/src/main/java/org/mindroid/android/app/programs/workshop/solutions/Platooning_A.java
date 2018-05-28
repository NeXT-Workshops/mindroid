package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;

public class Platooning_A extends ImperativeWorkshopAPI {

    public Platooning_A() {
        super("Platooning A");
    }

    @Override
    public void run() {
        if (getDistance() > 0.30f) {
            // I am The Leader, send Message and drive
            sendMessage("Eve", "I am the Leader");
            delay(2000);
            while (!isInterrupted()) {
                setMotorSpeed(200);
                forward();

            }
        } else {
            // I am the Follower, wait for Message
            sendLogMessage("I am the Follower, waiting on GO!");
            if (hasMessage()) {
                sendLogMessage("Got msg");
                String msg = getNextMessage().getContent();
                if (msg.equals("I am the Leader")) {
                    sendLogMessage("Got GO!");
                    while (!isInterrupted()) {
                        if (getDistance() > 0.35)
                            forward(250);
                        else if (getDistance() < 0.15f)
                            forward(150);
                        else
                            forward(200);
                    }
                }
            }
        }
    }
}
