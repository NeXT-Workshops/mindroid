package org.mindroid.impl.robot;

import org.mindroid.api.robot.*;
import org.mindroid.api.robot.context.IRobotContextStateEvaluator;
import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.robot.control.ISensorControl;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import org.mindroid.impl.configuration.RobotConfigurator;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.robot.context.RobotContextStateListener;
import org.mindroid.impl.robot.context.RobotContextStateEvaluator;
import org.mindroid.impl.robot.context.RobotContextStateManager;

import java.util.HashMap;

/**
 * Created by torben on 02.03.2017.
 */
public final class RobotFactory implements IRobotFactory {
    // ---------------- VON AUSSEN GESETZTE VARIABLEN ------------------------
    private Sensors sensor_S1 = null;
    private Sensors sensor_S2 = null;
    private Sensors sensor_S3 = null;
    private Sensors sensor_S4 = null;

    private SensorMessages.SensorMode_ mode_S1 = null;
    private SensorMessages.SensorMode_ mode_S2 = null;
    private SensorMessages.SensorMode_ mode_S3 = null;
    private SensorMessages.SensorMode_ mode_S4 = null;

    private Motors motor_A = null;
    private Motors motor_B = null;
    private Motors motor_C = null;
    private Motors motor_D = null;

    //Unneccessary vars - just for testing
    private IMotorControl motorControl = Robot.getRobotController().getMotorController();
    private ISensorControl sensorControl = Robot.getRobotController().getSensorController();
    private IBrickControl brickControl = Robot.getRobotController().getBrickController();

    private String brickIP = null;
    private int brickTCPport = -1;

    private String msgServerIP = null;
    private int msgServerTCPPort = -1;

    IRobodancerConfig robotConfig;

    private HashMap<String,IStatemachine> statemachines = new HashMap<String,IStatemachine>();

    // ----------------------- Verwendete Variablen ------------------------------

    private final String regexIP = "((((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)[.]){3})(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?){1}){1}";

    // --------------------------------------------------------------------------

    private Robot myRobot;
    private RobotCommandCenter myRobotCommandCenter;



    public RobotFactory(){
        myRobot = Robot.getInstance();
        myRobotCommandCenter = new RobotCommandCenter(myRobot);
    }

    @Override
    public void setRobotConfig(IRobodancerConfig robotConfig) {
        this.robotConfig = robotConfig;
    }

    @Override
    public IRobotCommandCenter createRobot() {
        RobotCommandCenter robotCommandCenter = new RobotCommandCenter(myRobot);


        if(robotConfig != null) {
            //Sensors
            sensor_S1 = robotConfig.getSensorAtPortS1();
            sensor_S2 = robotConfig.getSensorAtPortS2();
            sensor_S3 = robotConfig.getSensorAtPortS3();
            sensor_S4 = robotConfig.getSensorAtPortS4();

            //Sensormodes
            mode_S1 = robotConfig.getSensorModeOfS1();
            mode_S2 = robotConfig.getSensorModeOfS2();
            mode_S3 = robotConfig.getSensorModeOfS3();
            mode_S4 = robotConfig.getSensorModeOfS4();

            //Motors
            motor_A = robotConfig.getMotorAtPortA();
            motor_B = robotConfig.getMotorAtPortB();
            motor_C = robotConfig.getMotorAtPortC();
            motor_D = robotConfig.getMotorAtPortD();
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
                myRobot.setSensor_S1(robotConfigurator.createSensor(EV3PortIDs.PORT_1));
                myRobot.setSensor_S2(robotConfigurator.createSensor(EV3PortIDs.PORT_2));
                myRobot.setSensor_S3(robotConfigurator.createSensor(EV3PortIDs.PORT_3));
                myRobot.setSensor_S4(robotConfigurator.createSensor(EV3PortIDs.PORT_4));

                myRobot.setMotor_A(robotConfigurator.createMotor(EV3PortIDs.PORT_A));
                myRobot.setMotor_B(robotConfigurator.createMotor(EV3PortIDs.PORT_B));
                myRobot.setMotor_C(robotConfigurator.createMotor(EV3PortIDs.PORT_C));
                myRobot.setMotor_D(robotConfigurator.createMotor(EV3PortIDs.PORT_D));
            } catch (PortIsAlreadyInUseException e) {
                robotCommandCenter = null;
                e.printStackTrace();
            }

            // --- Connect Sensorobject --> with RobotContextStateListener
            if(myRobot.getSensor_S1() != null){
                myRobot.getSensor_S1().registerListener(RobotContextStateListener.getInstance());
            }
            if(myRobot.getSensor_S2() != null){
                myRobot.getSensor_S2().registerListener(RobotContextStateListener.getInstance());
            }
            if(myRobot.getSensor_S3() != null){
                myRobot.getSensor_S3().registerListener(RobotContextStateListener.getInstance());
            }
            if(myRobot.getSensor_S4() != null){
                myRobot.getSensor_S4().registerListener(RobotContextStateListener.getInstance());
            }

            myRobot.setBrick(robotConfigurator.getBrick());


            // Connect statemachines RobotContextStateEvaluator etc with state machine
            IRobotContextStateEvaluator evaluator = new RobotContextStateEvaluator();
            myRobot.getStatemachineManager().addConstraintEvalauator(evaluator);
            RobotContextStateManager.getInstance().addRobotContextStateEvaluator(evaluator);




            //TODO Create TimeEventProducer (CLK) to call handleCLK()
            /*
            if(RobotContextStateManager.getInstance() instanceof IClockListener){
                ((IClockListener) RobotContextStateManager.getInstance()).handleCLK();
            }
            */

            /**
             *
             * Starte RobotConfigurator

             * Connect motors...
             *
             * Connect RobodancerControl with Robot instance
             */


        }else{
            //TODO may throw exception
            System.err.println("RobotFactory.createRobot(): Error appeard while creating robot!");
            robotCommandCenter = null;
        }
        System.out.println("The RobotFactory created a Robot with the following setup:\n"+toString());
        return robotCommandCenter;
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
    public void addStatemachine(IStatemachine statemachine) {
        myRobot.getStatemachineManager().addStatemachine(statemachine);
    }

    @Override
    public void clear() {
        Sensors sensor_S1 = null;
        Sensors sensor_S2 = null;
        Sensors sensor_S3 = null;
        Sensors sensor_S4 = null;

        SensorMessages.SensorMode_ mode_S1 = null;
        SensorMessages.SensorMode_ mode_S2 = null;
        SensorMessages.SensorMode_ mode_S3 = null;
        SensorMessages.SensorMode_ mode_S4 = null;

        Motors motor_A = null;
        Motors motor_B = null;
        Motors motor_C = null;
        Motors motor_D = null;

        String brickIP = null;
        int brickTCPport = -1;

        String msgServerIP = null;
        int msgServerTCPPort = -1;
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
                ",\n motorControl=" + motorControl +
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
