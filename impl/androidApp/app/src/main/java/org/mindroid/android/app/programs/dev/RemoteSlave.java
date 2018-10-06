package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;

public class RemoteSlave extends ImperativeWorkshopAPI {

    public RemoteSlave() {
        super("Remote Slave", 2);
    }

    @Override
    public void run() {
        setMotorSpeed(250);
        while (!isInterrupted()) {
            delay(10);
            if (hasMessage()) {
                MindroidMessage msg = getNextMessage();
                switch (msg.getContent()) {
                    case "UP":
                        forward();
                        break;
                    case "DOWN":
                        backward();
                        break;
                    case "LEFT":
                        turnLeft();
                        break;
                    case "RIGHT":
                        turnRight();
                        break;
                    case "STOP":
                        stop();
                        break;
                    default:
                        break;

                }
            }
        }
    }
}
