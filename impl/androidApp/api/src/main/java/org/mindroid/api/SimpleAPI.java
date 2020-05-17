package org.mindroid.api;

import org.mindroid.common.messages.server.MindroidMessage;

public abstract class SimpleAPI extends ImperativeWorkshopAPI {
    public SimpleAPI(String implementationID, int sessionRobotCount) {
        super(implementationID, sessionRobotCount);
    }

    public SimpleAPI(String implementationID) {
        super(implementationID);
    }

    public void driveToWall(){
        do {
            forward(200);
            while (getDistance() > 15f && !isInterrupted()) {
                delay(25);
            }
            stop();
        }while(!isInterrupted());
    }

    public void driveToWallAndTurn(){
        do {
            forward(200);
            while (getDistance() > 15f && !isInterrupted()) {
                delay(25);
            }
            stop();
            driveDistanceBackward(10);
            turnLeft(180);
        }while(!isInterrupted());
    }

    public void turnAround(){
        turnLeft(180);
    }

    public void displayDistance(){
        drawString("Distance: " + getDistance());
    }

    /**
     * Show Colors from both Sensors on the display
     */
    public void displayColors(){
        drawString("Color Left: " + getLeftColor(), 3);
        drawString("Color Left: " + getRightColor(), 3);
    }

    /**
     * Blocking method, waits for any incoming Message
     */
    public void waitForMessage(){
        while (!hasMessage()) delay(50);
        if(hasMessage()){
            MindroidMessage msg = getNextMessage();
            sendLogMessage("I received: " + msg.getContent());
        }
    }

    /**
     * Blocking method, waits for Message with specific content
     * @param waitingMessage the message to wait for
     */
    public void waitForMessage(String waitingMessage){
        while (!hasMessage()) delay(50);
        if(hasMessage()){
            MindroidMessage msg = getNextMessage();
            if (msg.getContent().equals(waitingMessage))
                sendLogMessage("I received: " + msg.getContent());
        }
    }
}
