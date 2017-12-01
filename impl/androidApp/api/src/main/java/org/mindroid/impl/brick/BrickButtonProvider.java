package org.mindroid.impl.brick;


import org.mindroid.common.messages.brick.ButtonMessage;

import java.util.HashMap;
import java.util.Map;

public class BrickButtonProvider {

    Map<Button,EV3Button> buttons;

    public static BrickButtonProvider instance = new BrickButtonProvider();

    public static BrickButtonProvider getInstance(){ return instance; }

    private BrickButtonProvider(){
        this.buttons = new HashMap<Button,EV3Button>();
    }

    public EV3Button getButton(Button id){
        return buttons.get(id);
    }

    protected void addButton(EV3Button button){
        buttons.put(button.getID(),button);
    }

    /**
     * Mapps the ID of the button defined in the message project to the ID for the button in the API
     * @param msgsButtonID - button id received from the message
     * @return - Button
     */
    protected static Button getMappedID(int msgsButtonID){
        switch(msgsButtonID){
            case ButtonMessage.ID_BUTTON_ENTER: return Button.ENTER;
            case ButtonMessage.ID_BUTTON_LEFT: return Button.LEFT;
            case ButtonMessage.ID_BUTTON_RIGHT: return Button.RIGHT;
            case ButtonMessage.ID_BUTTON_UP: return Button.UP;
            case ButtonMessage.ID_BUTTON_DOWN: return Button.DOWN;
            default: return null;
        }
    }
}
