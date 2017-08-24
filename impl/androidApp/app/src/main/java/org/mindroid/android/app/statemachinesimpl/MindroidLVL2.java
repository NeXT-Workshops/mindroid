package org.mindroid.android.app.statemachinesimpl;

import org.mindroid.api.LVL2API;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

/**
 * Created by Torben on 03.05.2017.
 * Easiest programming level.
 */
public class MindroidLVL2 extends LVL2API {
    @Override
    public void run() {
        sendLogMessage("Imperative Implementation is running!");

        int scenario = 1;
        switch(scenario)
        {
            case 1:
                helloWorld(); // corresponds to Aufgabe 1
                break;
            case 2:
                forwardAndBackward(); // corresponds to Aufgabe 2.1
                break;
            case 3:
                slalom(); // corresponds to Aufgabe 2.2
                break;
            case 4:
                tableTurn(); // corresponds to Aufgabe 2.3
                break;
            case 5:
                measureDistance(); // corresponds to Aufgabe 2.4
                break;
            case 6:
                measureColors(); // corresponds to Aufgabe 2.5
                break;
            case 7:
                measureAngle(); // new: use gyro sensor
                break;
            case 8:
                sendingMessages(); // new: use send/receive
                break;

            case 9:
                lightAndShadow(); // corr. to Aufgabe 4
                break;
            case 10:
                wallPingPong();  // corr. to Aufgabe 5
                break;
            case 11:
                stayOnTable(); // corr. to Aufgabe 6
                break;
            case 12:
                polonaise(); // corr. to Aufgabe 7
                break;
        }

    }


    private void helloWorld() {
        //TODO@Mindroid: Implement me
    }

    private void forwardAndBackward() {
        //TODO@Mindroid: Implement me
    }

    private void slalom() {
        //TODO@Mindroid: Implement me
    }

    private void tableTurn() {
        //TODO@Mindroid: Implement me
    }

    private void measureDistance() {
        //TODO@Mindroid: Implement me
    }

    private void measureColors() {
        //TODO@Mindroid: Implement me
    }

    private void measureAngle() {
        //TODO@Mindroid: Implement me
    }

    private void sendingMessages() {
        //TODO@Mindroid: Implement me
    }

    private void lightAndShadow() {
        //TODO@Mindroid: Implement me
    }

    public void wallPingPong() {
        while (true  && !isInterrupted()) {
            forward();
            while (distanceGreaterThan(0.15f) && !isInterrupted()) {
                delay(300);
            }
            stopMotors();
            backward();
            delay(1200);
            setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.BLINKING);
            turnLeft(130);
            setLED(EV3StatusLightColor.OFF, EV3StatusLightInterval.BLINKING);
            stopMotors();

        }
    }

    private void stayOnTable() {
        //TODO@Mindroid: Implement me
    }

    private void polonaise() {
        //TODO@Mindroid: Implement me
    }



}
