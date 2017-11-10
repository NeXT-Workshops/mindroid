package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;

public class ImpCoordWallPingPong extends ImperativeWorkshopAPI {

    private final String player_1 = "Bobby";
    private final String player_2 = "Lea";

    public ImpCoordWallPingPong() {
        super("ImpCoordWallPingPong");
    }

    @Override
    public void run() {
        String myID = getRobotID();
        String colleague;

        if(myID.equals(player_1)){
            colleague = player_2;

            //I am player one so i start
            runWallPingPong(colleague);
        }else{
            colleague = player_1;
        }

        while(!isInterrupted()) {
            waitForMessageFromColleague(myID);
            runWallPingPong(colleague);
        }
    }

    /**
     * Waits until a message is received
     * @param myID - my ID
     */
    private void waitForMessageFromColleague(String myID){
        do {

            do {
                delay(30);
            } while (!isInterrupted() && !hasMessage());

        }while(!isInterrupted() && !getNextMessage().getDestination().getValue().equals(myID));
    }

    /**
     * Do a wall ping pong
     * @param colleague - my colleagues id
     */
    private void runWallPingPong(String colleague){
        forward(500);

        while(!isInterrupted() && getDistance() > 0.15f){
            delay(50);
        }

        enableFloatMode();

        driveDistanceBackward(5f,350);

        turnRight(180,350);

        sendMessage(colleague,"Start to drive!");

        driveDistanceForward(20f);

        enableFloatMode();

        turnLeft(180,350);
    }
}
