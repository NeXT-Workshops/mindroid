package org.mindroid.impl.robot.context;

import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.ITimeEventListener;
import org.mindroid.api.IMessageListener;
import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.api.statemachine.ITimeEvent;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.Robot;

import java.util.ArrayList;


/**
 * Created by torben on 11.03.2017.
 */
public class RobotContextState implements IRobotContextState,IEV3SensorEventListener,IMessageListener,ITimeEventListener {

    private static RobotContextState ourInstance = new RobotContextState();

    IEV3SensorEvent sensor_output_S1;
    IEV3SensorEvent sensor_output_S2;
    IEV3SensorEvent sensor_output_S3;
    IEV3SensorEvent sensor_output_S4;

    ArrayList<ITimeEvent> receivedTimeEvents;
    ArrayList<MindroidMessage> receivedMessages;

    StartCondition startCondition;

    public RobotContextState(){
        startCondition = StartCondition.getInstance();
        this.receivedTimeEvents = new ArrayList<ITimeEvent>();
        this.receivedMessages = new ArrayList<MindroidMessage>();
    }


    @Override
    public synchronized IEV3SensorEvent getSensorEvent(final EV3PortID sensorPort) {
        if(sensorPort.equals(EV3PortIDs.PORT_1)){
            return sensor_output_S1;
        }
        if(sensorPort.equals(EV3PortIDs.PORT_2)){
            return sensor_output_S2;
        }

        if(sensorPort.equals(EV3PortIDs.PORT_3)){
            return sensor_output_S3;
        }

        if(sensorPort.equals(EV3PortIDs.PORT_4)){
            return sensor_output_S4;
        }

        throw new IllegalArgumentException("Unallowed Port(as Method-Paramert): "+sensorPort);

    }

    @Override
    public synchronized ArrayList<ITimeEvent> getTimeEvents() {
        return receivedTimeEvents;
    }

    @Override
    public synchronized ArrayList<MindroidMessage> getMessages() {
        return receivedMessages;
    }


    @Override
    public synchronized Sensormode getSensorMode(EV3PortID sensorPort) {

        if(sensorPort.equals(EV3PortIDs.PORT_1)){
            return sensor_output_S1.getSensorMode();
        }
        if(sensorPort.equals(EV3PortIDs.PORT_2)){
            return sensor_output_S2.getSensorMode();
        }

        if(sensorPort.equals(EV3PortIDs.PORT_3)){
            return sensor_output_S3.getSensorMode();
        }

        if(sensorPort.equals(EV3PortIDs.PORT_4)){
            return sensor_output_S4.getSensorMode();
        }

        throw new IllegalArgumentException("Unallowed Port(as Method-Paramert): "+sensorPort);
    }


    @Override
    public synchronized void handleSensorEvent(EV3PortID sensorPort, IEV3SensorEvent event) {
        //System.out.println("Handle sensor event called"+event);

        if(sensorPort.equals(EV3PortIDs.PORT_1)){
            this.sensor_output_S1 = event;
            return;
        }
        if(sensorPort.equals(EV3PortIDs.PORT_2)){
            this.sensor_output_S2 = event;
            return;
        }

        if(sensorPort.equals(EV3PortIDs.PORT_3)){
            this.sensor_output_S3 = event;
            return;
        }

        if(sensorPort.equals(EV3PortIDs.PORT_4)){
            this.sensor_output_S4 = event;
            return;
        }

        throw new IllegalArgumentException("Unallowed Port(as Method-Paramert): "+sensorPort);
    }



    public static RobotContextState getInstance() {
        return ourInstance;
    }


    @Override
    public synchronized void handleTimeEvent(ITimeEvent timeEvent) {
        //Gets Called form TimeEventProducer
        System.out.println("RobotContextListener.handleTimeEvent(): called with: "+timeEvent);
        receivedTimeEvents.add(timeEvent);
    }


    public synchronized void setSensor_output_S1(IEV3SensorEvent sensor_output_S1) {
        this.sensor_output_S1 = sensor_output_S1;
    }

    public synchronized void setSensor_output_S2(IEV3SensorEvent sensor_output_S2) {
        this.sensor_output_S2 = sensor_output_S2;
    }

    public synchronized void setSensor_output_S3(IEV3SensorEvent sensor_output_S3) {
        this.sensor_output_S3 = sensor_output_S3;
    }

    public synchronized void setSensor_output_S4(IEV3SensorEvent sensor_output_S4) {
        this.sensor_output_S4 = sensor_output_S4;
    }

    public synchronized void setReceivedTimeEvents(ArrayList<ITimeEvent> receivedTimeEvents) {
        this.receivedTimeEvents = receivedTimeEvents;
    }

    public synchronized void setReceivedMessages(ArrayList<MindroidMessage> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public synchronized StartCondition getStartCondition() {
        return startCondition;
    }

    @Override
    public synchronized void handleMessage(MindroidMessage msg) {

        if(Robot.getInstance().isMessageingEnabled()){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"I received a message!");
        }
        receivedMessages.add(msg);
    }

    @Override
    public synchronized String toString() {
        return "RobotContextState{" +
                "sensor_output_S1=" + sensor_output_S1 +
                ", sensor_output_S2=" + sensor_output_S2 +
                ", sensor_output_S3=" + sensor_output_S3 +
                ", sensor_output_S4=" + sensor_output_S4 +
                ", receivedTimeEvents=" + receivedTimeEvents +
                ", receivedMessages=" + receivedMessages +
                ", startCondition=" + startCondition +
                '}';
    }
}
