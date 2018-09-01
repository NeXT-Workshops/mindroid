package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class TestMessageServerScroll extends ImperativeWorkshopAPI {


    public TestMessageServerScroll() {
        super("msgServer Scrolling Test");
    }

    @Override
    public void run() {
        int i;
        for (i = 0; i < 23; i++) {
            sendLogMessage("Message sent No." + i);
            delay(50);
        }
        while(!isInterrupted()) {
            delay(50);
            if (isEnterButtonClicked()) {
                sendLogMessage("Message sent No." + i);
                i++;
            }
        }
    }
}
