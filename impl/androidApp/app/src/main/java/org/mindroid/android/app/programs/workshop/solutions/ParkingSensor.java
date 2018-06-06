package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.brick.Textsize;

public class ParkingSensor extends ImperativeWorkshopAPI {

    public ParkingSensor() {
        super("Parking Sensor");
    }

  @Override
    public void run() {
        String previousState = "";
        clearDisplay();
        drawString("Parking sensor", Textsize.MEDIUM, 10, 10);
        while (!isInterrupted()) { 
            clearDisplay();
            if(getDistance() < 0.30f && getDistance() > 0.15f) {
                drawString("Hm :-/", Textsize.MEDIUM, 10, 10);      
                if (!previousState.equals("hm")) {
                    setLED(EV3StatusLightColor.YELLOW, 
                           EV3StatusLightInterval.BLINKING);
                }
                previousState = "hm";
            } else if (getDistance() < 0.15f) {
                drawString("Oh oh :-O", Textsize.MEDIUM, 10, 10);
                if (!previousState.equals("oh")) {
                    setLED(EV3StatusLightColor.RED, 
                           EV3StatusLightInterval.DOUBLE_BLINKING);
                }
                previousState = "oh";
            } else {                       
                drawString("OK :-)", Textsize.MEDIUM, 10, 10);
                if (!previousState.equals("ok")) {
                    setLED(EV3StatusLightColor.GREEN, 
                           EV3StatusLightInterval.ON);                   
                }
                previousState = "ok";
            }
            delay(100);
        }
    }
}
