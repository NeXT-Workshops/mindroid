package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;

public class MotorTest extends ImperativeWorkshopAPI {

    public MotorTest() {
        super("Motor-Test");
    }

    @Override
    public void run() {
        testSimpleForwardBackward();

        testRotate();

        driveForwardBackward();

        forward(750);
        setLED(EV3StatusLightColor.GREEN, EV3StatusLightInterval.BLINKING);
        enableFloatMode();
        delay(1000);
        stop();

        backward(750);
        setLED(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.BLINKING);
        enableFloatMode();
        delay(1000);
        stop();

        delay(1000);
        setLEDOff();

    }

    private void driveForwardBackward() {
        driveDistanceBackward(30.0f);
        stopAndDelay();
        driveDistanceBackward(30.0f,750);
        stopAndDelay();
        driveDistanceForward(30.0f);
        stopAndDelay();
        driveDistanceForward(30.0f,750);
    }

    private void testSimpleForwardBackward() {
        forward();
        stopAndDelay();
        setMotorSpeed(750);

        forward();
        stopAndDelay();

        backward();
        stopAndDelay();

        setMotorSpeed(250);
        backward();

        stopAndDelay();
    }

    private void testRotate(){
        turnLeft(90);
        turnRight(180);
        turnLeft(180,500);
        turnRight(180);
        turnLeft(180);
        turnRight(180,1000);
        turnLeft(90);
    }

    private void stopAndDelay(){
        delay(1000);
        stop();
        delay(50);
    }
}
