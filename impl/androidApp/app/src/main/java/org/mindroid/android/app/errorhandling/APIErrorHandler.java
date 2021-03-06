package org.mindroid.android.app.errorhandling;

import org.mindroid.android.app.R;
import org.mindroid.android.app.activities.MainActivity;
import org.mindroid.android.app.fragments.log.GlobalLogger;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.statemachine.NoStartStateException;
import org.mindroid.api.statemachine.exception.DuplicateTransitionException;
import org.mindroid.api.statemachine.exception.NoCurrentStateSetException;
import org.mindroid.api.statemachine.exception.NoSuchStateException;
import org.mindroid.api.statemachine.exception.StateAlreadyExistsException;
import org.mindroid.impl.communication.ServerWorker;
import org.mindroid.impl.robot.RobotCommandCenter;
import org.mindroid.impl.statemachine.Statemachine;
import org.mindroid.impl.statemachine.StatemachineExecutor;
import org.mindroid.impl.util.Throwables;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by torben on 08.08.2017.
 */
public class APIErrorHandler extends AbstractErrorHandler{


    private MainActivity mainActivity;

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    public APIErrorHandler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        GlobalLogger.getInstance().registerLogger(LOGGER);
    }

    @Override
    public void handleError(Exception e, Class source, String msg) {
        ErrorLogMessage logMessage = new ErrorLogMessage(source.getClass(),e,msg);
        LOGGER.log(Level.WARNING,logMessage.toString()+"\n\r Exception Stack Trace: "+Throwables.getStackTrace(e));

        if(source == Statemachine.class){
            handleStatemachineErrors(e,msg);

        }else if(source == StatemachineExecutor.class){
            handleStatemachineManagerErrors(e,msg);
        }else if(source == RobotCommandCenter.class){
            handleRobotCommandCenterErrors(e,msg);
        }else if(source == ServerWorker.class){
            // do nothing on Error Connection closed
        }else{
            //Anything else
            mainActivity.showErrorDialog("Error", source.toString()+":\n"+e.getMessage());
        }

    }

    /**
     * Handles exception received from the Statemachine Manager class
     * @param e exception
     * @param msg - error messages
     */
    private void handleRobotCommandCenterErrors(Exception e, String msg) {
        if(msg.equals(RobotCommandCenter.ERROR_INITIALIZATION)){
            String errorTitle = mainActivity.getResources().getString(R.string.errortext_title_initialization);
            String errorMsg = mainActivity.getResources().getString(R.string.errortext_msg_initialization);
            mainActivity.showErrorDialog(errorTitle,errorMsg);
        }
    }

    /**
     * Handles errors received from statemachine class
     * @param e - exception
     * @param msg - error msg
     */
    private void handleStatemachineErrors(Exception e, String msg) {
        LOGGER.log(Level.WARNING,"StatemachineError: Msg={"+msg+"} , Exception={"+e+"}");
        if(e instanceof StateAlreadyExistsException){
            mainActivity.showErrorDialog("Duplicate State","The Statemachine "+msg+" can not be executed, because at least two states with the same ID exists. Check your Statemachine!");
        }else if(e instanceof NoSuchStateException){
            mainActivity.showErrorDialog("No Such State","The Statemachine '"+msg+"' tried to use a State, which does not exist! Check your Statemachine-Implementation!");
        }else if(e instanceof DuplicateTransitionException){
            mainActivity.showErrorDialog("Duplicate Transition",msg);
        }else if(e instanceof NoCurrentStateSetException){
            mainActivity.showErrorDialog("No Current State Set Exception","The current state of the Statemachine '"+msg+"' is null!");
        }else{
            mainActivity.showErrorDialog("Error", "Statemachine Error appeared: \n" + e.getMessage());
        }
    }

    /**
     * Handles exception received from the Statemachine Manager class
     * @param e - exception
     * @param msg - error msg
     */
    private void handleStatemachineManagerErrors(Exception e, String msg) {
        if(e instanceof NoStartStateException) {
            mainActivity.showErrorDialog("No Start State set","The Statemachine '"+msg+"' does not have a State to start with! Specify the Start-State to execute your Statemachine.");
        }else{
            mainActivity.showErrorDialog("Error","StatemachineExecutor:  Error appeared: \n" +  e.getMessage());
        }
    }
}
