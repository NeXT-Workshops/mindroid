package mindroid.common.ev3.endpoints.motors.ev3;

/**
 * Created by torben on 27.01.2017.
 */
public interface IMotorMessageListener {
    void handleMotorMessage(Object msg);
}
