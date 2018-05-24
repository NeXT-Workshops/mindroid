package org.mindroid.impl.robot;

import org.mindroid.api.brick.Brick;
import org.mindroid.impl.communication.MessengerClient;
import org.mindroid.impl.configuration.RobotConfigurator;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.motor.EV3RegulatedMotorEndpoint;
import org.mindroid.impl.motor.SynchronizedMotorsEndpoint;
import org.mindroid.impl.sensor.IEV3SensorEndpoint;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Torben on 01.03.2017.
 */
public final class Robot {

    public String robotID = "No id set";

    private RobotConfigurator robotConfigurator = null;

    Brick brick;

    //Sensor Endpoints
    private IEV3SensorEndpoint sensor_S1 = null;
    private IEV3SensorEndpoint sensor_S2 = null;
    private IEV3SensorEndpoint sensor_S3 = null;
    private IEV3SensorEndpoint sensor_S4 = null;

    //Motor Endpoints
    private EV3RegulatedMotorEndpoint IMotor_A = null;
    private EV3RegulatedMotorEndpoint IMotor_B = null;
    private EV3RegulatedMotorEndpoint IMotor_C = null;
    private EV3RegulatedMotorEndpoint IMotor_D = null;

    //Synchronized Motors Endpoint
    private SynchronizedMotorsEndpoint syncedMotors;

    private boolean isSimulated = false;

    protected MessengerClient messenger = null;

    private static Robot robot = new Robot();

    private static RobotController robotController = new RobotController(getInstance());

    public static Robot getInstance(){
        return robot;
    }

    private static final Logger LOGGER = Logger.getLogger(Robot.class.getName());

    static {
        APILoggerManager.getInstance().registerLogger(LOGGER);
    }

    private Robot(){

    }

    public IEV3SensorEndpoint getSensorS1() {
        return sensor_S1;
    }

    protected RobotConfigurator getRobotConfigurator() {
        return robotConfigurator;
    }

    protected void setRobotConfigurator(RobotConfigurator robotConfigurator) {
        this.robotConfigurator = robotConfigurator;
    }

    protected void setSensorS1(IEV3SensorEndpoint sensor_S1) {
        LOGGER.log(Level.INFO,"SensorEndpoint S1: "+ sensor_S1.hashCode()+" -- "+sensor_S1);
        this.sensor_S1 = sensor_S1;
    }

    protected IEV3SensorEndpoint getSensorS2() {
        return sensor_S2;
    }

    protected void setSensorS2(IEV3SensorEndpoint sensor_S2) {
        LOGGER.log(Level.INFO,"SensorEndpoint S2: "+ sensor_S2.hashCode()+" -- "+sensor_S2);
        this.sensor_S2 = sensor_S2;
    }

    protected IEV3SensorEndpoint getSensorS3() {
        return sensor_S3;
    }

    protected void setSensorS3(IEV3SensorEndpoint sensor_S3) {
        LOGGER.log(Level.INFO,"SensorEndpoint S3: "+ sensor_S3.hashCode()+" -- "+sensor_S3);
        this.sensor_S3 = sensor_S3;
    }

    protected IEV3SensorEndpoint getSensorS4() {
        return sensor_S4;
    }

    protected void setSensorS4(IEV3SensorEndpoint sensor_S4) {
        LOGGER.log(Level.INFO,"SensorEndpoint S4: "+ sensor_S4.hashCode()+" -- "+sensor_S4);
        this.sensor_S4 = sensor_S4;
    }

    protected EV3RegulatedMotorEndpoint getIMotor_A() {
        return IMotor_A;
    }

    protected void setMotorA(EV3RegulatedMotorEndpoint IMotor_A) {
        this.IMotor_A = IMotor_A;
    }

    protected EV3RegulatedMotorEndpoint getIMotor_B() {
        return IMotor_B;
    }

    protected void setMotorB(EV3RegulatedMotorEndpoint IMotor_B) {
        this.IMotor_B = IMotor_B;
    }

    protected EV3RegulatedMotorEndpoint getIMotor_C() {
        return IMotor_C;
    }

    protected void setMotorC(EV3RegulatedMotorEndpoint IMotor_C) {
        this.IMotor_C = IMotor_C;
    }

    protected EV3RegulatedMotorEndpoint getIMotor_D() {
        return IMotor_D;
    }

    protected void setMotorD(EV3RegulatedMotorEndpoint IMotor_D) {
        this.IMotor_D = IMotor_D;
    }

    public SynchronizedMotorsEndpoint getSyncedMotors() {
        return syncedMotors;
    }

    public void setSyncedMotors(SynchronizedMotorsEndpoint syncedMotors) {
        this.syncedMotors = syncedMotors;
    }

    protected Brick getBrick() {
        return brick;
    }

    protected void setBrick(Brick brick) {
        this.brick = brick;
    }

    public static RobotController getRobotController(){
        return robotController;
    }

    protected String getRobotID() {
        return robotID;
    }

    protected void setRobotID(String robotID) {
        this.robotID = robotID;
    }

    public MessengerClient getMessenger() {
        return messenger;
    }

    public boolean isSimulated() {
        return isSimulated;
    }

    public void setSimulated(boolean simulated) {
        isSimulated = simulated;
    }
}
