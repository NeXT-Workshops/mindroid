package org.mindroid.api;

/**
 * This Class defines the Imperative API.
 */
public abstract class ImperativeAPI extends BasicAPI {


    /**
     * To identify the Implementation.
     * ID will be shown in the Apps dropdown.
     */
    private String implementationID = "";
    private boolean isInterrupted = false;

    /**
     *
     * @param implementationID - The ID of your Implementation. Necessary to run your implementation later on.
     */
    public ImperativeAPI(String implementationID){
        this.implementationID = implementationID;
    }

    /**
     * Implementation of the Robots behavior.
     * This method will be executed by the imperative Engine.
     *
     * Note: To stop the execution of this implementation properly all loops have to exit when the method isInterrupted {@link #isInterrupted}returns true!
     */
    public abstract void run();

    /**
     * Start the execution of the Implementation
     * Executes run method.
     * Stops all motors, when execution finished.
     */
    public void start(){
        run();
        getMotorProvider().stopAllMotors();
    }

    /**
     * Sets the isInterrupted field to true to stop the execution of this method
     */
    public void stop(){
        this.isInterrupted = true;
    }


    // -------- Getter and Setter --------
    public String getImplementationID() {
        return implementationID;
    }

    public void setImplementationID(String implementationID) {
        this.implementationID = implementationID;
    }

    /**
     * Returns true, when stop {@link #stop()} got called.
     *
     * @return true, when execution should stop
     */
    public boolean isInterrupted() {
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

}
