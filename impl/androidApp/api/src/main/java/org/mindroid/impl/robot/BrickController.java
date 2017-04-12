package org.mindroid.impl.robot;

import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.robot.Robot;

/**
 * Created by torben on 02.03.2017.
 */

public class BrickController implements IBrickControl {


    private Robot robot;

    public BrickController(Robot robot){
        this.robot = robot;
    }


    @Override
    public void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval) {
        robot.getBrick().setEV3StatusLight(color,interval);
    }

    @Override
    public void resetEV3StatusLight() {
        robot.getBrick().resetEV3StatusLight();
    }

    @Override
    public void clearDisplay() {
        //TODO Not working correctly yet
        robot.getBrick().getDisplay().clearDisplay();
    }

    @Override
    public void drawString(String str,int posX, int posY) {
        //TODO Not working correctly yet
        robot.getBrick().getDisplay().drawString(str,posX,posY);
    }

    @Override
    public void drawImage(String str) {
        //TODO Impl
    }

}
