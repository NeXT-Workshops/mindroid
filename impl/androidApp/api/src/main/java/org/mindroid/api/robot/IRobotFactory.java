package org.mindroid.api.robot;

import org.mindroid.api.ImperativeAPI;
import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.statemachine.StatemachineCollection;

/**
 * Created by torben on 02.03.2017.
 */

public interface IRobotFactory {

    // ------- Robot Characteristics -------
    public void setRobotConfig(IRobotPortConfig robotConfig);

    public void setBrickIP(String ip);

    public void setBrickTCPPort(int tcpPort);

    public void setRobotID(String robotID);

    // ------- Properties for Messaging -------
    public void setMSGServerIP(String msgServerIP);

    public void setMSGServerTCPPort(int tcpPort);

    void setRobotServerPort(int robotServerPort);

    // ------- Statemachine Enginge Initialization -------

    void addStatemachine(StatemachineCollection statemachines);

    // ------- Imperative Engine Initialization -------

    void addImperativeImplementation(ImperativeAPI imperativeImplementation);

    // ------- Controlling ------- //TODO Maybe move some of them to the RobotCommandCenter

    void registerSensorListener(EV3PortID port, IEV3SensorEventListener listener);

    void addErrorHandler(AbstractErrorHandler errorHandler);

    /** Clears all properties **/
     void clearConfiguration();

    IRobotCommandCenter createRobot();

}
