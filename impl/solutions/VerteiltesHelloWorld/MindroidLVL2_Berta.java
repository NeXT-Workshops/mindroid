package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;

// Ich bin Berta
public class MindroidLVL2_Berta extends LVL2API {
    @Override
    public void run() {
        sendMessage("Robert", "Hallo Robert!");
    }
}
