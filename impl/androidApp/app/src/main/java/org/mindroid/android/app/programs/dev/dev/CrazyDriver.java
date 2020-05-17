package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;
import org.mindroid.impl.motor.Motor;

import static java.lang.Math.PI;

public class CrazyDriver extends ImperativeWorkshopAPI {
    public CrazyDriver() {
        super("Crazy Driver aka Dancer", -1);
    }
    // Halber Radabstand
    private final double d = 6.25f; //cm
    // Raddurchmesser
    private final double diameter_wheel = 5.6f;
    // Radumfang
    private final double circ_wheel = diameter_wheel * Math.PI;
    Motor leftMotor = getMotorProvider().getMotor(getLeftMotorPort());
    Motor rightMotor = getMotorProvider().getMotor(getRightMotorPort());

    @Override
    public void run() {

        driveCurve(20.0, (40 * Math.PI)/10);
        delay(10000);

        /*
        leftMotor.setSpeed(getDegFromCm(10));
        rightMotor.setSpeed(getDegFromCm(10));
        forward();
        delay(1000);
        */
        stop();

    }

    private int getDegFromCm(double cmPerSec){
        return (int) ((cmPerSec/circ_wheel) * 360);
    }

    /**
     *  drives a curve of given radius r and translationSpeed v
     * @param r Curve radius in cm
     * @param v Speed in cm/sec
     */
    void driveCurve(double r, double v){
        double w = v / (2 * PI * r);
        double vl = (w * 2 * Math.PI * (r-d) );
        double vr = (w * 2 * Math.PI * (r+d) );

        clearDisplay();

        drawString("w:   " + w, Textsize.MEDIUM, 10, 10);
        drawString("v_l: " + vl, Textsize.MEDIUM, 10, 26);
        drawString("v_r: " + vr, Textsize.MEDIUM, 10, 42);

        leftMotor.setSpeed(getDegFromCm(vl));
        rightMotor.setSpeed(getDegFromCm(vr));

        forward();
    }
}
