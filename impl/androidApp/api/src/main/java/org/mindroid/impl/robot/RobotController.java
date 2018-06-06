package org.mindroid.impl.robot;

import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.impl.communication.MessengerClient;

/**
 * Created by torben on 27.03.2017.
 */
public class RobotController {

    private MotorProvider motorProvider;
    private IBrickControl brickController;
    private SensorProvider sensorProvider;
    private MessengerClient messenger;
    private String robotID = "";

    private Robot robot;

    public RobotController(Robot robot){
        this.robot = robot;

        this.motorProvider = new MotorProvider(robot);
        this.sensorProvider = new SensorProvider(robot);

        this.robotID = robot.robotID;
    }

    public MotorProvider getMotorProvider() {
        return motorProvider;
    }

    public IBrickControl getBrickController() {
        return robot.getBrick();
    }

    public SensorProvider getSensorProvider() {
        return sensorProvider;
    }

    public MessengerClient getMessenger() {
        return robot.getMessenger();
    }

    public String getRobotID() {
        return robotID;
    }

    protected void setMessenger(MessengerClient messenger) {
        this.messenger = messenger;
    }

    protected void setRobotID(String robotID) {
        this.robotID = robotID;
    }

    /**
     * Cleans the Controller state, which is necessary, when a new Robot will be created.
     * It assures that new created motor and sensor endpoints can be accessed correctly by the API.
     */
    protected void clearController(){
        sensorProvider.clearSensors();
        motorProvider.clearMotors();
    }
}
