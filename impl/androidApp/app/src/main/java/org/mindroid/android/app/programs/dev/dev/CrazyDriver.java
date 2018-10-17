package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.impl.brick.Textsize;
import org.mindroid.impl.motor.Motor;

import static java.lang.Math.PI;

public class CrazyDriver extends ImperativeWorkshopAPI {
    public CrazyDriver() {
        super("Crazy Driver aka Dancer", -1);
    }
    private double d = 6.25f; //cm
    private double u = 2 * d * Math.PI;
    Motor leftMotor = getMotorProvider().getMotor(getLeftMotorPort());
    Motor rightMotor = getMotorProvider().getMotor(getRightMotorPort());

    @Override
    public void run() {
        driveCurve(25, 25);
        delay(6280);
    }
    private int getDegFromCm(int cmPerSec){
        return 360 * cmPerSec / 360;
    }

    /**
     *  drives a curve of given radius r and translationSpeed v
     * @param r Curve radius in cm
     * @param v Speed in cm/sec
     */
    void driveCurve(int r, int v){
        double w = v / 2 * PI * r;
        drawString("w: " + w, Textsize.MEDIUM, 10, 10);
        int vl = (int) (v-d*w);
        int vr = (int) (v+d*w);
        
        leftMotor.setSpeed(getDegFromCm(vl));
        rightMotor.setSpeed(getDegFromCm(vr));
        forward();
    }
}
