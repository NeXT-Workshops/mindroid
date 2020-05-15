package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;

import java.util.Random;

public class Follow extends ImperativeWorkshopAPI {

    public Follow() {
        super("Follower Dynamic Leader [sol]",2);
    }
    enum PlatoonState {
        FAST,
        MED,
        SLOW
    }
    private PlatoonState prevState;

    final int LEADER = 0;
    final int FOLLOWER = 1;
    int role = -1;


    private String colleague;

    private String player_1 = "Alice";
    private String player_2 = "Bob";


    //Messages
    private final String leaderMsg = "I AM THE LEADER";
    private final String turnMsg = "TURN!";
    private final String turnedMsg = " TURNED";
    private String myTurnedMsg;
    private String otherTurnedMsg;

    @Override
    public void run() {

        String myID = getRobotID();
        //String colleague;

        // find out who i am, so i know who my colleague is
        if(myID.equals(player_1)){
            colleague = player_2;
        }else{
            colleague = player_1;
        }

        myTurnedMsg = myID + turnedMsg;
        otherTurnedMsg = colleague + turnedMsg;
        boolean initDone = false;
        // get Roles
        while(!isInterrupted() && !initDone ) {
            if (isButtonClicked(Button.ENTER)) {
                sendLogMessage("I am the leader!");
                sendMessage(colleague, leaderMsg);
                role = LEADER;
                initDone = true;
            }
            if (hasMessage()) {
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a message: " + msg.getSource().getValue() + ": \"" + msg.getContent() + "\"");
                if (msg.getContent().equals(leaderMsg)) {
                    //Colleague is the leader
                    sendLogMessage("I am NOT the leader!");
                    role = FOLLOWER;
                }
                initDone = true;
            }
            delay(10);
        }

        // drive with changing roles
        while(!isInterrupted()){
            if (role == LEADER){
                driveAsLeader();
            }
            if (role == FOLLOWER){
                driveAsFollower();
            }
        }
    }

    private void driveAsLeader(){
        sendLogMessage("Leading the way!");
        setMotorSpeed(200);
        forward();
        delay(5000);
        stop();

        sendMessage(colleague, turnMsg);
        waitForTurn();
        turnRight(180, 100);
        sendMessage(colleague, myTurnedMsg);
        role = FOLLOWER;
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
                    turnLeft(180, 100);
                    sendMessage(colleague, myTurnedMsg);
                    waitForTurn();
                    role = LEADER;
                    return;
                }
            }
            delay(50);
        }
        stop();
    }

    private void keepDistance(){
        float distance = getDistance();
        if (distance > 35f) {
            forward(300);
        } else if (distance < 25f) {
            forward(100);
        } else if (distance > 25f && distance < 35f) {
            forward(200);
        }
    }

    private void waitForTurn(){
        boolean finished = false;
        while (!isInterrupted() && !finished){
            delay(50);
            if(hasMessage()) {
                MindroidMessage msg = getNextMessage();
                if (msg.getContent().equals(otherTurnedMsg)) {
                    sendLogMessage("I received: " + msg.getContent());
                    finished = true;
                }
            }
        }
    }
}
