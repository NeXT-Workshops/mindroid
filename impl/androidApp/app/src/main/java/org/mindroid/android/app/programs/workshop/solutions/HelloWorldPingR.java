package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;

public class HelloWorldPingR extends ImperativeWorkshopAPI {

    public HelloWorldPingR() {
        super("Hello World Ping Robert");
    }

    @Override
    public void run() {
        clearDisplay();
        while(!isInterrupted()){
            if (hasMessage()){
                String msg = getNextMessage().getContent();
                if (msg.equals("Hallo Robert!")){
                    drawString("Nachricht von Berta erhalten", Textsize.MEDIUM, 1, 60);
                }
            }
            delay(100);
        };
    }
  }
  
