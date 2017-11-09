package org.mindroid.impl.robot;


import org.mindroid.api.ImperativeAPI;
import org.mindroid.api.communication.IMessageServer;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.robot.*;
import org.mindroid.api.robot.context.IConstraintEvaluator;
import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.api.robot.control.MotorProvider;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.communication.MessageServer;
import org.mindroid.impl.communication.MessengerClient;
import org.mindroid.impl.configuration.RobotConfigurator;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.robot.context.RobotContextState;
import org.mindroid.impl.robot.context.RobotContextStateEvaluator;
import org.mindroid.impl.robot.context.RobotContextStateManager;
import org.mindroid.impl.statemachine.StatemachineCollection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by torben on 02.03.2017.
 */
public final class RobotFactory implements IRobotFactory {
    // ---------------- VON AUSSEN GESETZTE VARIABLEN ------------------------
    //stores Sensortypes plugged to the ports
    private Sensors sensor_S1 = null;
    private Sensors sensor_S2 = null;
    private Sensors sensor_S3 = null;
    private Sensors sensor_S4 = null;

    //stores sensormodes used by the sensors connected to the ports
    private Sensormode mode_S1 = null;
    private Sensormode mode_S2 = null;
    private Sensormode mode_S3 = null;
    private Sensormode mode_S4 = null;

    //stores Motortypes plugged to the ports
    private Motors motor_A = null;
    private Motors motor_B = null;
    private Motors motor_C = null;
    private Motors motor_D = null;

    //Further to register SensorListeners
    private HashMap<EV3PortID,ArrayList<IEV3SensorEventListener>> sensorListenerToRegister = new HashMap<>();

    //Unneccessary vars - just for testing
    private MotorProvider motorProvider = Robot.getRobotController().getMotorProvider();
    private SensorProvider sensorControl = Robot.getRobotController().getSensorProvider();
    private IBrickControl brickControl = Robot.getRobotController().getBrickController();

    private String brickIP = null;
    private int brickTCPport = -1;
    private int robotServerPort = -1;

    private String msgServerIP = null;
    private int msgServerTCPPort = -1;

    IRobotPortConfig robotConfig;



    // ----------------------- Verwendete Variablen ------------------------------

    private final String regexIP = "((((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)[.]){3})(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?){1}){1}";

    // --------------------------------------------------------------------------

    private Robot myRobot;
    private RobotCommandCenter myRobotCommandCenter;

    public RobotFactory(){
        myRobot = Robot.getInstance();
        myRobotCommandCenter = new RobotCommandCenter(myRobot);

        sensorListenerToRegister.put(EV3PortIDs.PORT_1,new ArrayList<IEV3SensorEventListener>());
        sensorListenerToRegister.put(EV3PortIDs.PORT_2,new ArrayList<IEV3SensorEventListener>());
        sensorListenerToRegister.put(EV3PortIDs.PORT_3,new ArrayList<IEV3SensorEventListener>());
        sensorListenerToRegister.put(EV3PortIDs.PORT_4,new ArrayList<IEV3SensorEventListener>());

        sensorListenerToRegister.get(EV3PortIDs.PORT_1).add(RobotContextState.getInstance());
        sensorListenerToRegister.get(EV3PortIDs.PORT_2).add(RobotContextState.getInstance());
        sensorListenerToRegister.get(EV3PortIDs.PORT_3).add(RobotContextState.getInstance());
        sensorListenerToRegister.get(EV3PortIDs.PORT_4).add(RobotContextState.getInstance());
    }

    @Override
    public void setRobotConfig(IRobotPortConfig robotConfig) {
        this.robotConfig = robotConfig;
    }

