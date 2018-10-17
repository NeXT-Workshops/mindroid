package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;

public class MessageEcho extends ImperativeWorkshopAPI {

    public MessageEcho() {
        super("Message Echo");
    }

    @Override
    public void run() {
        clearDisplay();
        while(!isInterrupted()){
            while(!hasMessage()){
                delay(50);
            }
            String content = getNextMessage().getContent();
            sendLogMessage("I received a message: " + content);
            drawString("I received a message", Textsize.SMALL, 0, 40);
            drawString(content, Textsize.SMALL, 0, 60);
        }
    }
}
