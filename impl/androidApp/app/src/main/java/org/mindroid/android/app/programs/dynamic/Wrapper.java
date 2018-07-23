package org.mindroid.android.app.programs.dynamic;

import org.mindroid.api.ImperativeWorkshopAPI;

public class Wrapper extends ImperativeWorkshopAPI {
    /**
     * @param implementationID The ID of your Implementation. Necessary to run your implementation later on.
     */
    public Wrapper(String implementationID) {
        super(implementationID);
    }

    @Override
    public void run() {

    }
}
