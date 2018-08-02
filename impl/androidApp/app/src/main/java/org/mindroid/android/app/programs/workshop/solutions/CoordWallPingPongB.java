package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;

public class CoordWallPingPongB extends ImperativeWorkshopAPI {

    public CoordWallPingPongB() {
        super("Coord Wall Ping-Pong Bob [sol]");
    }

    String colleague = "Alice";

    @Override
    public void run(){
        while(!isInterrupted()){
            waitForMessage("Start!");
            driveToWallAndTurn();
            sendMessage(colleague, "Weiter!");
        }
    }

    private void driveToWallAndTurn(){
        forward(300);
        while (!isInterrupted() && getDistance() > 10f) {
            delay(10);
        }
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
