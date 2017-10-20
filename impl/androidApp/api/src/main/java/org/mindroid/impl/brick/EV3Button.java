package org.mindroid.impl.brick;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class EV3Button{


    private final Button buttonID;

    //True if button is pressed
    private boolean isPressed = false;

    //True if button got clicked (Press And Released)
    private boolean isClicked = false;

    public EV3Button(Button ID){
        this.buttonID = ID;
    }

    protected void setIsPressed(boolean isPressed){
       if(!isPressed && this.isPressed){
           this.isClicked = true;
       }

       this.isPressed = isPressed;
    }

    protected void setIsClicked(boolean isPressed){
        this.isClicked = isClicked;
    }

    /**
     * Returns if the button is Pressed
     * @return - true if is pressed
     */
    public boolean isPressed(){
        return this.isPressed;
    }

    /**
     *  Returns true if the button got pressed and released.
     *  Resets the isClicked = false (Should behave like an event)
     * @return true if button got clicked
     */
    public boolean isClicked(){
        boolean isClicked = this.isClicked;
        this.isClicked = false;

        return isClicked;
    }

    public Button getID() {
        return buttonID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EV3Button ev3Button = (EV3Button) o;

        return buttonID == ev3Button.buttonID;
    }

    @Override
    public int hashCode() {
        return buttonID != null ? buttonID.hashCode() : 0;
    }
}
