package org.mindroid.android.app.programs.workshop.solutions;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;

public class HelloWorld extends ImperativeWorkshopAPI {

    public HelloWorld() {
        super("Hello World [sol]");
    }

    @Override
    public void run() {
           clearDisplay(); 
           drawString("Hello World", Textsize.MEDIUM, 10 , 50);   
    }
}
  
