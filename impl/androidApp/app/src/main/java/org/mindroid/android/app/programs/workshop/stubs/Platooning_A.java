package org.mindroid.android.app.programs.workshop.stubs;

import org.mindroid.api.ImperativeWorkshopAPI;

public class Platooning_A extends ImperativeWorkshopAPI {
  // Konstruktor nicht ändern!!
    public Platooning_A() {
        super("Platooning A");
    }

  @Override
    public void run() {
           forward();
           delay(500);
           forward(100);
           delay(500);
           forward(1000);
           delay(500);
           stop();
    }
}
