package org.mindroid.common.messages.brick;

public class ButtonMessageFactory {

    /**
     * Returns a ButtonMessage with the given parameter
     * @param buttonID - buttonid
     * @param buttonAction - pressed or released
     * @return ButtonMessage
     */
    public static ButtonMessage createButtonMessage(int buttonID, int buttonAction){
        return new ButtonMessage(buttonID,buttonAction);
    }
}
