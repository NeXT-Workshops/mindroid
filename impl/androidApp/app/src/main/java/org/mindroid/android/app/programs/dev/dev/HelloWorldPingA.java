package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Button;

public class HelloWorldPingA extends ImperativeWorkshopAPI {

    public HelloWorldPingA() {
        super("Hello World Ping A [sol]");
    }

    @Override
    public void run() {
        clearDisplay();
        while(!isInterrupted()){
            delay(10);
            if(isButtonClicked(Button.ENTER))
                sendLogMessage("Ich sende jetzt eine Nachricht an Robert!");
                sendMessage("Robert", "Hallo Robert!");
        }
    }
}
