package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;

public class MessageTest extends ImperativeWorkshopAPI {

    private final String player_1 = "Bobby";
    private final String player_2 = "Lea";

    public MessageTest() {
        super("MessageTest");
    }

    @Override
    public void run() {
        String myID = getRobotID();
        String colleague;

        if(myID.equals(player_1)){
            colleague = player_2;
        }else{
            colleague = player_1;
        }


        sendLogMessage("Implementation Started. My id is "+getRobotID());
        delay(5000);
        sendMessage(colleague,"Hi colleague, my name is "+getRobotID());

        while(!hasMessage()){
            delay(50);
        }
        sendLogMessage("I received a message: "+getNextMessage().getContent());
    }
}