    @Override
    public IRobotCommandCenter createRobot() {
        RobotCommandCenter robotCommandCenter = new RobotCommandCenter(myRobot);
        Robot.getRobotController().setRobotID(Robot.getInstance().robotID);

        if(robotConfig != null) {
            //Sensors
            sensor_S1 = robotConfig.getSensorS1();
            sensor_S2 = robotConfig.getSensorS2();
            sensor_S3 = robotConfig.getSensorS3();
            sensor_S4 = robotConfig.getSensorS4();

            //Sensormodes
            mode_S1 = robotConfig.getSensormodeS1();
            mode_S2 = robotConfig.getSensormodeS2();
            mode_S3 = robotConfig.getSensormodeS3();
            mode_S4 = robotConfig.getSensormodeS4();

            //Motors
            motor_A = robotConfig.getMotorA();
            motor_B = robotConfig.getMotorB();
            motor_C = robotConfig.getMotorC();
            motor_D = robotConfig.getMotorD();

        }else{
            throw new NullPointerException("No RobotConfig set!");
        }

        if(isValidIP(brickIP) && isValidTCPPort(brickTCPport)) {
            // Configuration of the robot
            RobotConfigurator robotConfigurator = new RobotConfigurator(brickIP, brickTCPport);
            myRobot.setRobotConfigurator(robotConfigurator);

            robotConfigurator.addSensorConfigurationSet(sensor_S1,sensor_S2,sensor_S3,sensor_S4);
            robotConfigurator.setSensorModeSet(mode_S1,mode_S2,mode_S3,mode_S4);
            robotConfigurator.addMotorConfigurationSet(motor_A,motor_B,motor_C,motor_D);

            try {
                myRobot.setSensorS1(robotConfigurator.createSensor(EV3PortIDs.PORT_1));
                myRobot.setSensorS2(robotConfigurator.createSensor(EV3PortIDs.PORT_2));
                myRobot.setSensorS3(robotConfigurator.createSensor(EV3PortIDs.PORT_3));
                myRobot.setSensorS4(robotConfigurator.createSensor(EV3PortIDs.PORT_4));

                myRobot.setMotorA(robotConfigurator.createMotor(EV3PortIDs.PORT_A));
                myRobot.setMotorB(robotConfigurator.createMotor(EV3PortIDs.PORT_B));
                myRobot.setMotorC(robotConfigurator.createMotor(EV3PortIDs.PORT_C));
                myRobot.setMotorD(robotConfigurator.createMotor(EV3PortIDs.PORT_D));

                myRobot.setSyncedMotors(robotConfigurator.createSynchronizedMotorsEndpoint());

            } catch (PortIsAlreadyInUseException e) {
                //TODO maybe add proper error handling or remove exception?
                robotCommandCenter = null;
                e.printStackTrace();
            }

            // --- Connect Sensorobject --> with RobotContextState
            for(EV3PortID port : sensorListenerToRegister.keySet()){
                if(port.equals(EV3PortIDs.PORT_1) && myRobot.getSensorS1() != null){
                    for (IEV3SensorEventListener iev3SensorEventListener : sensorListenerToRegister.get(port)) {
                        myRobot.getSensorS1().registerListener(iev3SensorEventListener);
                        //System.out.println("[RobotFactory:createRobot] registered Sensor Listener "+iev3SensorEventListener);
                    }
                }else if(port.equals(EV3PortIDs.PORT_2) && myRobot.getSensorS2() != null){
                    for (IEV3SensorEventListener iev3SensorEventListener : sensorListenerToRegister.get(port)) {
                        myRobot.getSensorS2().registerListener(iev3SensorEventListener);
                        //System.out.println("[RobotFactory:createRobot] registered Sensor Listener "+iev3SensorEventListener);
                    }
                }else if(port.equals(EV3PortIDs.PORT_3) && myRobot.getSensorS3() != null){
                    for (IEV3SensorEventListener iev3SensorEventListener : sensorListenerToRegister.get(port)) {
                        myRobot.getSensorS3().registerListener(iev3SensorEventListener);
                        //System.out.println("[RobotFactory:createRobot] registered Sensor Listener "+iev3SensorEventListener);
                    }
                }else if(port.equals(EV3PortIDs.PORT_4) && myRobot.getSensorS4() != null){
                    for (IEV3SensorEventListener iev3SensorEventListener : sensorListenerToRegister.get(port)) {
                        myRobot.getSensorS4().registerListener(iev3SensorEventListener);
                        //System.out.println("[RobotFactory:createRobot] registered Sensor Listener "+iev3SensorEventListener);
                    }
                }
            }

            myRobot.setBrick(robotConfigurator.getBrick());

            // Connect statemachines RobotContextStateEvaluator etc with state machine
            setupStatemachineEngine();

            //TODO setup Imperative Engine


            //---------------- CREATE MESSENGER and ROBOTSERVER
            createNetworkCommunicationInterfaces();


        }else{
            ErrorHandlerManager.getInstance().handleError(new Exception("Could not create Robot"),this.getClass(),"RobotFactory.createRobot(): Error appeard while creating robot!");
            robotCommandCenter = null;
        }
        System.out.println("[RobotFactory:createRobot] The RobotFactory created a Robot with the following setup:\n"+toString());
        return robotCommandCenter;
    }

    /**
     * Setups the Statemachine Engine.
     *
     */
    private void setupStatemachineEngine() {
        IConstraintEvaluator evaluator = new RobotContextStateEvaluator();
        myRobot.getStatemachineManager().addConstraintEvaluator(evaluator);
        RobotContextStateManager.getInstance().registerRobotContextStateListener(evaluator);
    }

