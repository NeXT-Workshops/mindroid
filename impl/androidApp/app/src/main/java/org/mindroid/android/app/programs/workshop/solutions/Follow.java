package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;

import java.util.Random;

public class Follow extends ImperativeWorkshopAPI {

    public Follow() {
        super("Follower [sol]");
    }
    enum PlatoonState {
        FAST,
        MED,
        SLOW
    }
    private PlatoonState prevState;

    enum RoleState {
        LEADER,
        FOLLOWER
    }
    private RoleState role;

    enum Direction {
        LEFT,
        RIGHT
    }

    private Random rnd = new Random();
    private String colleague;

    private String player_1 = "Robert";
    private String player_2 = "Berta";


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
                role = RoleState.LEADER;
                initDone = true;
            }
            if (hasMessage()) {
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a message: " + msg.getSource().getValue() + ": \"" + msg.getContent() + "\"");
                if (msg.getContent().equals(leaderMsg)) {
                    //Colleague is the leader
                    sendLogMessage("I am NOT the leader!");
                    role = RoleState.FOLLOWER;
                }
                initDone = true;
            }
        }

        // drive with changing roles
        while(!isInterrupted()){
            switch(role){
                case LEADER:
                    driveAsLeader(); break;
                case FOLLOWER:
                    driveAsFollower(); break;
            }
        }
    }

    private void driveAsLeader(){
        sendLogMessage("Leading the way!");
        // drive at least 3000ms + random(0..3000ms)
        int driveTime = (int) (300 + Math.floor(rnd.nextFloat() * 300));
        int drivenTime = 0;
        sendLogMessage("Driving for " + driveTime * 10 + "ms");
        setMotorSpeed(200);
        forward();
        // drive until interrupted or time is up
        while (!isInterrupted() && drivenTime < driveTime) {
            delay(10);
            drivenTime++;
        }
        stop();

        sendMessage(colleague, turnMsg);
        waitForTurn();
        turn(Direction.RIGHT);
        role = RoleState.FOLLOWER;
    }

    private void driveAsFollower(){
        sendLogMessage("Following!");
        while(!isInterrupted()) {
            float distance = getDistance();
            if (prevState != PlatoonState.FAST && distance > 0.35f) {
                forward(300);
                prevState = PlatoonState.FAST;
                setLED(LED_GREEN_ON);
            } else if (prevState != PlatoonState.SLOW && distance < 0.25f) {
                forward(100);
                prevState = PlatoonState.SLOW;
                setLED(LED_RED_ON);
            } else if (prevState != PlatoonState.MED && distance > 0.25f && distance < 0.35f) {
                forward(200);
                prevState = PlatoonState.MED;
                setLED(LED_YELLOW_ON);
            }

            // check for Turn Message
            if(hasMessage()){
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received: " + msg.getContent());
                if( msg.getContent().equals(turnMsg)){
                    turn(Direction.LEFT);
                    waitForTurn();
                    role = RoleState.LEADER;
                    return;
                }
            }
            delay(50);
        }
        stop();
    }

    private void waitForTurn(){
        while (!hasMessage()) delay(50);
        if(hasMessage()){
            MindroidMessage msg = getNextMessage();
            if (msg.getContent().equals(otherTurnedMsg))
                sendLogMessage("I received: " + msg.getContent());
        }
    }

    private void turn(Direction dir){
        sendLogMessage("turning...");
        switch(dir) {
            case LEFT:
                turnLeft(180, 100);
                break;
            case RIGHT:
                turnRight(180, 100);
                break;
        }
        sendMessage(colleague, myTurnedMsg);
    }
}
