package org.mindroid.api;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.*;
import org.mindroid.impl.statemachine.StatemachineCollection;

/**
 * Created by Torbe on 03.05.2017.
 */
public abstract class LVL1API implements IMindroidMain {

    public StatemachineCollection statemachineCollection = new StatemachineCollection();

    public RobotController robotController = Robot.getRobotController();
    public MotorProvider motorProvider = robotController.getMotorController();
    public BrickController brickController = robotController.getBrickController();
    public SensorController sensorController = robotController.getSensorController();
    //private IMessenger messenger = Robot.getRobotController().getMessenger();
    //public String myRobotID = Robot.getRobotController().getRobotID();

    @Override
    public final StatemachineCollection getStatemachineCollection() {
        return  statemachineCollection;
    }

    /**
     * Forwards motors at ports A and D.
     * Speed is set to 50.
     */
    public void forward() {
        motorProvider.setMotorDirection(EV3PortIDs.PORT_A, MotorDirection.FORWARD);
        motorProvider.setMotorDirection(EV3PortIDs.PORT_D, MotorDirection.FORWARD);
        motorProvider.setMotorSpeed(EV3PortIDs.PORT_A,50);
        motorProvider.setMotorSpeed(EV3PortIDs.PORT_D,50);
    }

    /**
     * Backwards motors at ports A and D
     * Speed is set to 50.
     */
    public void backward() {
        motorProvider.setMotorDirection(EV3PortIDs.PORT_A, MotorDirection.BACKWARD);
        motorProvider.setMotorDirection(EV3PortIDs.PORT_D, MotorDirection.BACKWARD);
        motorProvider.setMotorSpeed(EV3PortIDs.PORT_A,50);
        motorProvider.setMotorSpeed(EV3PortIDs.PORT_D,50);
    }

    /**
     * Send a Message to anothers Robot 'destination'
     * @param destination 'robotID'
     * @param message 'msg to send'
     */
    public void sendMessage(String destination, String message){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(destination,message);
        }else{
            System.out.println("[LVL1API:sendMessage] Tried to send a message, but the Messenger was null");
        }
    }

    /**
     * TODO UNTESTED; WILL PROBABLY NOT WORK
     *
     * Broadcast a message to all Robots in Group.
     * @param message 'message to send'
     */
    public void broadcastMessage(String message){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.BROADCAST,message);
        }else{
            System.out.println("[LVL1API:broadcastMessage] Tried to broadcast a message, but the Messenger was null");
        }
    }

    /**
     * The logmessage will be sent to the message-server and displayed.
     * @param logmessage 'the log message'
     */
    public void sendLogMessage(String logmessage){
        if(Robot.getRobotController().getMessenger() != null){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,logmessage);
        }else{
            System.out.println("[LVL1API:sendLogMessage] Tried to send a logmessage, but the Messenger was null");
        }
    }

    /**
     *
     * @return robotID
     */
    public String getRobotID(){
        return Robot.getRobotController().getRobotID();
    }

}
