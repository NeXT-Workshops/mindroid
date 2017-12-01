package org.mindroid.api.robot.control;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.brick.Textsize;

/**
 * Created by torben on 02.03.2017.
 */

public interface IBrickControl {

    //Status light operations
     void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval);
     void setLEDOff();

    //Sound operations
     void setVolume(int volume);
     void singleBeep();
     void doubleBeep();
     void buzz();
     void beepSequenceDown();
     void beepSequenceUp();

    //Display operations
     void clearDisplay();
     void drawString(String str, Textsize textsize, int posX, int posY);
     void drawImage(String str);

}
