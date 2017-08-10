package org.mindroid.api.robot;

import org.mindroid.api.errorhandling.AbstractErrorHandler;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.impl.statemachine.StatemachineCollection;

/**
 * Created by torben on 02.03.2017.
 */

public interface IRobotFactory {

    public void setRobotConfig(IRobotPortConfig robotConfig);

    public void setBrickIP(String ip);
    public void setBrickTCPPort(int tcpPort);

    public void setMSGServerIP(String msgServerIP);
    public void setMSGServerTCPPort(int tcpPort);

    void setRobotServerPort(int robotServerPort);

    public void setRobotID(String robotID);

    public void addStatemachine(StatemachineCollection statemachines);

    void addErrorHandler(AbstractErrorHandler errorHandler);

    /** Clears all properties **/
    public void clearConfiguration();

    public IRobotCommandCenter createRobot(); //TODO return robodancer or singleton or both

}
