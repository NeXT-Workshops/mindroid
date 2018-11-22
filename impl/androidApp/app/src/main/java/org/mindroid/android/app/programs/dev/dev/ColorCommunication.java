package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;
import org.mindroid.impl.statemachine.properties.Colors;

public class ColorCommunication extends ImperativeWorkshopAPI {


    private final String WALL_MSG = "Wall found!";
    private final String LEADER_MSG = "I am the leader!";

    public ColorCommunication() {
        super("Color Comm", 2);
    }

    @Override
    public void run() {
        clearDisplay();
        setLED(LED_OFF);
        boolean leader = false;
        while (!isInterrupted()) {
            if (isButtonClicked(Button.ENTER)) {
                sendBroadcastMessage(LEADER_MSG);
                leader = true;
                break;
            } else if (hasMessage()) {
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
        if (leader) {
            // LEADER
            while(!isInterrupted()) {
                delay(100);
                String colorString = describeColor(getLeftColor());
                sendBroadcastMessage(colorString);
                clearDisplay();
                drawString(colorString, Textsize.MEDIUM, 20,20);
            }
        } else {
            // FOLLOWER
            String oldColor="";
            while (!isInterrupted()) {
                delay(10);
                if (hasMessage()) {
                    MindroidMessage msg = getNextMessage();
                    String colorString = msg.getContent();
                    if(!oldColor.equals(colorString)) {
                        clearDisplay();
                        drawString(colorString, Textsize.MEDIUM, 20,20);
                        switch (colorString) {
                            case "Green":
                                setLED(LED_GREEN_ON);
                            case "Yellow":
                                setLED(LED_YELLOW_ON);
                            case "Red":
                                setLED(LED_RED_ON);
                            default:
                                setLED(LED_OFF);
                        }
                        oldColor = colorString;
                    }
                }
            }
        }
    }

    private String describeColor(Colors colorValue) {
        switch (colorValue) {
            case GREEN:
                return "Green";
            case YELLOW:
                return "Yellow";
            case RED:
                return "Red";
        }
        return "Other";
    }
}
