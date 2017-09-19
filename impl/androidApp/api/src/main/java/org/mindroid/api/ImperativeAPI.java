package org.mindroid.api;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
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


    // --------------------- BRICK CONTROLLING METHODS: Display, LED ---------------------


    /**
     * Sets the LED to the given color
     *
     * @param color    the {@link EV3StatusLightColor} to use
     * @param interval the blink interval in milliseconds
     */
    public final void setLED(EV3StatusLightColor color, EV3StatusLightInterval interval) {
        if (!isInterrupted()) {
            brickController.setEV3StatusLight(color, interval);
        }
    }

    /**
     * Displays the given text onto the EV3 display at the given position (xPosition, yPosition).
     *
     * The coordinate (0,0) is at the top-left corner of the display.
     *
     * @param text the text to display
     * @param xPosition the x position
     * @param yPosition the y position
     */
    public void drawString(final String text, final int xPosition, final int yPosition) {
        this.brickController.drawString(text, xPosition, yPosition);
    }

    /**
     * Removes everything from the EV3 display
     */
    public void clearDisplay() {
        this.brickController.clearDisplay();
    }

    // ------ Messaging ------


    public void getMessageBySource(String source){
        //TODO implement
    }

    public boolean hasMessage(){
        //TODO implement
        return false;
    }

    public void getNextMessage(){
        //TODO implement
    }

    /**
     * Send a Message to anothers Robot 'destination'
     * @param destination 'robotID'
     * @param message 'msg to send'
     */
    public void sendMessage(String destination, String message){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(destination,message);
        }else{
            System.out.println("[StatemachineAPI:sendMessage] Tried to send a message, but the Messenger was null");
        }
    }

    /**
     * TODO UNTESTED; WILL PROBABLY NOT WORK
     *
     * Broadcast a message to all Robots in Group.
     * @param message 'message to send'
     */
    public void broadcastMessage(String message){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.BROADCAST,message);
        }else{
            System.out.println("[StatemachineAPI:broadcastMessage] Tried to broadcast a message, but the Messenger was null");
        }
    }

    /**
     * The logmessage will be sent to the message-server and displayed.
     * @param logmessage 'the log message'
     */
    public void sendLogMessage(String logmessage){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,logmessage);
        }else{
            System.out.println("[StatemachineAPI:sendLogMessage] Tried to send a logmessage, but the Messenger was null");
        }
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
