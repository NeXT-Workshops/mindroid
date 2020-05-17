package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.statemachine.properties.Colors;

public class LineFollow extends ImperativeWorkshopAPI {

    private static Colors tapeColor = Colors.GREEN;
    private static int turnAngle = 10;

    public LineFollow() {
<<<<<<< HEAD
        super("Lawn Mower [sol]");
    }

  @Override
=======
        super("Line Follower[sol]");
    }

    @Override
>>>>>>> e2d262cbf62a8ea53d614c1e79f4fb54bbe34391
    public void run() {
        while(!isInterrupted()){
            setMotorSpeed(200);
            forward();
            if(getLeftColor()== tapeColor){
<<<<<<< HEAD
                turnLeft(turnAngle);
            }else if(getRightColor()== tapeColor){
=======

                turnLeft(turnAngle);
            }else if(getRightColor()== tapeColor){

>>>>>>> e2d262cbf62a8ea53d614c1e79f4fb54bbe34391
                turnRight(turnAngle);
            }
            delay(50);
        }
    }
}
