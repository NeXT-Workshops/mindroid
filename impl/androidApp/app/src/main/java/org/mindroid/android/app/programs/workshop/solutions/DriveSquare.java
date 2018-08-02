package org.mindroid.android.app.programs.workshop.solutions;
                                                            
import org.mindroid.api.ImperativeWorkshopAPI;

public class DriveSquare extends ImperativeWorkshopAPI {

    public DriveSquare() {
           super("Drive Square [sol]");
    }

    @Override
    public void run() {
      for (int i = 0; i<3 && !isInterrupted(); i++) {
          int angle = 90;
          forward();
          delay(1000);
          turnRight(angle);
          forward();
          delay(1000);
          turnLeft(angle);
          backward();
          delay(1000);
          turnRight(angle);
          backward();
          delay(1000);
          turnLeft(angle);              
      } // end of for
      stop();
    }
}
