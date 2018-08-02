package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;
import org.mindroid.impl.statemachine.properties.Colors;

public class ColorTest extends ImperativeWorkshopAPI {

    public ColorTest() {
        super("Color Test [sol]");
    }

     @Override
    public void run() {       
        while (!isInterrupted()) { 
            Colors leftColorValue = getLeftColor();
            Colors rightColorValue = getRightColor();

            clearDisplay(); 
            drawString("Colors", Textsize.MEDIUM, 1, 1);
            drawString("L: " + describeColor(leftColorValue), Textsize.MEDIUM, 1, 17);
            drawString("R: " + describeColor(rightColorValue), Textsize.MEDIUM, 1, 33);
            drawString("Distance: " + getDistance(), Textsize.MEDIUM, 1, 51);
            delay(500);
        }
    }

    private static String describeColor(final Colors colorValue) {
        if (colorValue == Colors.NONE)   return "None";
        if (colorValue == Colors.BLACK)  return "Black";
        if (colorValue == Colors.BLUE)   return "Blue";
        if (colorValue == Colors.GREEN)  return "Green";
        if (colorValue == Colors.YELLOW) return "Yellow";
        if (colorValue == Colors.RED)    return "Red";
        if (colorValue == Colors.WHITE)  return "White";
        if (colorValue == Colors.BROWN)  return "Brown";
        return "unknown";
    }
}
