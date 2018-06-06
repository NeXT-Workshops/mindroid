package org.mindroid.common.messages.brick;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class ButtonMessage implements ILoggable {

    //public final static int ID_BUTTON_ESCAPE = 0;
    public final static int ID_BUTTON_ENTER = 1;

    public final static int ID_BUTTON_LEFT = 2;
    public final static int ID_BUTTON_RIGHT = 3;

    public final static int ID_BUTTON_UP = 4;
    public final static int ID_BUTTON_DOWN = 5;

    public final static int ACTION_BUTTON_PRESSED = 1;
    public final static int ACTION_BUTTON_RELEASED = 2;

    private int buttonID = -1;
    private int buttonAction = -1;

    public ButtonMessage(){

    }

    public ButtonMessage(int buttonID, int buttonAction){
        this.buttonAction = buttonAction;
        this.buttonID = buttonID;
    }

    public int getButtonID() {
        return buttonID;
    }

    public int getButtonAction() {
        return buttonAction;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
