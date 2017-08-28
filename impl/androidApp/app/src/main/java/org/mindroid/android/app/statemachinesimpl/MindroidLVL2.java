package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() { 
        clearDisplay();
        drawString("Hello World!", 1, 1);
    }
}