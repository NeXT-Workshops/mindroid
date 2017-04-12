package org.mindroid.api.robot.control;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

/**
 * Created by torben on 02.03.2017.
 */

public interface IBrickControl {

    public void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval);
    public void resetEV3StatusLight();

    public void clearDisplay();
    public void drawString(String str,int posX, int posY);
    public void drawImage(String str);

}
