package org.mindroid.api;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.BrickButtonProvider;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.EV3Button;
import org.mindroid.impl.brick.Textsize;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.robot.*;

import java.util.Random;

/**
 * This API provides the basic methods executable on any robot which do not depend on the implementation style (Statemachine/Imperative).
 */
public abstract class BasicAPI {


    //TODO maybe add Constructor to automatically register Implementation to some component which the app can get access to to get implementation classes

    abstract void accept(AbstractImplVisitor visitor);

    //LED MODES
    public static final int LED_OFF = 0;
    public static final int LED_GREEN_ON = 1;
    public static final int LED_GREEN_BLINKING = 2;
    public static final int LED_GREEN_FAST_BLINKING = 3;
    public static final int LED_YELLOW_ON = 4;
    public static final int LED_YELLOW_BLINKING = 5;
    public static final int LED_YELLOW_FAST_BLINKING = 6;
    public static final int LED_RED_ON = 7;
    public static final int LED_RED_BLINKING = 8;
    public static final int LED_RED_FAST_BLINKING = 9;

    public final int RUNTIME_ID = new Random().nextInt(Integer.MAX_VALUE);

    // --------------------- BRICK CONTROLLING METHODS: Display, LED, Sounds, Buttons ---------------------

    /**
     * Sets the LED to the given color
     *
     * @param color    the {@link EV3StatusLightColor} to use
     * @param interval the blink interval in milliseconds
     */
    public final void setLED(EV3StatusLightColor color, EV3StatusLightInterval interval) {
        getBrickController().setEV3StatusLight(color, interval);
    }

    /**
     * Sets the LED Color
     * 0 - off
     * 1 - green on
     * 2 - green blinking
     * 3 - green double blinking
     * 4 - yellow on
     * 5 - yellow blinking
     * 6 - yellow double blinking
     * 7 - red on
     * 8 - red blinking
     * 9 - red double blinking
     * @param mode 0 - 9
     */
    public final void setLED(int mode){
        switch(mode){
            case LED_OFF: setLEDOff();
                    break;
            case LED_GREEN_ON:
                    setLED(EV3StatusLightColor.GREEN,EV3StatusLightInterval.ON);
                    break;
            case LED_GREEN_BLINKING:
                    setLED(EV3StatusLightColor.GREEN,EV3StatusLightInterval.BLINKING);
                    break;
            case LED_GREEN_FAST_BLINKING:
                    setLED(EV3StatusLightColor.GREEN,EV3StatusLightInterval.DOUBLE_BLINKING);
                    break;
            case LED_YELLOW_ON:
                    setLED(EV3StatusLightColor.YELLOW,EV3StatusLightInterval.ON);
                    break;
            case LED_YELLOW_BLINKING:
                    setLED(EV3StatusLightColor.YELLOW,EV3StatusLightInterval.BLINKING);
                    break;
            case LED_YELLOW_FAST_BLINKING:
                    setLED(EV3StatusLightColor.YELLOW,EV3StatusLightInterval.DOUBLE_BLINKING);
                    break;
            case LED_RED_ON:
                    setLED(EV3StatusLightColor.RED,EV3StatusLightInterval.ON);
                    break;
            case LED_RED_BLINKING:
                    setLED(EV3StatusLightColor.RED,EV3StatusLightInterval.BLINKING);
                    break;
            case LED_RED_FAST_BLINKING:
                    setLED(EV3StatusLightColor.RED,EV3StatusLightInterval.DOUBLE_BLINKING);
                    break;
            default: setLEDOff();
                    break;
        }
    }

    /**
     * Turns off the LED
     *
     */
    public final void setLEDOff(){
        getBrickController().setLEDOff();
    }

    /**
     * Displays the given text onto the EV3 display at the given position (xPosition, yPosition).
     *
     * The coordinate (0,0) is at the top-left corner of the display.
     *
     * @param text the text to display
     * @param textsize Size of the string drawn on the display
     * @param xPosition the x position
     * @param yPosition the y position
     */
    public final void drawString(final String text, final Textsize textsize, final int xPosition, final int yPosition) {
        getBrickController().drawString(text,textsize, xPosition, yPosition);
    }

