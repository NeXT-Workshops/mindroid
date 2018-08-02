package org.mindroid.android.app.programs.workshop.stubs;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class PlatooningLeader extends ImperativeWorkshopAPI {

    public PlatooningLeader() {
        super("Platooning");
    }

    @Override
    public void run() {
    }
}
