package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class SimpleForward extends ImperativeWorkshopAPI {

    public SimpleForward() {
        super("SimpleForward");
    }

    @Override
    public void run() {

        driveDistanceForward(30f);

    }
}
