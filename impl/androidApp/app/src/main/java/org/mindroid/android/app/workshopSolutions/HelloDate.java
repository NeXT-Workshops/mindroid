package org.mindroid.android.app.workshopSolutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloDate extends ImperativeWorkshopAPI {

    public HelloDate() {
        super("Hello Date");
    }

    @Override
    public void run() {
           SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyy");
           clearDisplay();
           drawString("Datum: " + formatter.format(new Date()), Textsize.SMALL, 1, 1);   
           }
  }
  
