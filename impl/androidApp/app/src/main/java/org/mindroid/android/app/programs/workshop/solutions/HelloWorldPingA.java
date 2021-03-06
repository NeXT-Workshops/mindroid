package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class HelloWorldPingA extends ImperativeWorkshopAPI {

    public HelloWorldPingA() {
        super("Hello World Ping Alice [sol]", 2);
    }

    @Override
    public void run() {
        clearDisplay();
        while(!isInterrupted()){
            delay(10);
            if(isButtonClicked(Button.ENTER)) {
                sendMessage("Bob", "Hallo Bob!");
                drawString("Nachricht an Bob gesendet!");
                sendLogMessage("Nachricht an Bob gesendet!");
            }
        }
    }
}
