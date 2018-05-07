package org.mindroid.api;

/**
 * This Class defines the basic Imperative Implementation API.
 */
public abstract class ImperativeAPI extends BasicAPI implements IInterruptable {



    /**
     * To identify the Implementation.
     * ID will be shown in the Apps dropdown.
     */
    private final String implementationID;

    /** true, when stopExecution is called **/
    protected boolean isInterrupted = false;

    @Override
    protected void accept(AbstractImplVisitor visitor){
        visitor.visit(this);
    }

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
     * Resets the state. In this case, stops all motors
     * Normally gets called from the proper Executor if the execution failed somehow.
     */
    protected final void stopAllMotors(){
        getMotorProvider().stopAllMotors();
    }

    protected final IExecutor accept(ExecutorProvider execProvider){
        return execProvider.getExecutor(this);
    }

    // -------- Getter and Setter --------
    public final String getImplementationID() {
        return implementationID;
    }

    /**
     * Returns true, when the execution of this implementation got stopped.
     * Normally this method is called by its implementation Executor.
     *
     * @return true, when execution should stop
     */
    @Override
    public final boolean isInterrupted() {
        return isInterrupted;
    }



}
