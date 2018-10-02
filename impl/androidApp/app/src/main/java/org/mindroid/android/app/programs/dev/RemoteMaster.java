package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;

public class RemoteMaster extends ImperativeWorkshopAPI {

    public RemoteMaster(){
        super("Remote Master", 8);
    }

    private enum Directions{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            delay(10);
            if(isUpButtonPressed()){
                sendMessage("BrOaDcAsT", "UP");
                while(!isUpButtonReleased()) delay(10);
                sendMessage("Broadcast", "STOP");

            }else if(isDownButtonPressed()){
                sendMessage("BrOaDcAsT", "DOWN");
                while(!isDownButtonReleased()) delay(10);
                sendMessage("Broadcast", "STOP");

            }else if(isLeftButtonPressed()){
                sendMessage("BrOaDcAsT", "LEFT");
                while(!isLeftButtonReleased()) delay(10);
                sendMessage("Broadcast", "STOP");

            }else if(isRightButtonPressed()) {
                sendMessage("BrOaDcAsT", "RIGHT");
                while(!isRightButtonReleased()) delay(10);
                sendMessage("Broadcast", "STOP");
            }
        }
    }
}
