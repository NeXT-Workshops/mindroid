package org.mindroid.android.app.robodancer;

import android.provider.Settings;
import org.mindroid.android.app.fragments.log.GlobalLogger;
import org.mindroid.android.app.fragments.sensormonitoring.SensorListener;
import org.mindroid.android.app.serviceloader.ImplementationService;
import org.mindroid.api.BasicAPI;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.impl.configuration.RobotPortConfig;
import org.mindroid.api.robot.IRobotFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private IRobotFactory roFactory;
    //-------- Robot
    private RobotPortConfig robotPortConfig;

    // The ID of the Implementation currently running - if empty ("") no implementation is running.
    private String runningImplementationID = "";

    private HashMap<EV3PortID,IEV3SensorEventListener> sensorListener;

    private final static Logger LOGGER = Logger.getLogger(Robot.class.getName());


    public Robot() {
        LOGGER.setLevel(Level.INFO);
        GlobalLogger.registerLogger(LOGGER);

        roFactory = new RobotFactory();
        robotPortConfig = new RobotPortConfig();
        sensorListener = new HashMap<>();

        updateRobotPortConfig();

        initSensorListener();
    }

    /**
     * Sensor listener for sensor monitor
     */
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
        roFactory.setRobotID(SettingsProvider.getInstance().getRobotID());
        for(EV3PortID port : sensorListener.keySet()){ //TODO maybe leads to an error that sensor listener gets registered multiple times crashing the app. -> move somewhere else
            roFactory.registerSensorListener(port,sensorListener.get(port));
        }

        //Create the Robot - with it the RobotCommandCenter for this robot is created and can be accessed after this call.
        roFactory.createRobot(SettingsProvider.getInstance().isSimulationEnabled());

        //Adds the collected Implementations to execute
        addImplementations(ImplementationService.getInstance().getImplementations());

        LOGGER.log(Level.INFO,"Robot created!");
    }

    /**
     * Adds the StatemachineAPI Implementations to the RobotCommandCenters' db
     */
    private void addImplementations(List<BasicAPI> implementations){
        for (BasicAPI implementation : implementations) {
            addImplemenation(implementation);
        }
    }

    private void addImplemenation(BasicAPI api){
        roFactory.getRobotCommandCenter().addImplementation(api);
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
     * Returns the state of the messenger client.
     * Returns false, if the command center is null (no robot created yet by - call create() first!)
     * @return true if the messenger client is connected to the message server
     */
    public boolean isMessengerConnected(){
        if(roFactory.getRobotCommandCenter() != null) {
            return roFactory.getRobotCommandCenter().isMessengerConnected();
        }else{
            return false;
        }
    }

    /**
     * Connects the messenger client to the message server
     */
    public boolean connectMessenger(String msgServerIP, int msgServerPort){
        LOGGER.log(Level.INFO,"Connecting to Message-Server: "+msgServerIP+":"+msgServerPort);
        roFactory.setRobotID(SettingsProvider.getInstance().getRobotID());
        return roFactory.getRobotCommandCenter().connectMessenger(msgServerIP,msgServerPort);
    }

    public void disconnectMessenger(){
        roFactory.getRobotCommandCenter().disconnectMessenger();
    }

    public void connectToBrick() throws IOException {
        LOGGER.log(Level.INFO,"Connecting to Brick");
        roFactory.getRobotCommandCenter().connectToBrick();
    }

    public boolean isConnectedToBrick() {
        if(roFactory.getRobotCommandCenter() != null) {
            return roFactory.getRobotCommandCenter().isConnectedToBrick();
        }else{
            return false;
        }
    }

    public boolean initializeConfiguration() throws BrickIsNotReadyException, PortIsAlreadyInUseException {
        LOGGER.log(Level.INFO,"Intializing Configuration");
        return roFactory.getRobotCommandCenter().initializeConfiguration();
    }

    /**
     * Aborts a running initialization.
     * There will be no check if a initialization process is actually running.
     */
    public void abortInitializationProcess(){
        roFactory.getRobotCommandCenter().abortConfiguration();
    }

    public boolean isConfigurated(){
        if(roFactory.getRobotCommandCenter() != null) {
            return roFactory.getRobotCommandCenter().isConfigurated();
        }else{
            return false;
        }
    }

    /**
     * Start executing the Implementation with the given ID.
     * If there is an Imperative and a Statemachine Implementation with the same ID the Imperative one will be started.
     * @param id - of the implementation
     */
    public void startExecuteImplementation(String id){
        LOGGER.log(Level.INFO, "Start to execute: " + id);
        runningImplementationID = id;
        roFactory.getRobotCommandCenter().startImplementation(id);
    }

    /**
     * Stop the running Implementation.
     */
    public void stopRunningImplmentation(){
        LOGGER.log(Level.INFO, "Stopped execution: " + runningImplementationID);
        runningImplementationID = "";
        roFactory.getRobotCommandCenter().stopImplementation();
    }

    public RobotPortConfig getRobotPortConfig() {
        return robotPortConfig;
    }

    public void disconnectFromBrick() {
        roFactory.getRobotCommandCenter().disconnectFromBrick();
    }

    public void registerErrorHandler(AbstractErrorHandler errorHandler){
        roFactory.addErrorHandler(errorHandler);
    }

    public SensorListener getListenerForPort(EV3PortID port){
        return (SensorListener) sensorListener.get(port);
    }
}