package org.mindroid.android.app.programs.workshop.solutions;

import java.util.Random;
import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class Platooning extends ImperativeWorkshopAPI {

    public Platooning(){
        super("Platooning Dynamic Leader [sol]");
    };
    enum State {
        FAST,
        MED,
        SLOW
    }
    State prevState;

    private final String player_1 = "Robert";
    private final String player_2 = "Berta";
    private final String myID = getRobotID();
    private String colleague;

    //Messages
    private final String leaderMsg = "I AM THE LEADER";

    @Override
    public void run() {

        // find out who i am, so i know who my colleague is
        if(myID.equals(player_1)){
            colleague = player_2;
        }else{
            colleague = player_1;
        }

        while(!isInterrupted()) {

            if (isButtonClicked(Button.ENTER)) {
                sendLogMessage("I am the leader!");
                sendMessage(colleague, leaderMsg);
                driveAsLeader();
            }
            if (hasMessage()) {
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a message: " + msg.getSource().getValue() + ": \"" + msg.getContent() + "\"");
                if (msg.getContent().equals(leaderMsg)) {
                    //Colleague is the leader
                    sendLogMessage("I am NOT the leader!");
                    driveAsFollower();
                }
            }
        }
    }

    private void driveAsLeader(){
        setMotorSpeed(200);
        forward();
        while (!isInterrupted()) {
            delay(50);
        }
        stop();
    }
    private void driveAsFollower(){
        while(!isInterrupted()) {
            clearDisplay();
            float distance = getDistance();
            drawString("Dist: " + distance, Textsize.MEDIUM, 10,50);
            if (prevState != State.FAST && distance > 30f) {
                forward(300);
                prevState = State.FAST;
                setLED(LED_GREEN_ON);
            } else if (prevState != State.SLOW && distance < 20f) {
                forward(100);
                prevState = State.SLOW;
                setLED(LED_RED_ON);
            } else if (prevState != State.MED && distance > 20f && distance < 30f) {
                forward(200);
                prevState = State.MED;
                setLED(LED_YELLOW_ON);
            }
            delay(50);
        }
        stop();

    }
}
