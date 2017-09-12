package org.mindroid.api.sensor;

import org.mindroid.impl.sensor.EV3SensorEndpoint;

/**
 * Created by torben on 21.01.2017.
 */

public interface IColorSensorEvent {

    public enum ColorsensorEventType {
        /** Colorsensor detects Ground **/
       GROUND,

        /** Colorsensor detects no ground **/
        NO_GROUND;
    }

    IColorSensorEvent.ColorsensorEventType getEvent();

    EV3SensorEndpoint getEventSource();

    long getTimeStamp();
}
