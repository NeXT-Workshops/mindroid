package org.mindroid.impl.imperative;


import org.mindroid.api.AbstractImperativeImplExecutor;
import org.mindroid.api.IExecutor;
import org.mindroid.api.ImperativeAPI;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.logging.APILoggerManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ImperativeImplExecutor extends AbstractImperativeImplExecutor implements IExecutor {


    /** true if a statemachine is running else false **/
    private boolean isRunning = false;


    private static final Logger LOGGER = Logger.getLogger(ImperativeImplExecutor.class.getName());

    static{
        APILoggerManager.getInstance().registerLogger(LOGGER);
    }

    /**
     * Start the execution of the Implementation
     * Executes run method in a thread.
     * Stops all motors, when execution finished/got interrupted.
     */
    @Override
    public final void start() {


        final ImperativeAPI runningImpl = getRunnable();
        Runnable runImpl = new Runnable() {
            @Override
            public void run() {
                setExecutionFinished(false);
                setInterrupted(false);
                try {
                    isRunning = true;
                    runningImpl.run();

                } catch (Exception e) {
                    //Throw error
                    Exception execException = new IIExecutionException(getExecutionErrorMsg(runningImpl.getImplementationID(), e));
                    ErrorHandlerManager.getInstance().handleError(execException, ImperativeAPI.class, execException.getMessage());
                } finally {
                    //Detects, that the implementation is finished
                    setExecutionFinished(true);
                    isRunning = false;

                    //When run is finished (imperative impl got stopped (interrupted) or just code is done) - stopAllMotors former state
                    stopAllMotors(runningImpl);

                }
            }
        };
        LOGGER.log(Level.INFO,"Starting an Implementation: ID="+runningImpl.getImplementationID());
        new Thread(runImpl).start();
    }

    @Override
    public void stop() {
        LOGGER.log(Level.INFO,"Stopping the currently running implementation");

        //Only set interrupted field, when exection has not finished
        if (!isExecutionFinished()) {
            setInterrupted(true);
            setExecutionFinished(true);
            isRunning = false;
        }

    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Will be true, when the execution is finished, after starting it.
     * Will be set to false, when an execution got stopped or started.
     * internal var to set isInterrupted correctly.
     */
    private boolean isExecutionFinished = false;


    /**
     * Returns true, if execution has finished otherwise false
     *
     * @return true, if execution has finished.
     */
    public final boolean isExecutionFinished() {
        return isExecutionFinished;
    }

    private void setExecutionFinished(boolean executionFinished) {
        isExecutionFinished = executionFinished;
    }

    public void setImplementation(ImperativeAPI runnable) {
        setRunnable(runnable);
        LOGGER.log(Level.INFO,"Implementation Set: ID="+runnable.getImplementationID());
    }

    // ------ Methods to add some code sugar ------

    final String getExecutionErrorMsg(String id, Exception e) {
        return "An Error occured while trying to execute the Imperative Implementation with the ID " + id + ". \n The given errormessage is: " + e.getMessage();
    }

    final class IIExecutionException extends Exception {
        IIExecutionException(String msg) {
            super(msg);
        }
    }


}
