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
    void setRobotConfig(IRobotPortConfig robotConfig);

    void setBrickIP(String ip);

    void setBrickTCPPort(int tcpPort);

    void setRobotID(String robotID);

    IRobotCommandCenter getRobotCommandCenter();

    // ------- Statemachine Enginge Initialization -------

    void addStatemachine(StatemachineCollection statemachines);

    // ------- Imperative Engine Initialization -------

    void addImperativeImplementation(ImperativeAPI imperativeImplementation);

    // ------- Controlling ------- //TODO Maybe move some of them to the RobotCommandCenter

    void registerSensorListener(EV3PortID port, IEV3SensorEventListener listener);

    void addErrorHandler(AbstractErrorHandler errorHandler);

    IRobotCommandCenter createRobot(boolean enableSimulation);

    /** Clears all properties **/
     void clearConfiguration();



}
