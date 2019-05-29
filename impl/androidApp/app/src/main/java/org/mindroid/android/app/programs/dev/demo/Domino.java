package org.mindroid.android.app.programs.dev.demo;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;

import java.util.Arrays;

public class Domino extends ImperativeWorkshopAPI {


  private final String WALL_MSG = "Wall found!";
  private final String LEADER_MSG = "I am the leader!";

  public Domino(){
    super("Domino", 3);
  }

  @Override
    public void run() {

    boolean leader = false;
    while(!isInterrupted()) {
      if (isButtonClicked(Button.ENTER)) {
        sendBroadcastMessage(LEADER_MSG);
        leader = true;
        break;
      }else if (hasMessage()) {
        MindroidMessage msg = getNextMessage();
        sendLogMessage("I received a message: " + msg.getSource().getValue() + ": \"" + msg.getContent() + "\"");
        if (msg.getContent().equals(LEADER_MSG)) {
          //Colleague is the leader
          sendLogMessage("I am NOT the leader!");
          break;
        }

      }
      delay(10);
    }

    if(!leader){
      waitForMessage();
      turnLeft(180);
    }

    setMotorSpeed(200);
    forward();
    while(!isInterrupted() && getDistance() > 20){
      delay(10);
    }
    stop();
    sendBroadcastMessage(WALL_MSG);

    while(!isInterrupted()) delay(50);

  }

  private void waitForMessage(){
    boolean waitOver = false;
    while (!isInterrupted() && !waitOver) {
      delay(10);
      if (hasMessage()) {
        MindroidMessage msg = getNextMessage();
        if (msg.getContent().equals(WALL_MSG)) {
          sendLogMessage("I received: " + msg.getContent());
          if (getDistance() < 30){
            waitOver = true;
            sendLogMessage("I am the chosen one");
          }
        }
      }
    }

  }
}
