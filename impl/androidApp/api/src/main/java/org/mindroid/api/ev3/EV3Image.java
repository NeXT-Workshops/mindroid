package org.mindroid.api.ev3;

/**
 * In NEPO: "Brille", "Augen", "Tacho"
 */
public interface EV3Image
{
   int getWidth();
   int getHeight();
   boolean getPixelAt(int row, int col);
}
