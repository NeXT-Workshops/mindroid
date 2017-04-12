package robotConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.mindroid.impl.configuration.RobotConfigurator;

import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.Sensors;

/**
 * Created by torben on 09.01.2017.
 */

public class TestRobotConfiguration {
    RobotConfigurator config;

    @Before
    public void init(){
        config = new RobotConfigurator("DummyIP",-1);

        config.addMotorConfigurationSet(Motors.MediumRegulatedMotor,Motors.UnregulatedMotor,null,Motors.MediumRegulatedMotor);
        config.addSensorConfigurationSet(Sensors.EV3ColorSensor,null,Sensors.EV3IRSensor,Sensors.EV3UltrasonicSensor);
    }

    @Test
    public void testGetConfiguration(){
        System.out.println(config.getConfiguration());
        System.out.println("ENDE");
    }
}
