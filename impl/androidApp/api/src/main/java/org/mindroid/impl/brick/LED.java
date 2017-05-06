package org.mindroid.impl.brick;

import org.mindroid.api.LVL2API;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

/**
 * Created by Torbe on 03.05.2017.
 */
public class LED {

    LVL2API api;

    public LED(LVL2API api){
        this.api = api;
    }

    public void red(){
        api.setLED(EV3StatusLightColor.RED, EV3StatusLightInterval.ON);
    }

    public void green(){
        //TODO implement
    }

    public void blinking(){
        //TODO implement
    }

    //TODO and so on..
}
