package TestRobotFactory;

import org.junit.Test;
import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.api.robot.IRobodancerConfig;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.robot.IRobotFactory;
import org.mindroid.common.messages.NetworkPortConfig;
import org.mindroid.impl.robot.RobotFactory;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;

import java.io.IOException;

/**
 * Created by torben on 03.03.2017.
 */

public class Testbench {

    @Test
    public void testRobotFactory() throws Exception {
        IRobodancerConfig config = new RobotHardwareConfigurationTest();
        IMindroidMain mindroid = new MindroidMainTest();
        IRobotCommandCenter robotControl;
        final String brickIP = "192.168.137.2";

        IRobotFactory rf = new RobotFactory();

        rf.addStatemachine(mindroid.getStatemachine());
        rf.setRobotConfig(config);
        rf.setBrickIP(brickIP);
        rf.setBrickTCPPort(NetworkPortConfig.BRICK_PORT);

        robotControl = rf.createRobot();

        try {
            robotControl.connectToBrick();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            robotControl.initializeConfiguration();
        } catch (BrickIsNotReadyException e) {
            e.printStackTrace();
        } catch (PortIsAlreadyInUseException e) {
            e.printStackTrace();
        }

    }
}
