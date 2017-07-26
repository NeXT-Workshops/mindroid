package org.mindroid.api.robot;

import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;

/**
 * Created by torben on 02.03.2017.
 */

public interface IRobotPortConfig {
    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorS1();

    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorS2();

    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorS3();

    /**
     *
     * @return sensor type - or null if no sensor is connected
     */
    Sensors getSensorS4();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensormodeS1();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensormodeS2();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensormodeS3();

    /**
     *
     * @return the mode the sensor should run
     */
    SensorMessages.SensorMode_ getSensormodeS4();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorA();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorB();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorC();

    /**
     *
     * @return motor type - or null if no sensor is connected
     */
    Motors getMotorD();
}
