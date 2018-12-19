package org.mindroid.android.app.programs.workshop.stubs;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;

public class HelloWorld extends ImperativeWorkshopAPI {

    public HelloWorld() {
        super("Hello World");
    }

    @Override
    public void run() {
           clearDisplay(); 
           drawString("Hello World");
    }
}
  
