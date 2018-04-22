package org.mindroid.android.app.workshopSolutions;

import org.mindroid.api.ImperativeWorkshopAPI;

public class HelloWorldPingB extends ImperativeWorkshopAPI {

    public HelloWorldPingB() {
        super("Hello World Ping Berta");
    }

    @Override
    public void run() {
        clearDisplay();
        sendMessage("Robert", "Hallo Robert!");
    }
}
