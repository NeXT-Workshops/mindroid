package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

public class ButtonTest extends ImperativeWorkshopAPI {

    private EV3StatusLightColor[] colors = {EV3StatusLightColor.GREEN,EV3StatusLightColor.YELLOW,EV3StatusLightColor.RED};
    private int colorIndex = 0;

    private  EV3StatusLightInterval[] interval = {EV3StatusLightInterval.ON,EV3StatusLightInterval.BLINKING,EV3StatusLightInterval.DOUBLE_BLINKING};
    private int intervallIndex = 0;


    private EV3StatusLightColor currentColor = EV3StatusLightColor.GREEN;
    private EV3StatusLightInterval currentIntervall = EV3StatusLightInterval.ON;
    private boolean isOn = true;


    public ButtonTest() {
        super("ButtonTest");
    }

    @Override
    public void run() {
        sendLogMessage("ButtonTest: Starte LED Test");

        //LED TEST
        setLED(colors[0],interval[0]);
        delay(1000);
        setLED(colors[0],interval[1]);
        delay(1000);
        setLED(colors[0],interval[2]);
        delay(1000);

        setLED(colors[1],interval[0]);
        delay(1000);
        setLED(colors[1],interval[1]);
        delay(1000);
        setLED(colors[1],interval[2]);
        delay(1000);

        setLED(colors[2],interval[0]);
        delay(1000);
        setLED(colors[2],interval[1]);
        delay(1000);
        setLED(colors[2],interval[2]);
        delay(1000);

        setLEDOff();
        isOn = false;
        delay(1000);

        sendLogMessage("ButtonTest: Starte Implementierung - Buttons aktiv!");

        while(!isInterrupted()){



            delay(50);

            //Turn LED on/off
            if(isEnterButtonClicked()){
                sendLogMessage("Enter Button clicked");
                if(isOn){
                    setLEDOff();
                    isOn = false;
                }else{
                    setLED(currentColor,currentIntervall);
                    isOn = true;
                }
            }

            if(isRightButtonClicked()){
                sendLogMessage("Right Button clicked");
                if(isOn) {
                    currentColor = getNextColor();
                    setLED(currentColor, currentIntervall);
                }
            }

            if(isLeftButtonClicked()){
                sendLogMessage("Left Button clicked");
                if(isOn) {
                    currentColor = getPreviousColor();
                    setLED(currentColor, currentIntervall);
                }
            }

            if(isUpButtonClicked()){
                sendLogMessage("Up Button clicked");
                if(isOn) {
                    currentIntervall = getIncreasedInterval();
                    setLED(currentColor, currentIntervall);
                }
            }

            if(isDownButtonClicked()){
                sendLogMessage("Down Button clicked");
                if(isOn) {
                    currentIntervall = getDecreasedInterval();
                    setLED(currentColor, currentIntervall);
                }
            }


        }
    }

    private EV3StatusLightColor getNextColor(){
        if(colorIndex < 2){
            colorIndex++;
        }
        return colors[colorIndex];
    }

    private EV3StatusLightColor getPreviousColor(){
        EV3StatusLightColor[] colors = {EV3StatusLightColor.GREEN,EV3StatusLightColor.YELLOW,EV3StatusLightColor.RED};
        if(colorIndex > 0){
            colorIndex--;
        }
        return colors[colorIndex];
    }

    private EV3StatusLightInterval getIncreasedInterval(){
        if(intervallIndex < 2){
            intervallIndex++;
        }
        return interval[intervallIndex];
    }

    private EV3StatusLightInterval getDecreasedInterval(){
        if(intervallIndex > 0){
            intervallIndex--;
        }
        return interval[intervallIndex];
    }

}
