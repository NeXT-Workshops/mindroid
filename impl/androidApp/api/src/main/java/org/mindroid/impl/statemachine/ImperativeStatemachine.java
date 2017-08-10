package org.mindroid.impl.statemachine;

/**
 * Extends the classical statemachine with a isInterrupt method.
 *
 */
public class ImperativeStatemachine extends Statemachine {

    private boolean isInterrupted = false;


    public void setInterrupted(boolean interrupted) {
        isInterrupted = interrupted;
    }



    public boolean isInterrupted() {
        return isInterrupted;
    }

    public ImperativeStatemachine(String ID) {
        super(ID);
    }


}
