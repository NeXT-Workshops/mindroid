package org.mindroid.impl.brick;

import org.mindroid.api.LVL2API;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.robot.BrickController;

/**
 * Created by Torbe on 03.05.2017.
 */
public class LED {

    BrickController brickController;

    public LED(BrickController brickController){
        this.brickController = brickController;
    }

    public void red(){
        brickController.setEV3StatusLight(EV3StatusLightColor.RED, EV3StatusLightInterval.ON);
    }

    public void green(){
        //TODO implement
    }

    public void blinking(){
        //TODO implement
    }

    //TODO and so on..
}
