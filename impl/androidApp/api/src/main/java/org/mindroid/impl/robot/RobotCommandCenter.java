package org.mindroid.impl.robot;


import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.robot.Robot;

import java.io.IOException;

/**
 *
 * Interface to Smartphone/app
 *
 * Created by torben on 02.03.2017.
 */
public class RobotCommandCenter implements IRobotCommandCenter {

    Robot robot;

    public RobotCommandCenter(Robot robot){
        this.robot = robot;
    }

    @Override
    public boolean startStatemachine(String id) {
        return robot.getStatemachineManager().startStatemachine(id);
    }

    @Override
    public boolean stopStatemachine(String id) {
        return robot.getStatemachineManager().stopStatemachine(id);
    }

    @Override
    public String[] getStatemachines() {
        String[] IDsOfStatemachines = (String[]) robot.getStatemachineManager().getStatemachineIDs().toArray();
        return IDsOfStatemachines;
    }

    @Override
    public void connectToBrick() throws IOException {
        long timeout = 30000;
        robot.getRobotConfigurator().getBrick().connect();
        long start_timer = System.currentTimeMillis();
        while(!robot.getRobotConfigurator().getBrick().isBrickReady()){
            if(timeout < System.currentTimeMillis() - start_timer){
                throw new IOException("RobotCommandCenter: Connection timed out!");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return robot.getRobotConfigurator().getBrick().isConnected();
    }

    @Override
    public boolean initializeConfiguration() throws BrickIsNotReadyException {
        return robot.getRobotConfigurator().initializeConfiguration();
    }
}