    /**
     * Removes everything from the EV3 display.
     */
    public final void clearDisplay() {
        getBrickController().clearDisplay();
    }

    /**
     * Clears the Messages from the Messengers Cache.
     */
    public final void clearMessages(){ getRobotController().getMessenger().clearMessageCache(); }

    /**
     * Set the Volume of the Sounds played on the brick.
     * @param volume - 0 to 100
     *
     */
    public final void setSoundVolume(int volume){
        getBrickController().setVolume(Math.max(0,Math.min(100,volume)));
    }

    /**
     *  Plays a buzz sound on the brick.
     */
    public final void playBuzzSound(){
        getBrickController().buzz();
    }

    /**
     * Plays a single beep sound on the brick.
     */
    public final void playSingleBeep(){
        getBrickController().singleBeep();
    }

    /**
     * Plays a double beep sound on the brick.
     */
    public final void playDoubleBeep(){
        getBrickController().doubleBeep();
    }

    /**
     * plays a beep sequence upwards sound on the brick.
     */
    public final void playBeepSequenceUp(){
        getBrickController().beepSequenceUp();
    }

    /**
     * plays a beep sequence downwards sound on the brick.
     */
    public final void playBeepSequenceDown(){
        getBrickController().beepSequenceDown();
    }

    /**
     * Returns the button with the given id;
     * @param buttonID buttonID
     * @return button
     */
    EV3Button getButton(Button buttonID){
        EV3Button button;
        if (BrickButtonProvider.getInstance() != null) {
            if((button = BrickButtonProvider.getInstance().getButton(buttonID)) != null){
                return button;
            }else{
                ErrorHandlerManager.getInstance().handleError(new NullPointerException("Button is null"),BasicAPI.class,"Button is null: "+buttonID);
            }
        }else{
            ErrorHandlerManager.getInstance().handleError(new NullPointerException("BrickButtonProvider is null"),BasicAPI.class,"Button provider is null");
        }
        return null;
    }

    /**
     * Returns true if the button is pressed
     * @param button - button to check
     * @return boolean
     */
    public final boolean isButtonPressed(Button button){
        EV3Button ev3button = getButton(button);
        if(ev3button != null) {
            return ev3button.isPressed();
        }
        return false;

    }

    /**
     * Returns true if the button is released
     * @param button - button to check
     * @return boolean
     */
    public final boolean isButtonReleased(Button button){
        EV3Button ev3button = getButton(button);
        if(ev3button != null) {
            return !getButton(button).isPressed();
        }
        return false;
    }

    /**
     * returns true if the button got clicked
     * @param button - button
     * @return boolean
     */
    public final boolean isButtonClicked(Button button){
        EV3Button ev3button = getButton(button);
        if(ev3button != null) {
            return ev3button.isClicked();
        }
        return false;
    }

    /**
     * Returns true if the enter button is pressed
     * @return boolean
     */
    public final boolean isEnterButtonPressed(){
        return isButtonPressed(Button.ENTER);
    }

    /**
     * Returns true if the enter button is released
     * @return boolean
     */
    public final boolean isEnterButtonReleased(){
        return isButtonReleased(Button.ENTER);
    }


    /**
     * Returns true if the button got clicked
     * @return boolean
     */
    public final boolean isEnterButtonClicked(){
        return isButtonClicked(Button.ENTER);
    }

    /**
     * Returns true if the left button is pressed
     * @return boolean
     */
    public final boolean isLeftButtonPressed(){
        return isButtonPressed(Button.LEFT);
    }

    /**
     * Returns true if the left button is released
     * @return boolean
     */
    public final boolean isLeftButtonReleased(){
        return isButtonReleased(Button.LEFT);
    }


    /**
     * Returns true if the button got clicked
     * @return boolean
     */
    public final boolean isLeftButtonClicked(){
        return isButtonClicked(Button.LEFT);
    }

