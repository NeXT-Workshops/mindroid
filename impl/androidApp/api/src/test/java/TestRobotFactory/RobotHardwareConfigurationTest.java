package TestRobotFactory;

import org.mindroid.api.robot.IRobotPortConfig;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;

/**
 * Created by torben on 02.03.2017.
 */

public class RobotHardwareConfigurationTest implements IRobotPortConfig {
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
        return null;
    }

    @Override
    public Sensors getSensorS4() {
        return Sensors.EV3ColorSensor;
    }

    @Override
    public SensorMessages.SensorMode_ getSensormodeS1() {
        return SensorMessages.SensorMode_.RED;
    }

    @Override
    public SensorMessages.SensorMode_ getSensormodeS2() {
        return SensorMessages.SensorMode_.DISTANCE;
    }

    @Override
    public SensorMessages.SensorMode_ getSensormodeS3() {
        return null;
    }

    @Override
    public SensorMessages.SensorMode_ getSensormodeS4() {
        return SensorMessages.SensorMode_.RED;
    }

    //------------------ MOTORS ------------------
    @Override
    public Motors getMotorA() {
        return Motors.UnregulatedMotor;   }

    @Override
    public Motors getMotorB() {
        return null; }

    @Override
    public Motors getMotorC() {
        return null;
    }

    @Override
    public Motors getMotorD() {
        return Motors.UnregulatedMotor;
    }
}
