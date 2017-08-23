package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

/**
 * Created by Torben on 03.05.2017.
 * Easiest programming level.
 */
public class MindroidLVL2 extends LVL2API {


    public MindroidLVL2() {

    }

    @Override
    public void run() {
        
        sendLogMessage("Imperative Implementation is running!");


        //Example Wall-Ping-Pong
        while (true  && !isInterrupted()) {
            forward();
            while (distanceGreaterThan(0.15f) && !isInterrupted()) {
                delay(300);
            }
            stopMotors();
            backward();
            delay(1200);
            setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.BLINKING);
            turnLeft(130);
            setLED(EV3StatusLightColor.OFF, EV3StatusLightInterval.BLINKING);
            stopMotors();

        }
    }
    


}
