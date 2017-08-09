package org.mindroid.android.app.robodancer;



import org.mindroid.android.app.SchuelerProjekt.ErrorStatemachines;
import org.mindroid.android.app.SchuelerProjekt.MindroidLVL1;
import org.mindroid.android.app.SchuelerProjekt.MindroidLVL2;
import org.mindroid.android.app.errorhandling.APIErrorHandler;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.impl.configuration.RobotPortConfig;
import org.mindroid.api.robot.IRobotFactory;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.statemachine.IMindroidMain;

import java.io.IOException;

import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.robot.RobotFactory;

/**
 * Created by mindroid on 09.12.16.
 */
public class Robot {

    public boolean isConnectedToBrick = false;
    public boolean isConfigurationBuilt = false;

    public boolean isRunning = false;

    IRobotCommandCenter commandCenter;

    IRobotFactory roFactory = new RobotFactory();
    //-------- Robot
    public RobotPortConfig robotPortConfig = new RobotPortConfig(); //TODO Dependency/Linked to SchuelerProjekt#
    //TODO Refactor: Make more dynamic: add collections of statemachines dynamically
    public IMindroidMain mindroidStatemachine; //TODO Dependency/Linked to SchuelerProjekt
    public IMindroidMain mindroidImperative; //TODO Dependency/Linked to SchuelerProjekt

    String runningStatemachineID = "";

    public Robot() {

    }

    public void loadStatemachines()  {
        //TODO Refactor: Make more dynamic
        if(mindroidStatemachine == null || mindroidImperative == null){
            mindroidStatemachine = new MindroidLVL1();
            mindroidImperative = new MindroidLVL2();
        }
    }

    /**
     * Configurate the Robot at the RobotFactory and build the Robot.
     */
    public void makeRobot()  {
        //TODO Add some hasChanged var to detect changes in configurations and only rebuild robot, when changes appear
        //Config
        if(mindroidStatemachine != null && mindroidImperative != null) {
            roFactory.setRobotConfig(robotPortConfig);
            roFactory.setBrickIP(Settings.getInstance().ev3IP);
            roFactory.setBrickTCPPort(Settings.getInstance().ev3TCPPort);
            roFactory.setMSGServerIP(Settings.getInstance().serverIP);
            roFactory.setMSGServerTCPPort(Settings.getInstance().serverTCPPort);
            roFactory.setRobotServerPort(Settings.getInstance().robotServerPort);
            roFactory.setRobotID(Settings.getInstance().robotID);

            //Add Statemachines
            roFactory.addStatemachine(mindroidStatemachine.getStatemachineCollection());
            roFactory.addStatemachine(mindroidImperative.getStatemachineCollection());

            //Create the Robot
            commandCenter = roFactory.createRobot();
        }else{
            System.out.println("[Robot:makeRobot] statemachine collections are null");
        }
    }

    /**
     *
     *
     */
    public void connectToBrick() throws IOException {
        commandCenter.connectToBrick();
    }

    private void checkState(){
        Runnable task = new Runnable(){
            @Override
            public void run(){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                isConnectedToBrick = commandCenter.isConnected();
            }
        };
    }


    public boolean isConnectedToBrick() {
        return isConnectedToBrick;
    }

    public void setConnectedToBrick(boolean connectedToBrick) {
        isConnectedToBrick = connectedToBrick;
    }

    public boolean isConfigurationBuilt() {
        return isConfigurationBuilt;
    }

    public void setConfigurationBuilt(boolean configurationBuilt) {
        isConfigurationBuilt = configurationBuilt;
    }

    /**
     * Returns the IDs of Statemachine-Groups (Group of parallel Statemachines) and Single-Statemachines
     * @return IDs of Statemachines as String[]
     */
    public String[] getStatemachineIDs(){
        //TODO Refactor: Make more dynamic
        int size_imp = mindroidImperative.getStatemachineCollection().getStatemachineKeySet().size();
        int size_state = mindroidStatemachine.getStatemachineCollection().getStatemachineKeySet().size();
        String[] tmpIDs = new String[size_imp+size_state];
        int index = 0;
        for (String id : mindroidImperative.getStatemachineCollection().getStatemachineKeySet()) {
            //Only add valid statemachines!
                tmpIDs[index] = id;
                index++;
        }
        for (String id : mindroidStatemachine.getStatemachineCollection().getStatemachineKeySet()) {
            //Only add valid statemachines!
            tmpIDs[index] = id;
            index++;

        }
        String[] ids = new String[index];
        for (int i = 0; i < index; i++) {
            ids[i] = tmpIDs[i];
        }

        return ids;
    }

    public boolean isConnected() {
        return commandCenter.isConnected();
    }

    public boolean initializeConfiguration() throws BrickIsNotReadyException, PortIsAlreadyInUseException {
        return commandCenter.initializeConfiguration();
    }

    /**
     * Start Group of-/Statemachine with id
     * @param id
     */
    public void startStatemachine(String id) {
        runningStatemachineID = id;
        commandCenter.startStatemachine(id);
    }

    /**
     * Stop the Group of-/Statemachine with id
     * @param id
     */
    public void stopStatemachine(String id) {
        runningStatemachineID = "";
        commandCenter.stopStatemachine(id);
    }

    public void stop() {
        if(runningStatemachineID != ""){
            commandCenter.stopStatemachine(runningStatemachineID);
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