package org.mindroid.android.app.programs.dev;

import java.util.Random;
import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.statemachine.properties.Colors;

public class LawnMowerLars extends ImperativeWorkshopAPI {
    // Konstruktor nicht ändern!!
    public LawnMowerLars() {
        super("Lawn Mower Lars");
    }

    @Override
    public void run() {
        Random r = new Random (90);
        while (!isInterrupted()) {
            Colors leftColorValue = getLeftColor();
            Colors rightColorValue = getRightColor();

            forward(200);
            delay(100);
            if (getLeftColor() == Colors.WHITE) {
                sendLogMessage("Left Color = WHITE");
                backward(200);
                delay(1500);
                stop();
                int turnDegrees = r.nextInt(90) + 90;
                sendLogMessage("turn right: "+turnDegrees);
                turnRight(turnDegrees);
            }
            else {
                if (getRightColor() == Colors.WHITE) {
                    sendLogMessage("Right Color = WHITE");
                    backward(200);
                    delay(1500);
                    stop();
                    int turnDegrees = r.nextInt(90) + 90;
                    sendLogMessage("turn left: "+turnDegrees);
                    turnLeft(turnDegrees);
                } // end of if-else
            }

        }
    }
}
