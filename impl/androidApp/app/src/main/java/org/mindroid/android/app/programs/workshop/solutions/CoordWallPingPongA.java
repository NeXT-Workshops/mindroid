package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;

public class CoordWallPingPongA extends ImperativeWorkshopAPI{

    public CoordWallPingPongA(){
        super("Coord Wall Ping-Pong Alice [sol]");
    }

    String colleague = "Bob";

    @Override
    public void run() {
        while(!isInterrupted()) {
            driveToWallAndTurn();
            sendMessage(colleague, "Start!");
            waitForMessage("Weiter!");
        }
    }

    private void driveToWallAndTurn(){
        forward(300);
        while (!isInterrupted() && getDistance() > 10f) {
            delay(10);
        }
        stop();
        delay(500);
        driveDistanceBackward(10);
        turnLeft(180);
    }

    private void waitForMessage(String message){
        while (!isInterrupted()) {
            if (hasMessage()) {
                if (getNextMessage().getContent().equals(message));
                    return;
            }
            delay(10);
        }
    }

}
