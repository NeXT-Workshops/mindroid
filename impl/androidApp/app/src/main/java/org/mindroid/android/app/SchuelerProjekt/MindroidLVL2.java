package org.mindroid.android.app.SchuelerProjekt;

import org.mindroid.api.LVL2API;
import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.impl.robot.Robot;

/**
 * Created by Torben on 03.05.2017.
 * Easiest programming level.
 */
public class MindroidLVL2 extends LVL2API {


    public MindroidLVL2() throws StateAlreadyExists {

    }

    @Override
    public void run() {
        
        messenger = Robot.getRobotController().getMessenger();
        if(messenger!=null) messenger.sendMessage(IMessenger.SERVER_LOG, "Imperative Implementation is running!");


        //Example Wall-Ping-Pong
        while (true) {
            forward();
            while (distanceGreaterThan(0.15f)) {
                delay(300);
            }
            stopMotors();
            backward();
            delay(1200);
            setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.BLINKING);

            turnLeft(130);
            setLED(EV3StatusLightColor.OFF,EV3StatusLightInterval.BLINKING);
            stopMotors();
        }

    }
    


}
