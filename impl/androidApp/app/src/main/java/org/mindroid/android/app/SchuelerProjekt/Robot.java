package org.mindroid.android.app.SchuelerProjekt;

import org.mindroid.api.LVL2API;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;

public class Robot extends LVL2API {

    public Robot() throws StateAlreadyExists {
      
    }

    @Override
    public void run() {
        brickController.drawString("Hello World!", 1, 1);        
    }
    
}



