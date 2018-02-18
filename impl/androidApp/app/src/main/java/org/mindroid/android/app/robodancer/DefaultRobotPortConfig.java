package org.mindroid.android.app.robodancer;

import org.mindroid.android.app.fragments.myrobot.HardwareMapping;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;

/**
 * Default configuration of Sensor/sensormodes and Motors of the Robot.
 * Gets used by the SettingsProvider {@link SettingsProvider} when loading the setup from the SharedPreferences xml file.
 * If this file does not contain any information about a port or the whole setup it will load the default values from this class.
 */
public class DefaultRobotPortConfig {

    public static final String MOTOR_A = Motors.LargeRegulatedMotor.getName();
    public static final String MOTOR_B = HardwareMapping.notDefined;
    public static final String MOTOR_C = HardwareMapping.notDefined;
    public static final String MOTOR_D = Motors.LargeRegulatedMotor.getName();

    public static final String SENSOR_S1 = Sensors.EV3ColorSensor.getName();
    public static final String SENSOR_S2 = Sensors.EV3UltrasonicSensor.getName();
    public static final String SENSOR_S3 = Sensors.EV3GyroSensor.getName();
    public static final String SENSOR_S4 = Sensors.EV3ColorSensor.getName();

    public static final String SENSORMODE_S1 = Sensormode.COLOR_ID.getValue();
    public static final String SENSORMODE_S2 = Sensormode.DISTANCE.getValue();
    public static final String SENSORMODE_S3 = Sensormode.ANGLE.getValue();
    public static final String SENSORMODE_S4 = Sensormode.COLOR_ID.getValue();

}
