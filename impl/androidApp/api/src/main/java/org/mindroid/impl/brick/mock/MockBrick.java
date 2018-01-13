package org.mindroid.impl.brick.mock;

import org.mindroid.api.brick.Brick;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.impl.brick.Textsize;

public class MockBrick extends Brick implements IBrickControl {


    /**
     * Creates a Brick Class.
     *
     */
    public MockBrick() {

    }

    @Override
    public void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval) {

    }

    @Override
    public void setLEDOff() {

    }

    @Override
    public void setVolume(int volume) {

    }

    @Override
    public void singleBeep() {

    }

    @Override
    public void doubleBeep() {

    }

    @Override
    public void buzz() {

    }

    @Override
    public void beepSequenceDown() {

    }

    @Override
    public void beepSequenceUp() {

    }

    @Override
    public void clearDisplay() {

    }

    @Override
    public void drawString(String str, Textsize textsize, int posX, int posY) {

    }

    @Override
    public void drawImage(String str) {

    }

    public boolean isConnected(){
        //always "connected" as it is used as a mock class
        return true;
    }

    @Override
    public boolean isBrickReady() {
        return false;
    }

    @Override
    public void resetBrickState() {

    }

    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public void disconnect() {

    }
}
