package org.mindroid.android.app.robodancer;



import org.mindroid.android.app.serviceloader.StatemachineService;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.impl.configuration.RobotPortConfig;
import org.mindroid.api.robot.IRobotFactory;
import org.mindroid.api.robot.control.IRobotCommandCenter;

import java.io.IOException;

import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.robot.RobotFactory;

/**
 * Created by mindroid on 09.12.16.
 */
public class Robot {

    public boolean isRunning = false;


    IRobotCommandCenter commandCenter;

    IRobotFactory roFactory = new RobotFactory();
    //-------- Robot
    public RobotPortConfig robotPortConfig = new RobotPortConfig(); //TODO Dependency/Linked to SchuelerProjekt#
    //TODO Refactor: Make more dynamic: add collections of statemachines dynamically
    //public IMindroidMain mindroidStatemachine; //TODO Dependency/Linked to SchuelerProjekt
    //public IMindroidMain mindroidImperative; //TODO Dependency/Linked to SchuelerProjekt

    String runningStatemachinesGroupID = "";

    public Robot() {

    }

    /**
     * Configurate the Robot at the RobotFactory and build the Robot.
     */
    public void makeRobot()  {
        //TODO Add some hasChanged var to detect changes in configurations and only rebuild robot, when changes appear
        //Config
        roFactory.setRobotConfig(robotPortConfig);
        roFactory.setBrickIP(Settings.getInstance().ev3IP);
        roFactory.setBrickTCPPort(Settings.getInstance().ev3TCPPort);
        roFactory.setMSGServerIP(Settings.getInstance().serverIP);
        roFactory.setMSGServerTCPPort(Settings.getInstance().serverTCPPort);
        roFactory.setRobotServerPort(Settings.getInstance().robotServerPort);
        roFactory.setRobotID(Settings.getInstance().robotID);

        //Add StatemachineCollections
        for (int i = 0; i < StatemachineService.getInstance().getStatemachineCollections().size(); i++) {
            roFactory.addStatemachine(StatemachineService.getInstance().getStatemachineCollections().get(i));
        }

        //Create the Robot
        commandCenter = roFactory.createRobot();

    }

    /**
     *
     *
     */
    public void connectToBrick() throws IOException {
        commandCenter.connectToBrick();
    }

    public boolean isConnected() {
        if(commandCenter != null) {
            return commandCenter.isConnected();
        }else{
            return false;
        }
    }

    public boolean initializeConfiguration() throws BrickIsNotReadyException, PortIsAlreadyInUseException {
        return commandCenter.initializeConfiguration();
    }

    public boolean isConfigurated(){
        if(commandCenter != null) {
            return commandCenter.isConfigurated();
        }else{
            return false;
        }
    }

    /**
     * Start Group of-/Statemachine with id
     * @param id
     */
    public void startStatemachine(String id) {
        runningStatemachinesGroupID = id;
        commandCenter.startStatemachine(id);
    }

    /**
     * Stop the Group of-/Statemachine with id
     * @param id
     */
    public void stopStatemachine(String id) {
        runningStatemachinesGroupID = "";
        commandCenter.stopStatemachine(id);
    }

    public void stop() {
        if(!runningStatemachinesGroupID.equals("")){
            commandCenter.stopStatemachine(runningStatemachinesGroupID);
        }
    }

    public RobotPortConfig getRobotPortConfig() {
        return robotPortConfig;
    }

    public void disconnect() {
        commandCenter.disconnect();
    }

    public void registerErrorHandler(AbstractErrorHandler errorHandler){
        roFactory.addErrorHandler(errorHandler);
    }
}