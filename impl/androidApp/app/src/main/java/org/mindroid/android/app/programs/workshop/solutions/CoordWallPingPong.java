package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;

public class CoordWallPingPong extends ImperativeWorkshopAPI {

    private final String player_1 = "Robert";
    private final String player_2 = "Berta";

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
                sendMessage(colleague,leaderMsg);
                leaderElectionFinished = true;

                //Start doing wall ping pong
                runWallPingPong(colleague);
            }

            if(hasMessage()){
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a message: "+msg.getSource().getValue()+": \""+msg.getContent()+"\"");
                if(msg.getContent().equals(leaderMsg)){
                    //Colleague is the leader
                    leaderElectionFinished = true;
                    sendLogMessage("I am NOT the leader!");
                }
            }
            delay(50);
        }

        while(!isInterrupted()){

            if(hasMessage()){
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a message: "+msg.getSource().getValue()+": \""+msg.getContent()+"\"");
                if(msg.getContent().equals(startPingPongMsg)){
                    runWallPingPong(colleague);
                }
            }
            delay(50);
        }
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
        driveDistanceBackward(10f,350);
        turnRight(180,350);
        sendMessage(colleague,startPingPongMsg);
        driveDistanceForward(40f);
        enableFloatMode();
        turnLeft(180,350);
    }
}
