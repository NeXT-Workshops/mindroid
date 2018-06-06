package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.LVL2API;

public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() {
        String previousState = "";
        clearDisplay();
        drawString("Parking sensor", 1, 1);
        while (!isInterrupted()) { 
            clearDisplay();
            if(distanceLessThan(0.30f) && distanceGreaterThan(0.15f)) {
                drawString("Hm :-/", 1, 1);      
                if (!previousState.equals("hm")) {
                    setLED(EV3StatusLightColor.YELLOW, 
                           EV3StatusLightInterval.BLINKING);
                }
                previousState = "hm";
            } else if (distanceLessThan(0.15f)) {
                drawString("Oh oh :-O", 1, 1);
                if (!previousState.equals("oh")) {
                    setLED(EV3StatusLightColor.RED, 
                           EV3StatusLightInterval.DOUBLE_BLINKING);
                }
                previousState = "oh";
            } else {                       
                drawString("OK :-)", 1, 1);
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