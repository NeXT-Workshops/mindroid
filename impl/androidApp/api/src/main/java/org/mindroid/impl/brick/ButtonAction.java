package org.mindroid.impl.brick;

public enum ButtonAction {
    PRESSED(1),
    RELEASED(2);

    private int val;

    ButtonAction(int val){
        this.val = val;
    }

    public int getValue(){
        return this.val;
    }
}
