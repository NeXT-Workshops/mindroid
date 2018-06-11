package org.mindroid.android.app.programs.workshop.stubs;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloDate extends ImperativeWorkshopAPI {
    // Konstruktor nicht ändern!!
    public HelloDate() {
        super("Hello Date");
    }

    @Override
    public void run() {
        clearDisplay();
        drawString("Hello World", Textsize.MEDIUM, 10 , 50);
    }
}

