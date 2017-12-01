package org.mindroid.impl.robot;

import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.impl.brick.BrickButtonProvider;
import org.mindroid.impl.brick.Textsize;

/**
 * Created by torben on 02.03.2017.
 */

public class BrickController implements IBrickControl {


    private Robot robot;

    public BrickController(Robot robot){
        this.robot = robot;
    }

    // ------------- STATUS LIGHT OPERATIONS -------------

    /**
     * Set the LED of the Brick
     * @param color the wanted color
     * @param interval the blink interval of the led
     */
    @Override
    public void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval) {
        robot.getBrick().setEV3StatusLight(color, interval);
    }

    /**
     * Reset the LED- of the Brick
     */
    @Override
    public void setLEDOff() {
        robot.getBrick().setLEDOff();
    }



    // ------------- SSOUND OPERATIONS -------------

    /**
     * Play a single beep
     */
    @Override
    public void singleBeep() {
        robot.getBrick().singleBeep();
    }

    /**
     * Play a double beep
     */
    @Override
    public void doubleBeep() {
        robot.getBrick().doubleBeep();
    }

    /**
     * Play a buzz-sound.
     */
    @Override
    public void buzz() {
        robot.getBrick().buzz();
    }

    /**
     * Play a downward beep sequence
     */
    @Override
    public void beepSequenceDown() {
        robot.getBrick().beepSequenceDown();
    }

    /**
     * Play a upward beep sequence
     */
    @Override
    public void beepSequenceUp() {
        robot.getBrick().beepSequenceUp();
    }

    // ------------- BUTTON PROVIDER -------------

    /**
     * Set Volume
     * @param volume volume of the sound
     */
    @Override
    public void setVolume(int volume) {
        robot.getBrick().setVolume(volume);
    }

    // ------------- DISPLAY OPERATIONS -------------

    /**
     * Clear the Brick-Display
     */
    @Override
    public void clearDisplay() {
        robot.getBrick().clearDisplay();
    }

    /**
     * Draw a String on the Brick-Display
     * @param str String to display
     * @param posX position x
     * @param posY position y
     */
    @Override
    public void drawString(String str, Textsize textsize, int posX, int posY) {
        robot.getBrick().drawString(str,textsize,posX,posY);
    }

    /**
     * TODO DOES NOT WORK YET
     * Draw an image.
     * @param str image name
     */
    @Override
    public void drawImage(String str) {
        //TODO not implemented on EV3Display
        robot.getBrick().drawImage(str);
    }



}
