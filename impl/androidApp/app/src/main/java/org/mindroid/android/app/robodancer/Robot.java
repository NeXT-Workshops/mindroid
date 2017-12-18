package org.mindroid.android.app.robodancer;




import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.fragments.log.GlobalLogger;
import org.mindroid.android.app.fragments.sensormonitoring.SensorListener;
import org.mindroid.android.app.serviceloader.ImperativeImplService;
import org.mindroid.android.app.serviceloader.StatemachineService;
import org.mindroid.api.ImperativeAPI;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.impl.configuration.RobotPortConfig;
import org.mindroid.api.robot.IRobotFactory;
import org.mindroid.api.robot.control.IRobotCommandCenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
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

    IRobotFactory roFactory;
    //-------- Robot
    private RobotPortConfig robotPortConfig;

    // The ID of the Implementation currently running - if empty ("") no implementation is running.
    private String runningImplementationID = "";

    private HashMap<EV3PortID,IEV3SensorEventListener> sensorListener;

    private final static Logger LOGGER = Logger.getLogger(Robot.class.getName());


    public Robot() {
        LogManager.getLogManager().addLogger(LOGGER);
        LOGGER.setLevel(Level.INFO);

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

        //Add StatemachineCollections
        for (int i = 0; i < StatemachineService.getInstance().getStatemachineCollections().size(); i++) {
            roFactory.addStatemachine(StatemachineService.getInstance().getStatemachineCollections().get(i));
        }

        //Add Imperative Implementations
        for (ImperativeAPI imperativeImpl : ImperativeImplService.getInstance().getImperativeImplCollection()) {
            roFactory.addImperativeImplementation(imperativeImpl);
        }

        //Create the Robot
        roFactory.createRobot(SettingsProvider.getInstance().isSimulationEnabled());
        LOGGER.log(Level.INFO,"Robot created!");
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
        if (ImperativeImplService.getInstance().getImperativeImplIDs().contains(id)) {
            LOGGER.log(Level.INFO,"Start imperative implementation: ".concat(id));
            startImperativeImpl(id);
        }else if(StatemachineService.getInstance().getStatemachineCollectionIDs().contains(id)){
            LOGGER.log(Level.INFO,"Start statemachine: ".concat(id));
            startStatemachine(id);
        }else{
            //TODO errorhandling?
        }
    }

    /**
     * Stop the running Implementation.
     */
    public void stopRunningImplmentation(){

        if (ImperativeImplService.getInstance().getImperativeImplIDs().contains(runningImplementationID)) {
            LOGGER.log(Level.INFO,"Stopping imperative implementation: ".concat(runningImplementationID));
            stopImperativeImpl(runningImplementationID);
        }else if(StatemachineService.getInstance().getStatemachineCollectionIDs().contains(runningImplementationID)){
            LOGGER.log(Level.INFO,"Stopping Statemachine: ".concat(runningImplementationID));
            stopStatemachine(runningImplementationID);
        }else{
            //TODO errorhandling?
        }
    }

    /**
     * Start an Imperative Implementation
     * @param id
     */
    private void startImperativeImpl(String id) {
        runningImplementationID = id;
        roFactory.getRobotCommandCenter().startImperativeImplemenatation(id);
    }

    /**
     * Stop an Imperative Implementation
     * @param id
     */
    private void stopImperativeImpl(String id) {
        runningImplementationID = "";
        roFactory.getRobotCommandCenter().stopImperativeImplementation(id);
    }

    /**
     * Start Group of-/Statemachine with id
     * @param id
     */
    private void startStatemachine(String id) {
        runningImplementationID = id;
        roFactory.getRobotCommandCenter().startStatemachine(id);
    }

    /**
     * Stop the Group of-/Statemachine with id
     * @param id
     */
    private void stopStatemachine(String id) {
        runningImplementationID = "";
        roFactory.getRobotCommandCenter().stopStatemachine(id);
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