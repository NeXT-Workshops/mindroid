package org.mindroid.android.app.robodancer;




import org.mindroid.android.app.fragments.sensormonitoring.SensorListener;
import org.mindroid.android.app.serviceloader.StatemachineService;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.impl.configuration.RobotPortConfig;
import org.mindroid.api.robot.IRobotFactory;
import org.mindroid.api.robot.control.IRobotCommandCenter;

import java.io.IOException;
import java.util.HashMap;

import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
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
    private RobotPortConfig robotPortConfig = new RobotPortConfig();

    String runningStatemachinesGroupID = "";

    private HashMap<EV3PortID,IEV3SensorEventListener> sensorListener = new HashMap<>();

    public Robot() {
        initSensorListener();
    }

    private void initSensorListener() {
        sensorListener.put(EV3PortIDs.PORT_1,new SensorListener(EV3PortIDs.PORT_1));
        sensorListener.put(EV3PortIDs.PORT_2,new SensorListener(EV3PortIDs.PORT_2));
        sensorListener.put(EV3PortIDs.PORT_3,new SensorListener(EV3PortIDs.PORT_3));
        sensorListener.put(EV3PortIDs.PORT_4,new SensorListener(EV3PortIDs.PORT_4));
    }

    /**
     * Configurate the Robot at the RobotFactory and build the Robot.
     */
    public void create()  {

        updateRobotPortConfig();

        //Config
        roFactory.setRobotConfig(robotPortConfig);
        roFactory.setBrickIP(SettingsProvider.getInstance().getEv3IP());
        roFactory.setBrickTCPPort(SettingsProvider.getInstance().getEv3TCPPort());
        roFactory.setMSGServerIP(SettingsProvider.getInstance().getServerIP());
        roFactory.setMSGServerTCPPort(SettingsProvider.getInstance().getServerTCPPort());
        roFactory.setRobotServerPort(SettingsProvider.getInstance().getRobotServerPort());
        roFactory.setRobotID(SettingsProvider.getInstance().getRobotID());
        for(EV3PortID port : sensorListener.keySet()){ //TODO maybe leads to an error that sensor listener gets registered multiple times crashing the app. -> move somewhere else
            roFactory.registerSensorListener(port,sensorListener.get(port));
        }

        //Add StatemachineCollections
        for (int i = 0; i < StatemachineService.getInstance().getStatemachineCollections().size(); i++) {
            roFactory.addStatemachine(StatemachineService.getInstance().getStatemachineCollections().get(i));
        }

        //Create the Robot
        commandCenter = roFactory.createRobot();
    }

    private void updateRobotPortConfig() {
        getRobotPortConfig().setSensorS1(SettingsProvider.getInstance().getSensorS1());
        getRobotPortConfig().setSensorS2(SettingsProvider.getInstance().getSensorS2());
        getRobotPortConfig().setSensorS3(SettingsProvider.getInstance().getSensorS3());
        getRobotPortConfig().setSensorS4(SettingsProvider.getInstance().getSensorS4());

        getRobotPortConfig().setSensormodeS1(SettingsProvider.getInstance().getSensorModeS1());
        getRobotPortConfig().setSensormodeS2(SettingsProvider.getInstance().getSensorModeS2());
        getRobotPortConfig().setSensormodeS3(SettingsProvider.getInstance().getSensorModeS3());
        getRobotPortConfig().setSensormodeS4(SettingsProvider.getInstance().getSensorModeS4());

        getRobotPortConfig().setMotorA(SettingsProvider.getInstance().getMotorA());
        getRobotPortConfig().setMotorB(SettingsProvider.getInstance().getMotorB());
        getRobotPortConfig().setMotorC(SettingsProvider.getInstance().getMotorC());
        getRobotPortConfig().setMotorD(SettingsProvider.getInstance().getMotorD());
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

    public SensorListener getListenerForPort(EV3PortID port){
        return (SensorListener) sensorListener.get(port);
    }
}