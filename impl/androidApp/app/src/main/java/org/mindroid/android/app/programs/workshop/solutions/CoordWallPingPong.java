package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;

public class CoordWallPingPong extends ImperativeWorkshopAPI {

    private final String player_1 = "Alice";
    private final String player_2 = "Bob";

    //Messages
    private final String leaderMsg = "I AM THE LEADER";
    private final String startPingPongMsg = "YOUR TURN";

    public CoordWallPingPong() {
        super("Coord Wall Ping-Pong Dynamic Leader [sol]");
    }

    @Override
    public void run() {
        String myID = getRobotID();
        String colleague;

        if(myID.equals(player_1)){
            colleague = player_2;
        }else{
            colleague = player_1;
        }

        sendLogMessage("I am " + myID);
        sendLogMessage("My Colleague is " + colleague);

        boolean leaderElectionFinished = false;

        while(!leaderElectionFinished && !isInterrupted()){
            if(isButtonClicked(Button.ENTER)){
                sendLogMessage("I am the leader!");
                //I am the Leader
                sendMessage(colleague, leaderMsg);
                leaderElectionFinished = true;

                //Start doing wall ping pong, start driving
                while(!isInterrupted()) {
                    driveToWallAndTurn();
                    sendMessage(colleague, "Start!");
                    waitForMessage("Weiter!");
                }
            }

            if(hasMessage()){
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a message: "+msg.getSource().getValue()+": \""+msg.getContent()+"\"");
                if(msg.getContent().equals(leaderMsg)){
                    //Colleague is the leader
                    leaderElectionFinished = true;
                    sendLogMessage("I am NOT the leader!");

                    // do wall-pingpong, start with waiting
                    while(!isInterrupted()){
                        waitForMessage("Start!");
                        driveToWallAndTurn();
                        sendMessage(colleague, "Weiter!");
                    }
                }
            }
            delay(50);
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
                if (getNextMessage().getContent().equals(message)){
                    return;
                }
            }
            delay(10);
        }
    }
}
