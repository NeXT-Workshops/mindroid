package org.mindroid.impl.configuration;

import org.mindroid.api.robot.IRobotPortConfig;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;


/**
 * Created by torben on 02.03.2017.
 */

public class RobotPortConfig implements IRobotPortConfig {

    private Sensors sensorS1 = null;
    private Sensors sensorS2 = null;
    private Sensors sensorS3 = null;
    private Sensors sensorS4 = null;

    private Sensormode sensormodeS1 = null;
    private Sensormode sensormodeS2 = null;
    private Sensormode sensormodeS3 = null;
    private Sensormode sensormodeS4 = null;

    private Motors motorA = null;
    private Motors motorB = null;
    private Motors motorC = null;
    private Motors motorD = null;
    //------------------ SENSORS ------------------
    @Override
    public Sensors getSensorS1() {    //Sensors.EV3ColorSensor;
        return sensorS1;
    }

    @Override
    public Sensors getSensorS2() { //Sensors.EV3UltrasonicSensor;
        return sensorS2;
    }

    @Override
    public Sensors getSensorS3() { //Sensors.EV3GyroSensor;
        return sensorS3;
    }

    @Override
    public Sensors getSensorS4() { //Sensors.EV3ColorSensor;
        return sensorS4;
    }

    @Override
    public Sensormode getSensormodeS1() { //SensorMessages.SensorMode_.COLOR_ID;
        return sensormodeS1;
    }

    @Override
    public Sensormode getSensormodeS2() { //SensorMessages.SensorMode_.DISTANCE;
        return sensormodeS2;
    }

    @Override
    public Sensormode getSensormodeS3() { //SensorMessages.SensorMode_.ANGLE;
        return sensormodeS3;
    }

    @Override
    public Sensormode getSensormodeS4() { //SensorMessages.SensorMode_.COLOR_ID;
        return sensormodeS4;
    }

    //------------------ MOTORS ------------------
    @Override
    public Motors getMotorA() { //Motors.UnregulatedMotor;
        return motorA;   }

    @Override
    public Motors getMotorB() {
        return motorB; }

    @Override
    public Motors getMotorC() {
        return motorC;
    }

    @Override
    public Motors getMotorD() {//return Motors.UnregulatedMotor;
        return motorD;
    }

    public void setSensorS1(Sensors sensorS1) {
        this.sensorS1 = sensorS1;
    }

    public void setSensorS2(Sensors sensorS2) {
        this.sensorS2 = sensorS2;
    }

    public void setSensorS3(Sensors sensorS3) {
        this.sensorS3 = sensorS3;
    }

    public void setSensorS4(Sensors sensorS4) {
        this.sensorS4 = sensorS4;
    }

    public void setSensormodeS1(Sensormode sensormodeS1) {
        this.sensormodeS1 = sensormodeS1;
    }

    public void setSensormodeS2(Sensormode sensormodeS2) {
        this.sensormodeS2 = sensormodeS2;
    }

    public void setSensormodeS3(Sensormode sensormodeS3) {
        this.sensormodeS3 = sensormodeS3;
    }

    public void setSensormodeS4(Sensormode sensormodeS4) {
        this.sensormodeS4 = sensormodeS4;
    }

    public void setMotorA(Motors motorA) {
        this.motorA = motorA;
    }

    public void setMotorB(Motors motorB) {
        this.motorB = motorB;
    }

    public void setMotorC(Motors motorC) {
        this.motorC = motorC;
    }

    public void setMotorD(Motors motorD) {
        this.motorD = motorD;
    }
}
