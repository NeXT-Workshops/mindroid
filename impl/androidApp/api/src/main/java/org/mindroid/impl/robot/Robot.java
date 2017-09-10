package org.mindroid.impl.robot;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.impl.brick.EV3Brick;
import org.mindroid.impl.configuration.RobotConfigurator;
import org.mindroid.impl.sensor.EV3Sensor;
import org.mindroid.impl.statemachine.StatemachineManager;

/**
 * Created by Torben on 01.03.2017.
 */
public final class Robot {

    public String robotID = "No id set";

    private RobotConfigurator robotConfigurator = null;

    EV3Brick brick;

    private EV3Sensor sensor_S1 = null;
    private EV3Sensor sensor_S2 = null;
    private EV3Sensor sensor_S3 = null;
    private EV3Sensor sensor_S4 = null;

    private RegulatedMotor IMotor_A = null;
    private RegulatedMotor IMotor_B = null;
    private RegulatedMotor IMotor_C = null;
    private RegulatedMotor IMotor_D = null;

    private StatemachineManager statemachineManager;

    protected boolean messageingEnabled = false;
    protected IMessenger messenger = null;

    private static Robot robot = new Robot();

    private static RobotController robotController = new RobotController(getInstance());

    public static Robot getInstance(){
        return robot;
    }

    private Robot(){
        statemachineManager = StatemachineManager.getInstance();
    }

    public EV3Sensor getSensorS1() {
        return sensor_S1;
    }

    protected RobotConfigurator getRobotConfigurator() {
        return robotConfigurator;
    }

    protected void setRobotConfigurator(RobotConfigurator robotConfigurator) {
        this.robotConfigurator = robotConfigurator;
    }

    protected void setSensorS1(EV3Sensor sensor_S1) {
        this.sensor_S1 = sensor_S1;
    }

    protected EV3Sensor getSensorS2() {
        return sensor_S2;
    }

    protected void setSensorS2(EV3Sensor sensor_S2) {
        this.sensor_S2 = sensor_S2;
    }

    protected EV3Sensor getSensorS3() {
        return sensor_S3;
    }

    protected void setSensorS3(EV3Sensor sensor_S3) {
        this.sensor_S3 = sensor_S3;
    }

    protected EV3Sensor getSensorS4() {
        return sensor_S4;
    }

    protected void setSensorS4(EV3Sensor sensor_S4) {
        this.sensor_S4 = sensor_S4;
    }

    protected RegulatedMotor getIMotor_A() {
        return IMotor_A;
    }

    protected void setMotorA(RegulatedMotor IMotor_A) {
        this.IMotor_A = IMotor_A;
    }

    protected RegulatedMotor getIMotor_B() {
        return IMotor_B;
    }

    protected void setMotorB(RegulatedMotor IMotor_B) {
        this.IMotor_B = IMotor_B;
    }

    protected RegulatedMotor getIMotor_C() {
        return IMotor_C;
    }

    protected void setMotorC(RegulatedMotor IMotor_C) {
        this.IMotor_C = IMotor_C;
    }

    protected RegulatedMotor getIMotor_D() {
        return IMotor_D;
    }

    protected void setMotorD(RegulatedMotor IMotor_D) {
        this.IMotor_D = IMotor_D;
    }

    protected StatemachineManager getStatemachineManager() {
        return statemachineManager;
    }

    protected EV3Brick getBrick() {
        return brick;
    }

    protected void setBrick(EV3Brick brick) {
        this.brick = brick;
    }

    protected void setStatemachineManager(StatemachineManager statemachineManager) {
        this.statemachineManager = statemachineManager;
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

    public boolean isMessageingEnabled() {
        return messageingEnabled;
    }

    protected IMessenger getMessenger() {
        return messenger;
    }
}
