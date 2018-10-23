package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;

public class CoordWallPingPong extends ImperativeWorkshopAPI {

    private final String PLAYER_1 = "Alice";
    private final String PLAYER_2 = "Bob";

    //Messages
    private final String LEADER_MSG = "I AM THE LEADER";
    private final String START_MSG = "START!";
    private final String CONTINUE_MSG = "WEITER!";

    public CoordWallPingPong() {
        super("Coord Wall Ping-Pong Dynamic Leader [sol]", 2);
    }

    @Override
    public void run() {
        String myID = getRobotID();
        String colleague;

        if(myID.equals(PLAYER_1)){
            colleague = PLAYER_2;
        }else{
            colleague = PLAYER_1;
        }

        sendLogMessage("I am " + myID);
        sendLogMessage("My Colleague is " + colleague);

        boolean leaderElectionFinished = false;

        while(!leaderElectionFinished && !isInterrupted()){
            if(isButtonClicked(Button.ENTER)){
                sendLogMessage("I am the leader!");
                //I am the Leader
                sendMessage(colleague, LEADER_MSG);
                leaderElectionFinished = true;

                //Start doing wall ping pong, start driving
                while(!isInterrupted()) {
                    driveToWallAndTurn();
                    sendMessage(colleague, START_MSG);
                    waitForMessage(CONTINUE_MSG);
                }
            }

            if(hasMessage()){
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a message: "+msg.getSource().getValue()+": \""+msg.getContent()+"\"");
                if(msg.getContent().equals(LEADER_MSG)){
                    //Colleague is the leader
                    leaderElectionFinished = true;
                    sendLogMessage("I am NOT the leader!");

                    // do wall-pingpong, start with waiting
                    while(!isInterrupted()){
                        waitForMessage(START_MSG);
                        driveToWallAndTurn();
                        sendMessage(colleague, CONTINUE_MSG);
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
        sendLogMessage("Warte auf: \"" + message + "\"");
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
