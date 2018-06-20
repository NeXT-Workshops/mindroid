package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;

public class HelloWorldPingA extends ImperativeWorkshopAPI {

    public HelloWorldPingA() {
        super("Hello World Ping A");
    }

    @Override
    public void run() {
        clearDisplay();
        sendMessage("Robert", "Hallo Robert!");
    }
}
