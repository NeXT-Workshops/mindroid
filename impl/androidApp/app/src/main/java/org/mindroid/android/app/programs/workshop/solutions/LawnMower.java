package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.statemachine.properties.Colors;

public class LawnMower extends ImperativeWorkshopAPI {

    private static Colors tapeColor = Colors.GREEN;
    private static float backDist = 10.0f;

    public LawnMower() {
        super("Lawn Mower [sol]");
    }

  @Override
    public void run() {
        while(!isInterrupted()){
            setMotorSpeed(200);
            forward();
            if(getLeftColor()== tapeColor && getRightColor() == tapeColor){
                driveDistanceBackward(backDist);
                turnRight(135);
            }else if(getLeftColor()== tapeColor){
                driveDistanceBackward(backDist);
                turnRight(90);
            }else if(getRightColor()== tapeColor){
                driveDistanceBackward(backDist);
                turnLeft(90);
            }
            delay(50);
        }
    }
}
