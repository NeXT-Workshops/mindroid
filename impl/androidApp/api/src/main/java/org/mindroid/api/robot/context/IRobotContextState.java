package org.mindroid.api.robot.context;

import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.statemachine.ITimeEvent;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.robot.context.StartCondition;
import org.mindroid.impl.sensor.EV3SensorEvent;

import java.util.ArrayList;

/**
 * Created by torben on 11.03.2017.
 */
public interface IRobotContextState {

    IEV3SensorEvent getSensorEvent(EV3PortID sensorPort);

    ArrayList<ITimeEvent> getTimeEvents();

    ArrayList<MindroidMessage> getMessages();

    Sensormode getSensorMode(EV3PortID sensorPort);

    StartCondition getStartCondition();


}
