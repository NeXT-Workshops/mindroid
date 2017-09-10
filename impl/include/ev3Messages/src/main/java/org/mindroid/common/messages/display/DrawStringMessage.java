package org.mindroid.common.messages.display;

/**
 * Message to draw a String on the Display at a given Position
 * @author Torben
 */
public class DrawStringMessage {
    String str;
    int x;
    int y;

    public DrawStringMessage(){}

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    };
}