package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class HelloWorldPing extends ImperativeWorkshopAPI {

    private final String player_1 = "Alice";
    private final String player_2 = "Bob";

    public HelloWorldPing() {
        super("Hello World Ping [sol]");
    }

    @Override
    public void run() {
        clearDisplay();

        String myID = getRobotID();
        String colleague;

        if(myID.equals(player_1)){
            colleague = player_2;
        }else{
            colleague = player_1;
        }

        String helloMsgOut = "Hallo " + colleague + "!";
        String helloMsgIn = "Hallo " + myID + "!";

        while(!isInterrupted()){

            delay(10);

            if(isButtonClicked(Button.ENTER)) {
                sendMessage(colleague, helloMsgOut);
            }

            if (hasMessage()){
                String msg = getNextMessage().getContent();
                if (msg.equals(helloMsgIn)){
                    drawString("Nachricht von " + colleague + " erhalten", Textsize.MEDIUM, 1, 60);
                }
            }
        }
    }
}
