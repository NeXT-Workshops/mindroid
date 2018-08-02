package org.mindroid.android.app.programs.workshop.solutions;

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
                sendMessage("Robert", "Hallo Robert!");
        }
    }
}
