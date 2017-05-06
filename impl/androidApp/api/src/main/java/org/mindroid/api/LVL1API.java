package org.mindroid.api;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.impl.robot.*;
import org.mindroid.impl.statemachine.StatemachineCollection;

/**
 * Created by Torbe on 03.05.2017.
 */
public abstract class LVL1API implements IMindroidMain {

    public StatemachineCollection statemachineCollection = new StatemachineCollection();

    public RobotController robotController = Robot.getRobotController();
    public MotorController motorController = robotController.getMotorController();
    public BrickController brickController = robotController.getBrickController();
    public SensorController sensorController = robotController.getSensorController();
    public IMessenger messenger = Robot.getRobotController().getMessenger();
    public String myRobotID = Robot.getRobotController().getRobotID();

    @Override
    public final StatemachineCollection getStatemachineCollection() throws StateAlreadyExists {
        return  statemachineCollection;
    }

}
