package org.mindroid.impl.robot;

import org.mindroid.api.communication.IMessenger;

/**
 * Created by torben on 27.03.2017.
 */
public class RobotController {

    private MotorProvider motorProvider;
    private BrickController brickController;
    private SensorProvider sensorProvider;
    private IMessenger messenger;
    private String robotID = "";

    private Robot robot;

    public RobotController(Robot robot){
        this.robot = robot;

        this.motorProvider = new MotorProvider(robot);
        this.sensorProvider = new SensorProvider(robot);
        this.brickController = new BrickController(robot);

        this.robotID = robot.robotID;
    }

    public MotorProvider getMotorProvider() {
        return motorProvider;
    }

    public BrickController getBrickController() {
        return brickController;
    }

    public SensorProvider getSensorProvider() {
        return sensorProvider;
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
