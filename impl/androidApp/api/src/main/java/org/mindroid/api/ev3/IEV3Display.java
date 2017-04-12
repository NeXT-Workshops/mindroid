package org.mindroid.api.ev3;

public interface IEV3Display
{

   void drawImage(EV3Image image, int row, int col);

   int getHeight();

   int getWidth();

}
