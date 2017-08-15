package org.mindroid.impl.robot;


import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;

import java.io.IOException;

/**
 *
 * Interface to Smartphone/app
 *
 * Created by torben on 02.03.2017.
 */
public class RobotCommandCenter implements IRobotCommandCenter {

    Robot robot;
    private boolean isConfigurated = false;

    public RobotCommandCenter(Robot robot){
        this.robot = robot;
    }

    @Override
    public void startStatemachine(String id) {
        robot.getStatemachineManager().startStatemachines(id);
    }

    @Override
    public void stopStatemachine(String id) {
        robot.getStatemachineManager().stopStatemachines(id);
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
                Exception e = new IOException("RobotCommandCenter: Connection timed out!");
                ErrorHandlerManager.getInstance().handleError(e,RobotCommandCenter.class,e.toString());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ErrorHandlerManager.getInstance().handleError(e,RobotCommandCenter.class,e.toString());
            }
        }
    }

    @Override
    public boolean isConnected() {
        if(robot.getRobotConfigurator() != null && robot.getRobotConfigurator().getBrick() != null) {
            return robot.getRobotConfigurator().getBrick().isConnected();
        }else{
            return false;
        }
    }

    @Override
    public boolean initializeConfiguration() throws BrickIsNotReadyException {
        return (isConfigurated = robot.getRobotConfigurator().initializeConfiguration());
    }

    @Override
    public boolean isConfigurated() {
        return isConnected() && isConfigurated;
    }

    @Override
    public void disconnect() {
        robot.getRobotConfigurator().getBrick().disconnect();
    }
}
