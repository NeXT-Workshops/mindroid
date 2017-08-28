package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;
                                       
public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() { 
        for (int i = 0; i < 3 && !isInterrupted(); ++i) {
            int angle = 70;
            forward();
            delay(2000);
            turnRight(angle);
            forward();
            delay(2000);
            turnLeft(angle);
            backward();
            delay(2000);
            turnRight(angle);
            backward();
            delay(2000); 
            turnLeft(angle);
        }         
        stopMotors();
    }
}