    /**
     * Returns true if the right button is pressed
     * @return boolean
     */
    public final boolean isRightButtonPressed(){
        return isButtonPressed(Button.RIGHT);
    }

    /**
     * Returns true if the right button is released
     * @return boolean
     */
    public final boolean isRightButtonReleased(){
        return isButtonReleased(Button.RIGHT);
    }


    /**
     * Returns true if the button got clicked
     * @return boolean
     */
    public final boolean isRightButtonClicked(){
        return isButtonClicked(Button.RIGHT);
    }

    /**
     * Returns true if the up button is pressed
     * @return boolean
     */
    public final boolean isUpButtonPressed(){
        return isButtonPressed(Button.UP);
    }

    /**
     * Returns true if the up button is released
     * @return boolean
     */
    public final boolean isUpButtonReleased(){
        return isButtonReleased(Button.UP);
    }


    /**
     * Returns true if the button got clicked
     * @return boolean
     */
    public final boolean isUpButtonClicked(){
        return isButtonClicked(Button.UP);
    }

    /**
     * Returns true if the down button is pressed
     * @return boolean
     */
    public final boolean isDownButtonPressed(){
        return isButtonPressed(Button.DOWN);
    }

    /**
     * Returns true if the down button is released
     * @return boolean
     */
    public final boolean isDownuttonReleased(){
        return isButtonReleased(Button.DOWN);
    }

    /**
     * Returns true if the button got clicked
     * @return boolean
     */
    public final boolean isDownButtonClicked(){
        return isButtonClicked(Button.DOWN);
    }


    // ------ Messaging ------

    /**
     * Returns true if the messenger has a message.
     * @return true if the messenger has a message.
     */
    public final boolean hasMessage(){
        return getRobotController().getMessenger().hasMessage();
    }

    /**
     * Returns the next message in line. The message will then be removed from the messenger and cant be retrieved again!
     * Returns null, when no message is in line. Use {@link #hasMessage()} to check if there is a message in line.
     * @return the next message in line.
     */
    public final MindroidMessage getNextMessage(){
        return getRobotController().getMessenger().getNextMessage();
    }

    /**
     * Send a Message to anothers robot 'destination'
     * @param destination 'robotID'
     * @param message 'msg to send'
     */
    public final void sendMessage(String destination, String message){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(destination, message, RUNTIME_ID);
        }else{
            System.out.println("[StatemachineAPI:sendMessage] Tried to send a message, but the Messenger was null");
        }
    }

    /**
     *
     * Broadcast a message to all robots connectd to the same msg server.
     * @param message 'message to send'
     */
    public final void broadcastMessage(String message){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.BROADCAST, message, RUNTIME_ID);
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
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG, logmessage, RUNTIME_ID);
        }else{
            ErrorHandlerManager.getInstance().handleError(new Exception("[StatemachineAPI:sendLogMessage] Tried to send a logmessage, but the Messenger was null"),BasicAPI.class,"[StatemachineAPI:sendLogMessage] Tried to send a logmessage, but the Messenger was null");
            System.out.println("[StatemachineAPI:sendLogMessage] Tried to send a logmessage, but the Messenger was null");
        }
    }

    // ------ get Some Providers -------

    /**
     * Returns the Motor Provider to access and control the motors.
     * @return Motorprovider
     */
    public final MotorProvider getMotorProvider() {
        return Robot.getRobotController().getMotorProvider();
    }

    /**
     * Returns the Sensor provider to access and control the sensors.
     * @return Sensors
     */
    public final SensorProvider getSensorProvider() {
        return Robot.getRobotController().getSensorProvider();
    }

    /**
     * Returns the Brick controller to run brick operations.
     * @return brickcontroller
     */
    public final IBrickControl getBrickController(){
        return Robot.getRobotController().getBrickController();
    }

    /**
     * Returns the robot Controller
     * @return robotController
     */
    public final RobotController getRobotController(){
        return Robot.getRobotController();
    }


    // -------------------- getting robot ID ---------------------

    /**
     * Returns the robot ID.
     * @return robot id
     */
    public final String getRobotID(){
        return getRobotController().getRobotID();
    }
}
