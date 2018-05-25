package org.mindroid.android.app.workshopimpl;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.brick.Textsize;

public class Platooning_A extends ImperativeWorkshopAPI {

    public Platooning_A() {
        super("Platooning A");
    }

  @Override
    public void run() {
           forward();
           delay(500);
           forward(100);
           delay(500);
           forward(1000);
           delay(500);
           stop();
    }
}
