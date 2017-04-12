package org.mindroid.api.robot;

import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;

/**
 * Created by torben on 02.03.2017.
 */

public interface IRobodancerConfig {
    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorAtPortS1();

    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorAtPortS2();

    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorAtPortS3();

    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorAtPortS4();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensorModeOfS1();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensorModeOfS2();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensorModeOfS3();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensorModeOfS4();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorAtPortA();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorAtPortB();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorAtPortC();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorAtPortD();
}