    /**
     * Creates the MessengerClient and the MessageServer.
     */
    private void createNetworkCommunicationInterfaces() {
        if(isValidIP(msgServerIP) && isValidTCPPort(msgServerTCPPort)){
            Runnable run = new Runnable(){
                @Override
                public void run(){
                    try {
                        //Initialize MessengerClient
                        Robot.getInstance().messenger = new MessengerClient(myRobot.getRobotID(), InetAddress.getByName(msgServerIP),msgServerTCPPort);
                        Robot.getRobotController().setMessenger(Robot.getInstance().messenger);
                        Robot.getInstance().messageingEnabled = true;
                        //Initialize MessageServer
                        if(isValidTCPPort(robotServerPort)) {
                            IMessageServer robotServer = new MessageServer(robotServerPort,myRobot.messenger);
                            robotServer.registerMsgListener(RobotContextState.getInstance());
                            robotServer.registerMsgListener(Robot.getInstance().getMessenger());
                            robotServer.start();
                        }else{
                            //TODO send error message to error-handler
                        }

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(run).start();
        }else{
            myRobot.messenger = null;
            myRobot.messageingEnabled = false;
        }

        //Workaround: messenger is not set when robot Controller gets initialized //TODO remove?
        if(Robot.getInstance().isMessageingEnabled()){

        }

    }

    @Override
    public void setBrickIP(String ip) {
        brickIP = ip;
    }

    @Override
    public void setBrickTCPPort(int tcpPort) {
        //TODO check if valid tcpport
        brickTCPport = tcpPort;
    }

    @Override
    public void setMSGServerIP(String msgServerIP) {
        this.msgServerIP = msgServerIP;
    }

    @Override
    public void setMSGServerTCPPort(int tcpPort) {
        this.msgServerTCPPort = tcpPort;
    }

    @Override
    public void setRobotServerPort(int robotServerPort){
        this.robotServerPort = robotServerPort;
    }

    @Override
    public void setRobotID(String robotID) {
        Robot.getInstance().setRobotID(robotID);
    }

    @Override
    public void addStatemachine(StatemachineCollection statemachines) {
        myRobot.getStatemachineManager().addStatemachines(statemachines);
    }

    @Override
    public void addImperativeImplementation(ImperativeAPI imperativeImplementation) {
        myRobot.getImperativeImplManager().addImperativeImplementation(imperativeImplementation);
    }

    /**
     * Needs to be added before creating the robot
     *
     * @param port port the listener should listen to
     * @param listener the sensor listener to register
     */
    @Override
    public void registerSensorListener(EV3PortID port,IEV3SensorEventListener listener) {
        if (!sensorListenerToRegister.containsKey(port)) {
            sensorListenerToRegister.put(port,new ArrayList());
        }
        sensorListenerToRegister.get(port).add(listener);

    }

    @Override
    public void addErrorHandler(AbstractErrorHandler errorHandler){
        ErrorHandlerManager.getInstance().registerErrorHandler(errorHandler);
    }

    @Override
    public void clearConfiguration() {
        this.sensor_S1 = null;
        this.sensor_S2 = null;
        this.sensor_S3 = null;
        this.sensor_S4 = null;

        this.mode_S1 = null;
        this.mode_S2 = null;
        this.mode_S3 = null;
        this.mode_S4 = null;

        this.motor_A = null;
        this.motor_B = null;
        this.motor_C = null;
        this.motor_D = null;

        this.brickIP = null;
        this.brickTCPport = -1;

        this.msgServerIP = null;
        this.msgServerTCPPort = -1;
        this.robotServerPort = -1;
    }

    private boolean isValidTCPPort(int brickTCPport) {
        return (brickTCPport >= 0 && brickTCPport <= 65535);
    }

    private boolean isValidIP(String brickIP) {
        if(brickIP == null){
            System.err.println("RobotFactory: BrickIP is null");
            return false;
        }
        return brickIP.matches(regexIP);
    }

    @Override
    public String toString() {
        return "RobotFactory{" +
                "\nsensor_S1=" + sensor_S1 +
                ",\n sensor_S2=" + sensor_S2 +
                ",\n sensor_S3=" + sensor_S3 +
                ",\n sensor_S4=" + sensor_S4 +
                ",\n mode_S1=" + mode_S1 +
                ",\n mode_S2=" + mode_S2 +
                ",\n mode_S3=" + mode_S3 +
                ",\n mode_S4=" + mode_S4 +
                ",\n motor_A=" + motor_A +
                ",\n motor_B=" + motor_B +
                ",\n motor_C=" + motor_C +
                ",\n motor_D=" + motor_D +
                ",\n motorProvider=" + motorProvider +
                ",\n sensorControl=" + sensorControl +
                ",\n brickControl=" + brickControl +
                ",\n brickIP='" + brickIP + '\'' +
                ",\n brickTCPport=" + brickTCPport +
                ",\n msgServerIP='" + msgServerIP + '\'' +
                ",\n msgServerTCPPort=" + msgServerTCPPort +
                ",\n robotConfig=" + robotConfig +
                '}';
    }
}
