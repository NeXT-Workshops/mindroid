package org.mindroid.impl.robot;

import org.mindroid.api.communication.IMessenger;

/**
 * Created by torben on 27.03.2017.
 */
public class RobotController {

    private MotorController motorControl;
    private BrickController brickController;
    private SensorController sensorControl;
    private IMessenger messenger;
    private String robotID = "";


    public RobotController(Robot robot){
        this.motorControl = new MotorController(robot);
        this.sensorControl = new SensorController(robot);
        this.brickController = new BrickController(robot);
        if(robot.isMessageingEnabled()){
            this.messenger = robot.getMessenger();
        }
        this.robotID = robot.robotID;
    }

    //TODO may add motortype as parameter to return specific controler for the motortype
    public MotorController getMotorController() {
        return motorControl;
    }

    public BrickController getBrickController() {
        return brickController;
    }

    public SensorController getSensorController() {
        return sensorControl;
    }

    public IMessenger getMessenger() {
        return messenger;
    }

    public String getRobotID() {
        return robotID;
    }

    protected void setMessenger(IMessenger messenger) {
        this.messenger = messenger;
    }

    protected void setRobotID(String robotID) {
        this.robotID = robotID;
    }
}
