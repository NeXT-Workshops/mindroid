package org.mindroid.impl.robot;

import org.mindroid.api.motor.Motor;
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

    private Motor motor_A = null;
    private Motor motor_B = null;
    private Motor motor_C = null;
    private Motor motor_D = null;

    private StatemachineManager statemachineManager;

    private static Robot robot = new Robot();

    private static RobotController robotController = new RobotController(getInstance());

    public static Robot getInstance(){
        return robot;
    }

    private Robot(){
        statemachineManager = StatemachineManager.getInstance();
    }

    public EV3Sensor getSensor_S1() {
        return sensor_S1;
    }

    protected RobotConfigurator getRobotConfigurator() {
        return robotConfigurator;
    }

    protected void setRobotConfigurator(RobotConfigurator robotConfigurator) {
        this.robotConfigurator = robotConfigurator;
    }

    protected void setSensor_S1(EV3Sensor sensor_S1) {
        this.sensor_S1 = sensor_S1;
    }

    protected EV3Sensor getSensor_S2() {
        return sensor_S2;
    }

    protected void setSensor_S2(EV3Sensor sensor_S2) {
        this.sensor_S2 = sensor_S2;
    }

    protected EV3Sensor getSensor_S3() {
        return sensor_S3;
    }

    protected void setSensor_S3(EV3Sensor sensor_S3) {
        this.sensor_S3 = sensor_S3;
    }

    protected EV3Sensor getSensor_S4() {
        return sensor_S4;
    }

    protected void setSensor_S4(EV3Sensor sensor_S4) {
        this.sensor_S4 = sensor_S4;
    }

    protected Motor getMotor_A() {
        return motor_A;
    }

    protected void setMotor_A(Motor motor_A) {
        this.motor_A = motor_A;
    }

    protected Motor getMotor_B() {
        return motor_B;
    }

    protected void setMotor_B(Motor motor_B) {
        this.motor_B = motor_B;
    }

    protected Motor getMotor_C() {
        return motor_C;
    }

    protected void setMotor_C(Motor motor_C) {
        this.motor_C = motor_C;
    }

    protected Motor getMotor_D() {
        return motor_D;
    }

    protected void setMotor_D(Motor motor_D) {
        this.motor_D = motor_D;
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

    public String getRobotID() {
        return robotID;
    }

    public void setRobotID(String robotID) {
        this.robotID = robotID;
    }

}
