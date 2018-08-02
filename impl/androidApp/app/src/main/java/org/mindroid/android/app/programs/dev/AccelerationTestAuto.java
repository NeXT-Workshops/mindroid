package org.mindroid.android.app.programs.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;
import org.mindroid.impl.motor.Motor;

public class AccelerationTestAuto extends ImperativeWorkshopAPI{

    public AccelerationTestAuto() {
        super("Accel Test Auto");
    }

    @Override
    public void run() {
        clearDisplay();
        Motor leftMotor = getMotorProvider().getMotor(getLeftMotorPort());
        Motor rightMotor = getMotorProvider().getMotor(getRightMotorPort());


        leftMotor.setAcceleration(1000);
        rightMotor.setAcceleration(1000);
        drawString("Accel left:  " + leftMotor.getAcceleration(), Textsize.MEDIUM, 10, 40 );
        drawString("Accel right: " + rightMotor.getAcceleration(), Textsize.MEDIUM, 10, 60 );

        delay(2000);
        forward(500);
        delay(2000);
        stop();

        clearDisplay();


        leftMotor.setAcceleration(50);
        rightMotor.setAcceleration(50);
        delay(2000);
        drawString("Accel left:  " + leftMotor.getAcceleration(), Textsize.MEDIUM, 10, 40 );
        drawString("Accel right: " + rightMotor.getAcceleration(), Textsize.MEDIUM, 10, 60 );

        delay(2000);
        forward(500);
        delay(2000);
        stop();
    }
}
