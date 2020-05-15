package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.statemachine.properties.Colors;

public class LawnMower extends ImperativeWorkshopAPI {

    private static Colors tapeColor = Colors.GREEN;
    private static int turnAngle = 10;

    public LawnMower() {
        super("Lawn Mower [sol]");
    }

  @Override
    public void run() {
        while(!isInterrupted()){
            setMotorSpeed(200);
            forward();
            if(getLeftColor()== tapeColor){

                turnLeft(turnAngle);
            }else if(getRightColor()== tapeColor){

                turnRight(turnAngle);
            }
            delay(50);
        }
    }
}
