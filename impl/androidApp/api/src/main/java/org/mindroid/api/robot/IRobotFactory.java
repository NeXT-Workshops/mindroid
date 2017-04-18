package org.mindroid.api.robot;

import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.robot.control.ISensorControl;
import org.mindroid.api.statemachine.IStatemachine;

/**
 * Created by torben on 02.03.2017.
 */

public interface IRobotFactory {

    public void setRobotConfig(IRobodancerConfig robotConfig);

    public void setBrickIP(String ip);
    public void setBrickTCPPort(int tcpPort);

    public void setMSGServerIP(String msgServerIP);
    public void setMSGServerTCPPort(int tcpPort);

    public void setRobotID(String robotID);

    //TODO public void addRuleSet(HasmMap<RobotEvent,Rule> rules);

    public void addStatemachine(IStatemachine statemachine);

    /** Clears all properties **/
    public void clear();

    public IRobotCommandCenter createRobot(); //TODO return robodancer or singleton or both

}
