package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;

import java.text.SimpleDateFormat;
import java.util.Date;
                                       
public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() { 
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");     
        clearDisplay();
        drawString("Datum: " + formatter.format(new Date()), 1, 1);
    }
}