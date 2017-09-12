package org.mindroid.impl.robot;

import org.mindroid.api.communication.IMessenger;

/**
 * Created by torben on 27.03.2017.
 */
public class RobotController {

    private MotorProvider motorControl;
    private BrickController brickController;
    private SensorController sensorControl;
    private IMessenger messenger;
    private String robotID = "";

    private Robot robot;

    public RobotController(Robot robot){
        this.robot = robot;

        this.motorControl = new MotorProvider(robot);
        this.sensorControl = new SensorController(robot);
        this.brickController = new BrickController(robot);

        this.robotID = robot.robotID;
    }

    //TODO may add motortype as parameter to return specific controler for the motortype
    public MotorProvider getMotorController() {
        return motorControl;
    }

    public BrickController getBrickController() {
        return brickController;
    }

    public SensorController getSensorController() {
        return sensorControl;
    }

    public IMessenger getMessenger() {
        if(this.robot.isMessageingEnabled()){
            return robot.getMessenger();
        }else{
            return null;
        }

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
