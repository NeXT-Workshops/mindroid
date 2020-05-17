package org.mindroid.android.app.programs.dev.demo;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Button;

public class RemoteMaster extends ImperativeWorkshopAPI {

    public RemoteMaster(){
        super("Remote Master", 5);
    }

    private enum Directions{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    private enum Modes{
        HOLDING("N/A", "N/A"),
        NORMAL("5", "45"),
        DOUBLE("10", "90");
        String dist;
        String angle;

        Modes(String dist, String angle) {
            this.dist = dist;
            this.angle = angle;
        }

        public String getDist() {
            return dist;
        }

        public String getAngle() {
            return angle;
        }
    }

    Modes mode = Modes.NORMAL;


    @Override
    public void run() {
        displayModeInfo();
        while(!isInterrupted()) {
            delay(10);
            if(mode == mode.HOLDING) {
                for (Button button : Button.values()) {
                    if(isEnterButtonClicked()){
                        switchMode();
                    }else if (isButtonPressed(button) && button!=Button.ENTER) {
                        sendMessage("Broadcast", button.name());
                        while(!isButtonReleased(button)) delay(10);
                        sendMessage("Broadcast", "STOP");
                    }
                }
            } else {
                for( Button button : Button.values()){
                    if(isButtonClicked(button)){
                        switch(button){
                            case ENTER:
                                switchMode();
                                break;
                            case LEFT:
                                sendMessage("Broadcast", "LEFT/" + mode.getAngle());
                                break;
                            case RIGHT:
                                sendMessage("Broadcast", "RIGHT/" + mode.getAngle());
                                break;
                            case UP:
                                sendMessage("Broadcast", "UP/" + mode.getDist());
                                break;
                            case DOWN:
                                sendMessage("Broadcast", "DOWN/" + mode.getDist());
                                break;
                        }
                    }
                }
            }
        }
    }

    private void switchMode() {
        switch(mode){
            case HOLDING:
                mode = Modes.NORMAL;
                break;
            case NORMAL:
                mode = Modes.DOUBLE;
                break;
            case DOUBLE:
                mode =  Modes.HOLDING;
                break;
        }
        displayModeInfo();
    }

    private void displayModeInfo() {
        clearDisplay();
        drawString("Mode: " + mode.name(), 3);
        drawString("Up/Down: " + mode.getDist() + "cm", 4);
        drawString("Left/Right: " + mode.getAngle() + "°", 5);
    }
}
