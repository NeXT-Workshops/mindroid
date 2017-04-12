package org.mindroid.api.ev3;

public interface EV3StatusLightEnabled
{
   void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval);

   void resetEV3StatusLight();
}
