package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Textsize;

public class TestMessageAccess extends ImperativeWorkshopAPI {

    public TestMessageAccess(){
        super("Test_Message_Access");
    }

    @Override
    public void run() {
        int counter = 1;

        final String robot_1 = "Bobby";
        final String robot_2 = "Lea";

        if(getRobotID().equals(robot_1)){
            delay(2000);
            sendMessage(robot_2,"This is message number 1");
        }

        while(!isInterrupted()) {
            while (!hasMessage()) {
                delay(50);
            }
            clearDisplay();

            MindroidMessage msg = getNextMessage();

            drawString("I received a message from "+ msg.getSource(), Textsize.SMALL,2,50);
            drawString("Content: "+msg.getContent(), Textsize.SMALL,2,70);

            String msgContent = "This is message number " + counter;
            delay(2500);

            if(getRobotID().equals(robot_1)) {
                sendMessage(robot_2, msgContent);
            }else{
                sendMessage(robot_1,msgContent);
            }

            counter++;

        }
    }
}
