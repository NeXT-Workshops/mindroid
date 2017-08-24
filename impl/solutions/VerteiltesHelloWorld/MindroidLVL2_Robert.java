package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;

// Ich bin Robert
public class MindroidLVL2_Robert extends LVL2API {
    @Override
    public void run() {
        while (!wasMsgReceived("Hallo Robert!", "Berta") && !isInterrupted()){
            delay(100);
        }
        if (!isInterrupted()) {
            brickController.drawString("Nachricht von Berta: Hallo Robert ", 1, 1);
            sendMessage("Berta", "Hallo Berta, ich bin es, der Robert!");
        }
    }
}
