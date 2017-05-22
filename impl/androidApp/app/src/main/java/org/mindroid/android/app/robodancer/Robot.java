package org.mindroid.android.app.robodancer;



import android.content.Context;
import android.content.SharedPreferences;

import org.mindroid.android.app.R;
import org.mindroid.android.app.SchuelerProjekt.MindroidLVL1;
import org.mindroid.android.app.SchuelerProjekt.MindroidLVL2;
import org.mindroid.android.app.SchuelerProjekt.RobotPortConfig;
import org.mindroid.api.robot.IRobotFactory;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.statemachine.IMindroidMain;

import java.io.IOException;

import org.mindroid.api.statemachine.exception.StateAlreadyExists;
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
    RobotPortConfig robotPortConfig = new RobotPortConfig(); //TODO Dependency/Linked to SchuelerProjekt
    public IMindroidMain mindroidStatemachine; //TODO Dependency/Linked to SchuelerProjekt
    public IMindroidMain mindroidImperative; //TODO Dependency/Linked to SchuelerProjekt

    String runningStatemachineID = "";




    public Robot() {

    }

    /**
     * TODO Refactor
     */
    public void makeRobot() throws StateAlreadyExists {
        if(mindroidStatemachine == null || mindroidImperative == null){
            mindroidStatemachine = new MindroidLVL1();
            mindroidImperative = new MindroidLVL2();
        }

        System.out.println("## App.Robot.makeRobot() got called ");

        //Load port Configuration
        //TODO

        //Config
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

        //Create Robot
        commandCenter = roFactory.createRobot();
    }

    /**
     *
     * Der EV3 muss auf USB eingestellt werden und eine statische IP zugewiesen bekommen, sowie
     * die Subnetz Maske 255.255.0.0. Den PC-Ausgang des Bricks mit dem Rechner verbinden.
     * ACHTUNG: Bei uns musste für die Verbindung von EV3 und Smartphone die Subnetzmaske wieder auf
     * automatic gestellt werden!!
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
        try {
            int size_imp = mindroidImperative.getStatemachineCollection().getStatemachineKeySet().size();
            int size_state = mindroidStatemachine.getStatemachineCollection().getStatemachineKeySet().size();
            String[] ids = new String[size_imp+size_state];
            int index = 0;
            for (String id : mindroidImperative.getStatemachineCollection().getStatemachineKeySet()) {
                ids[index] = id;
                index++;
            }
            for (String id : mindroidStatemachine.getStatemachineCollection().getStatemachineKeySet()) {
                ids[index] = id;
                index++;
            }
            return ids;
        } catch (StateAlreadyExists stateAlreadyExists) {
            stateAlreadyExists.printStackTrace();
        }
        return null;
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

}
