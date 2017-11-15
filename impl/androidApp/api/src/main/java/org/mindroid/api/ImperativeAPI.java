package org.mindroid.api;

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

        run();

        //Detects, that the implementation is finished
        setExecutionFinished(true);

        //When run is finished (imperative impl got stopped (interrupted) or just code is done) - reset former state
        getMotorProvider().stopAllMotors();

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
     * This method waits until the given amount of time has passed.
     * This method is blocking.
     *
     * @param milliseconds the time in milliseconds
     */
    public final void delay(long milliseconds) {
        if (!isInterrupted()) {
            try {
                Thread.sleep(milliseconds);
            } catch (final InterruptedException e) {
                // Ignore
            }
        }
    }

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
}
