package org.mindroid.impl.robot;

/**
 * Created by torben on 27.03.2017.
 */
public class RobotController {

    MotorController motorControl;
    BrickController brickController;
    SensorController sensorControl;

    public RobotController(Robot robot){
        this.motorControl = new MotorController(robot);
        this.sensorControl = new SensorController(robot);
        this.brickController = new BrickController(robot);
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
}
