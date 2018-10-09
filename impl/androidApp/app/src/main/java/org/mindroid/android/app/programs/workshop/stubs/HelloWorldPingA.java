package org.mindroid.android.app.programs.workshop.stubs;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class HelloWorldPingA extends ImperativeWorkshopAPI {

    public HelloWorldPingA() {
        super("Hello World Ping Alice", 2);
    }

    @Override
    public void run() {
        clearDisplay();
        while(!isInterrupted()){
            delay(10);
            if(isButtonClicked(Button.ENTER)) {
                sendMessage("Bob", "Hallo Bob!");
                drawString("Nachricht an Bob gesendet!", Textsize.SMALL, 1, 60);
                sendLogMessage("Nachricht an Bob gesendet!");
            }
        }
    }
}
