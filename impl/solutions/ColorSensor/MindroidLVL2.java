package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;

public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() {       
        while (!isInterrupted()) { 
            float leftColorValue = getLeftColor();
            float rightColorValue = getRightColor();
            clearDisplay(); 
            drawString("Colors", 1, 1);
            drawString("L: " + describeColor(leftColorValue), 1, 17);
            drawString("R: " + describeColor(rightColorValue), 1, 33);
            delay(100);
        }
    }

    private static String describeColor(final float colorValue) {
        if (colorValue == Color.NONE)   return "None";
        if (colorValue == Color.BLACK)  return "Black";
        if (colorValue == Color.BLUE)   return "Blue";
        if (colorValue == Color.GREEN)  return "Green";
        if (colorValue == Color.YELLOW) return "Yellow";
        if (colorValue == Color.RED)    return "Red";
        if (colorValue == Color.WHITE)  return "White";
        if (colorValue == Color.BROWN)  return "Brown";
        return "unknown";
    }
}