package org.mindroid.api;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.robot.*;

/**
 * This API provides the basic methods executable on any robot which do not depend on the implementation style (Statemachine/Imperative).
 */
public abstract class BasicAPI {

    private RobotController robotController = Robot.getRobotController();
    private MotorProvider motorProvider = robotController.getMotorProvider();
    private SensorProvider sensorProvider = robotController.getSensorProvider();
    private BrickController brickController = robotController.getBrickController();


    // -------------------- getting Robot ID ---------------------

    /**
     * Returns the Robot ID.
     * @return robot id
     */
    public final String getRobotID(){
        return Robot.getRobotController().getRobotID();
    }

    // --------------------- BRICK CONTROLLING METHODS: Display, LED, Sounds ---------------------

    /**
     * Sets the LED to the given color
     *
     * @param color    the {@link EV3StatusLightColor} to use
     * @param interval the blink interval in milliseconds
     */
    public final void setLED(EV3StatusLightColor color, EV3StatusLightInterval interval) {
        brickController.setEV3StatusLight(color, interval);
    }

    /**
     * Resets the LED on the Brick.
     *
     */
    public final void resetLED(){
        brickController.resetEV3StatusLight();
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
    public final void drawString(final String text, final int xPosition, final int yPosition) {
        this.brickController.drawString(text, xPosition, yPosition);
    }

    /**
     * Removes everything from the EV3 display
     */
    public final void clearDisplay() {
        this.brickController.clearDisplay();
    }

    /**
     * Set the Volume of the Sounds played on the brick.
     * @param volume - 0 to 100
     *
     */
    public final void setSoundVolume(int volume){
        brickController.setVolume(Math.max(0,Math.min(100,volume)));
    }

    /**
     *  Plays a buzz sound on the brick.
     */
    public final void playBuzzSound(){
        brickController.buzz();
    }

    /**
     * Plays a single beep sound on the brick.
     */
    public final void playSingleBeep(){
        brickController.singleBeep();
    }

    /**
     * Plays a double beep sound on the brick.
     */
    public final void playDoubleBeep(){
        brickController.doubleBeep();
    }

    /**
     * plays a beep sequence upwards sound on the brick.
     */
    public final void playBeepSequenceUp(){
        brickController.beepSequenceUp();
    }

    /**
     * plays a beep sequence downwards sound on the brick.
     */
    public final void playBeepSequenceDown(){
        brickController.beepSequenceDown();
    }


    // ------ Messaging ------

    public final boolean hasMessage(){
        return robotController.getMessenger().hasMessage();
    }

    public final MindroidMessage getNextMessage(){
        return robotController.getMessenger().getNextMessage();
    }

    /**
     * Send a Message to anothers Robot 'destination'
     * @param destination 'robotID'
     * @param message 'msg to send'
     */
    public final void sendMessage(String destination, String message){
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
    public final void broadcastMessage(String message){
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
    public final void sendLogMessage(String logmessage){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,logmessage);
        }else{
            System.out.println("[StatemachineAPI:sendLogMessage] Tried to send a logmessage, but the Messenger was null");
        }
    }

    // ------ get Some Providers -------

    /**
     * Returns the Motor Provider to access and control the motors.
     * @return Motorprovider
     */
    public final MotorProvider getMotorProvider() {
        return motorProvider;
    }

    /**
     * Returns the Sensor provider to access and control the sensors.
     * @return Sensors
     */
    public final SensorProvider getSensorProvider() {
        return sensorProvider;
    }



}
