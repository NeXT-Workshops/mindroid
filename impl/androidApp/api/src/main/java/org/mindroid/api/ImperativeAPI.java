package org.mindroid.api;

import org.mindroid.common.messages.server.MindroidMessage;

/**
 * This Class defines the basic Imperative Implementation API.
 */
public abstract class ImperativeAPI extends BasicAPI implements IInterruptable {
    /**
     * To identify the Implementation.
     * ID will be shown in the Apps dropdown.
     */
    private final String implementationID;

    /**
     * Defines number of Robots cooparating in a scenario / Defines lobby size at the server *
     */
    private final int sessionRobotCount;

    /** true, when stopExecution is called **/
    boolean isInterrupted = false;

    @Override
    void accept(AbstractImplVisitor visitor){
        visitor.visit(this);
    }

    /**
     *
     * @param implementationID - The ID of your Implementation. Necessary to run your implementation later on.
     */
    public ImperativeAPI(String implementationID, int sessionRobotCount){
        this.sessionRobotCount = sessionRobotCount;
        this.implementationID = implementationID;
    }

    public ImperativeAPI(String implementationID){
        this.implementationID = implementationID;
        this.sessionRobotCount = MindroidMessage.UNCOUPLED_SESSION;
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
    public int getSessionRobotCount() {
        return sessionRobotCount;
    }

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
