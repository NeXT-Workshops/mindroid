package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.LVL2API;

public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() {
        while (!isInterrupted()) { 
            if(distanceLessThan(0.30f) && distanceGreaterThan(0.15f)) {
                setLED(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.BLINKING);
            } else if (distanceLessThan(0.15f)) {
                setLED(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.DOUBLE_BLINKING);
            } else {
                setLED(EV3StatusLightColor.GREEN, EV3StatusLightInvertal.ON);                
            }
        }
    }

    private void square() {
        for (int i = 0; i < 3 && !isInterrupted(); ++i) {
            int angle = 70;
            forward();
            delay(2000);
            turnRight(angle);
            forward();
            delay(2000);
            turnLeft(angle);
            backward();
            delay(2000);
            turnRight(angle);
            backward();
            delay(2000); 
            turnLeft(angle);
        }         
        stopMotors();
    }

    private void messagingBerta() {
        sendMessage("Robert", "Hallo Robert!");
    }

    private void messagingRobter() {
        while (!wasMsgReceived("Hallo Robert!", "Berta") && !isInterrupted()){
            delay(100);
        }
        if (!isInterrupted()) {
            brickController.drawString("Nachricht von Berta: Hallo Robert ", 1, 1);
            sendMessage("Berta", "Hallo Berta, ich bin es, der Robert!");
        }
    }

    public void wallPingPong() {
        int iteration = 0;
        while (true  && !isInterrupted()) {
            forward();
            while (distanceGreaterThan(0.15f) && !isInterrupted()) {
                delay(300);
            }
            stopMotors();
            backward();
            delay(1200);
            setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.BLINKING);
            if (iteration % 2 == 0) {
              turnLeft(140);
            } else {
              turnRight(140);
            }
            
            setLED(EV3StatusLightColor.OFF, EV3StatusLightInterval.BLINKING);
            stopMotors();
            iteration++;
        }
    }
}