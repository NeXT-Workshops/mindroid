package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.statemachine.properties.Colors;

public class LawnMower extends ImperativeWorkshopAPI {

    public LawnMower() {
        super("Lawn Mower");
    }

  @Override
    public void run() {
        while(!isInterrupted()){
            setMotorSpeed(200);
            forward();
            if(getLeftColor()== Colors.RED && getRightColor() == Colors.RED){
                driveDistanceBackward(10.0f);
                turnRight(135);
            }else if(getLeftColor()== Colors.RED){
                driveDistanceBackward(10.0f);
                turnRight(90);
            }else if(getRightColor()== Colors.RED){
                driveDistanceBackward(10.0f);
                turnRight(90);
            }
            delay(50);
        }
    }
}
