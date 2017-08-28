package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;

public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() { 
        
        clearDisplay();
        drawString("Hello World!", 1, 1);
        
    }
}