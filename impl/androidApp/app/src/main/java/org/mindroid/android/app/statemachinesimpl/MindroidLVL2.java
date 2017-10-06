package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;
import org.mindroid.impl.brick.Textsize;

public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() { 
        clearDisplay();
        drawString("Hello World!", Textsize.MEDIUM, 1, 1);
        
    }
}