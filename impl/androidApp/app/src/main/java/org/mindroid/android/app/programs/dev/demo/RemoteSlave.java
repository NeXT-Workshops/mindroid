package org.mindroid.android.app.programs.dev.demo;

import android.view.ViewDebug;
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
                String in = msg.getContent();
                String[] splitted = in.split("/");
                if (splitted.length<2) {
                    switch (splitted[0]) {
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
                }else {
                    int value = Integer.valueOf(splitted[1]);
                    switch (splitted[0]) {
                        case "UP":
                            driveDistanceForward((float) value);
                            break;
                        case "DOWN":
                            driveDistanceBackward((float)value);
                            break;
                        case "LEFT":
                            turnLeft(value);
                            break;
                        case "RIGHT":
                            turnRight(value);
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
}
