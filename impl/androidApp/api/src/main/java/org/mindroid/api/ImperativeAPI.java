package org.mindroid.api;

import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.logging.APILoggerManager;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This Class defines the basic Imperative Implementation API.
 */
public abstract class ImperativeAPI extends BasicAPI implements IInterruptable {
    private static final Logger LOGGER = Logger.getLogger(ImperativeAPI.class.getName());
    static{
        APILoggerManager.getInstance().registerLogger(LOGGER);
    }

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
     * @param sessionRobotCount - The Size of the session you want to start aka the number of robots collaborating in
     *                          this implementation
     *                          must be > 1
     * @param implementationID - The ID of your Implementation. Necessary to run your implementation later on.
     */
    public ImperativeAPI(String implementationID, int sessionRobotCount){
        /**
         * Session Robot Count from this constructor should only be >1
         * Values <= 0 are control commands as defined in class MindroidMessage
         * Uncoupled Sessions should not make use of this constructor, they should only provide String implementationID
         */
        if (sessionRobotCount > 1) {
            this.sessionRobotCount = sessionRobotCount;
        } else {
            LOGGER.log(Level.WARNING, "Implementation "+ implementationID + " tried to set faulty SessionSize");
            this.sessionRobotCount = MindroidMessage.BAD_SESSION_SIZE;
        }
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
