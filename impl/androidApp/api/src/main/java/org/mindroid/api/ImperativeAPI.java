package org.mindroid.api;

import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.imperative.ImperativeImplManager;

/**
 * This Class defines the basic Imperative Implementation API.
 */
public abstract class ImperativeAPI extends BasicAPI {


    /**
     * To identify the Implementation.
     * ID will be shown in the Apps dropdown.
     */
    private final String implementationID;

    /** true, when stopExecution is called **/
    private boolean isInterrupted = false;

    /**
     * Will be true, when the execution is finished, after starting it.
     * Will be set to false, when an execution got stopped or started.
     * internal var to set isInterrupted correctly.
     */
    private boolean isExecutionFinished = false;

    /**
     *
     * @param implementationID - The ID of your Implementation. Necessary to run your implementation later on.
     */
    public ImperativeAPI(String implementationID){
        this.implementationID = implementationID;
    }

    /**
     * Implementation of the robots behavior.
     * This method will be executed by the imperative Engine.
     *
     * Note: To stop the execution of this implementation properly all loops have to exit when the method isInterrupted {@link #isInterrupted}returns true!
     */
    public abstract void run();

    /**
     * Start the execution of the Implementation
     * Executes run method.
     * Stops all motors, when execution finished/got interrupted.
     * //TODO Try to hide this methods from api user --> extract interface 'Executable" (start,stop, interrupt) --> introduce ExecutableController, register Implementation, use it to start stop etc --> make methods protected.
     */
    public final void start(){
        setExecutionFinished(false);
        isInterrupted = false;
        try{
            run();

        }catch(Exception e){
            //Throw error
            Exception execException = new IIExectuionException(getExecutionErrorMsg(getImplementationID(),e));
            ErrorHandlerManager.getInstance().handleError(execException,ImperativeAPI.class,execException.getMessage());
        }finally{
            //Detects, that the implementation is finished
            setExecutionFinished(true);

            //When run is finished (imperative impl got stopped (interrupted) or just code is done) - reset former state
            getMotorProvider().stopAllMotors();
        }
    }

    /**
     * Sets the isInterrupted field to true to stop the execution of this method
     * //TODO Try to hide this methods from api user
     */
    public final void stopExecution(){
        //Only set interrupted field, when exection has not finished
        if(!isExecutionFinished()) {
            this.isInterrupted = true;
        }
    }


    // -------- Getter and Setter --------
    public final String getImplementationID() {
        return implementationID;
    }

    /**
     * Returns true, when stop {@link #stopExecution()} got called.
     *
     * @return true, when execution should stop
     */
    public final boolean isInterrupted() {
        return isInterrupted;
    }


    // ------ Methods to add some code sugar ------


    /**
     * Returns true, if execution has finished otherwise false
     * @return true, if execution has finished.
     */
    public final boolean isExecutionFinished() {
        return isExecutionFinished;
    }

    private void setExecutionFinished(boolean executionFinished) {
        isExecutionFinished = executionFinished;
    }

    final String getExecutionErrorMsg(String id, Exception e){
        return "An Error occured while trying to execute the Imperative Implementation with the ID "+id+". \n The given errormessage is: "+e.getMessage();
    }

    final class IIExectuionException extends Exception {
        public IIExectuionException(String msg){
            super(msg);
        }
    }
}
