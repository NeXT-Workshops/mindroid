package org.mindroid.impl.brick;

/**
 * Defines the Textsize for writing on the Display of the EV3.
 */
public enum Textsize {
    SMALL(1),MEDIUM(2),LARGE(3);


    private int value = 2;

    Textsize(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
