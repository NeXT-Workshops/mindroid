package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;

public class HelloWorldPingB extends ImperativeWorkshopAPI {

    public HelloWorldPingB() {
        super("Hello World Ping B [sol]");
    }

    @Override
    public void run() {
        clearDisplay();
        while(!isInterrupted()){
            if (hasMessage()){
                String msg = getNextMessage().getContent();
                if (msg.equals("Hallo Robert!")){
                    sendLogMessage("Ich sende jetzt eine Nachricht an Berta!");
                    drawString("Nachricht von Berta erhalten", Textsize.MEDIUM, 1, 60);
                }
            }
            delay(100);
        };
    }
  }
  
