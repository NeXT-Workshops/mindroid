package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;

public class FollowA extends ImperativeWorkshopAPI {

    public FollowA() {
        super("Follower Alice [sol]", 2);
    }

    enum DistState {
        FAST,
        MED,
        SLOW
    }
    private DistState prevState;

    // Robot names
    private String myID = "Alice";
    private String colleague = "Bob";

    // Messages
    private final String turnMsg = "TURN!";
    private final String turnedMsg = " TURNED";
    private String myTurnedMsg = myID + turnedMsg;
    private String otherTurnedMsg = colleague + turnedMsg;


    @Override
    public void run(){
        while(!isInterrupted()) {
            driveAsLeader();
            driveAsFollower();
        }
    }


    private void driveAsLeader(){
        sendLogMessage("Leading the way!");
        forward(200);
        delay(3000);
        stop();

        sendMessage(colleague, turnMsg);
        waitForTurn();
        sendLogMessage("turning...");
        turnRight(180, 100);
        sendMessage(colleague, myTurnedMsg);
    }

    private void driveAsFollower(){
        sendLogMessage("Following!");
        while(!isInterrupted()) {
            keepDistance();
            // check for Turn Message
            if(hasMessage()){
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received: " + msg.getContent());
                if( msg.getContent().equals(turnMsg)){
                    sendLogMessage("turning...");
                    turnLeft(180, 100);
                    sendMessage(colleague, myTurnedMsg);
                    waitForTurn();
                    return;
                }
            }
            delay(50);
        }
        stop();
    }

    private void keepDistance(){
        float distance = getDistance();
        if (prevState != DistState.FAST && distance > 35) {
            forward(300);
            prevState = DistState.FAST;
            setLED(LED_GREEN_ON);
        } else if (prevState != DistState.SLOW && distance < 25) {
            forward(100);
            prevState = DistState.SLOW;
            setLED(LED_RED_ON);
        } else if (prevState != DistState.MED && distance > 25 && distance < 35) {
            forward(200);
            prevState = DistState.MED;
            setLED(LED_YELLOW_ON);
        }
    }

    private void waitForTurn(){
        while (!hasMessage()) delay(50);
        if(hasMessage()){
            MindroidMessage msg = getNextMessage();
            if (msg.getContent().equals(otherTurnedMsg))
                sendLogMessage("I received: " + msg.getContent());
        }
    }
}
