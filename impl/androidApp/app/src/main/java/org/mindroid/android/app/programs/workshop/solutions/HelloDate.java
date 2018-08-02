package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloDate extends ImperativeWorkshopAPI {

    public HelloDate() {
        super("Hello Date [sol]");
    }

    @Override
    public void run() {
           SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyy");
           clearDisplay();
           drawString("Datum: " + formatter.format(new Date()), Textsize.MEDIUM, 10, 50);
           }
  }
  
