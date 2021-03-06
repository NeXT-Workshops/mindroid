package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;

public class TestBroadcasting extends ImperativeWorkshopAPI {

    public TestBroadcasting() {
        super("TestBroadcasting");
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            if(isButtonClicked(Button.ENTER)){
                sendBroadcastMessage("Hello There this is a broadcast message :) ");
            }

            if(hasMessage()){
                MindroidMessage msg = getNextMessage();
                sendLogMessage("I received a msg: "+msg);
            }

            delay(20);
        }
    }
}
