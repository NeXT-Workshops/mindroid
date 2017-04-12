package mindroid.common.ev3.endpoints.sensors.ev3;

/**
 * Created by torben on 27.01.2017.
 */
public interface SensorListener {

    void handleSensorData(float[] sample);
}
