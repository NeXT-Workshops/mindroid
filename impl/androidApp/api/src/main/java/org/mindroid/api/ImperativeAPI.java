package org.mindroid.api;

import org.mindroid.impl.robot.*;

public abstract class ImperativeAPI {

    public RobotController robotController = Robot.getRobotController();
    public MotorProvider motorProvider = robotController.getMotorProvider();
    public SensorProvider sensorProvider = robotController.getSensorProvider();
    public BrickController brickController = robotController.getBrickController();

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


}
