package org.mindroid.api.robot.control;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.brick.Textsize;

/**
 * Created by torben on 02.03.2017.
 */

public interface IBrickControl {

    //Status light operations
    public void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval);
    public void resetEV3StatusLight();

    //Sound operations
    public void setVolume(int volume);
    public void singleBeep();
    public void doubleBeep();
    public void buzz();
    public void beepSequenceDown();
    public void beepSequenceUp();


    //Display operations
    public void clearDisplay();
    public void drawString(String str, Textsize textsize, int posX, int posY);

    public void drawImage(String str);

}
