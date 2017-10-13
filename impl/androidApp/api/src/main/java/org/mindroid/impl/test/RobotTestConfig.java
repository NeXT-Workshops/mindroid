package org.mindroid.impl.test;


import org.mindroid.api.robot.IRobotPortConfig;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;

public class RobotTestConfig implements IRobotPortConfig {

    //------------------ SENSORS ------------------
    @Override
    public Sensors getSensorS1() {
        return Sensors.EV3ColorSensor;
    }

    @Override
    public Sensors getSensorS2() {
        return Sensors.EV3UltrasonicSensor;
    }

    @Override
    public Sensors getSensorS3() {
        return Sensors.EV3GyroSensor;
    }

    @Override
    public Sensors getSensorS4() {
        return Sensors.EV3ColorSensor;
    }

    @Override
    public Sensormode getSensormodeS1() {
        return Sensormode.COLOR_ID;
    }

    @Override
    public Sensormode getSensormodeS2() {
        return Sensormode.DISTANCE;
    }

    @Override
    public Sensormode getSensormodeS3() {
        return Sensormode.ANGLE;
    }

    @Override
    public Sensormode getSensormodeS4() {
        return Sensormode.COLOR_ID;
    }

    //------------------ MOTORS ------------------
    @Override
    public Motors getMotorA() {
        return Motors.LargeRegulatedMotor;   }

    @Override
    public Motors getMotorB() {
        return null; }

    @Override
    public Motors getMotorC() {
        return null;
    }

    @Override
    public Motors getMotorD() {
        return Motors.LargeRegulatedMotor;
    }

}
