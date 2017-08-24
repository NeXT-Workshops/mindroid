package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;

// Ich bin Robert
public class MindroidLVL2_Robert extends LVL2API {
    @Override
    public void run() {

        while (!wasMsgReceived("Hallo Robert!", "Berta")){
            delay(100);
        }
		brickController.drawString("Nachricht von Berta: Hallo Robert ");
        sendMessage("Berta", "Hallo Berta, ich bin es, der Robert!");
    }
}
