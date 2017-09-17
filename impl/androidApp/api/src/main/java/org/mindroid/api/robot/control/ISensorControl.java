package org.mindroid.api.robot.control;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 02.03.2017.
 */

public interface ISensorControl {


    void changeSensorMode(Sensormode mode);

    float[] getValue();

    Sensormode getSensormode();

    EV3PortID getPort();

    Sensors getSensorType();

}
