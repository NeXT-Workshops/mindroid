package org.mindroid.android.app.errorhandling;

import android.app.Activity;
import org.mindroid.android.app.acitivites.MainActivity;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.statemachine.NoStartStateException;
import org.mindroid.api.statemachine.exception.DuplicateTransitionException;
import org.mindroid.api.statemachine.exception.NoCurrentStateSetException;
import org.mindroid.api.statemachine.exception.NoSuchStateException;
import org.mindroid.api.statemachine.exception.StateAlreadyExistsException;
import org.mindroid.impl.statemachine.Statemachine;
import org.mindroid.impl.statemachine.StatemachineManager;

/**
 * Created by torben on 08.08.2017.
 */
public class APIErrorHandler extends AbstractErrorHandler{


    private MainActivity mainActivity;

    public APIErrorHandler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleError(Exception e, Class source, String msg) {
        ErrorLogMessage logMessage = new ErrorLogMessage(source.getClass(),e,msg);
        if(source == Statemachine.class){
            handleStatemachineErrors(e,msg);

        }else if(source == StatemachineManager.class){
            handleStatemachineManagerErrors(e,msg);
        }

        System.out.println("[APIErrorHandler:handleError] "+logMessage);
    }

    private void handleStatemachineErrors(Exception e, String msg) {
        if(e instanceof StateAlreadyExistsException){
            mainActivity.showErrorDialog("Duplicate State","The Statemachine "+msg+" can not be executed, because at least two states with the same ID exists. Check your Statemachine!");
        }else if(e instanceof NoSuchStateException){
            mainActivity.showErrorDialog("No Such State","The Statemachine '"+msg+"' tried to use a State, which does not exist! Check your Statemachine-Implementation!");
        }else if(e instanceof DuplicateTransitionException){
            mainActivity.showErrorDialog("Duplicate Transition",msg);
        }else if(e instanceof NoCurrentStateSetException){
            mainActivity.showErrorDialog("No Current State Set Exception","The current state of the Statemachine '"+msg+"' is null!");
        }
    }

    private void handleStatemachineManagerErrors(Exception e, String msg) {
        if(e instanceof NoStartStateException) {
            mainActivity.showErrorDialog("No Start State set","The Statemachine '"+msg+"' does not have a State to start with! Specify the Start-State to execute your Statemachine.");
        }
    }
}
