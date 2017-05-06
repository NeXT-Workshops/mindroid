package org.mindroid.android.app.SchuelerProjekt;

import org.mindroid.api.robot.IRobodancerConfig;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;


/**
 * Created by torben on 02.03.2017.
 */

public class RobotPortConfig implements IRobodancerConfig {
    //------------------ SENSORS ------------------
    @Override
    public Sensors getSensorAtPortS1() {
        return Sensors.EV3ColorSensor;
    }

    @Override
    public Sensors getSensorAtPortS2() {
        return Sensors.EV3UltrasonicSensor;
    }

    @Override
    public Sensors getSensorAtPortS3() {
        return null;
    }

    @Override
    public Sensors getSensorAtPortS4() {
        return Sensors.EV3ColorSensor;
    }

    @Override
    public SensorMessages.SensorMode_ getSensorModeOfS1() {
        return SensorMessages.SensorMode_.COLOR_ID;
    }

    @Override
    public SensorMessages.SensorMode_ getSensorModeOfS2() {
        return SensorMessages.SensorMode_.DISTANCE;
    }

    @Override
    public SensorMessages.SensorMode_ getSensorModeOfS3() {
        return null;
    }

    @Override
    public SensorMessages.SensorMode_ getSensorModeOfS4() {
        return SensorMessages.SensorMode_.COLOR_ID;
    }

    //------------------ MOTORS ------------------
    @Override
    public Motors getMotorAtPortA() {
        return Motors.UnregulatedMotor;   }

    @Override
    public Motors getMotorAtPortB() {
        return null; }

    @Override
    public Motors getMotorAtPortC() {
        return null;
    }

    @Override
    public Motors getMotorAtPortD() {
        return Motors.UnregulatedMotor;
    }
}
