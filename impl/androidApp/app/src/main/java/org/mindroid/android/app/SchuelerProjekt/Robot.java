package org.mindroid.android.app.SchuelerProjekt;

import org.mindroid.api.LVL2API;

public class Robot extends LVL2API {

    @Override
    public void run() {
        
        sendLogMessage("Implementation is running!");

        brickController.drawString("Hello World!");
        
    }
    
}


