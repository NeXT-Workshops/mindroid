package org.mindroid.impl.robot.context;

import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.sensor.EV3SensorEvent;

import java.util.HashMap;

/**
 * Created by torben on 24.03.2017.
 */
public class StartCondition {

    long t_state_active;
    HashMap<EV3PortID,IEV3SensorEvent> relative_position;

    private static StartCondition ourInstance = new StartCondition();

    private StartCondition(){
        relative_position = new HashMap<EV3PortID,IEV3SensorEvent>(4);
        t_state_active = 0;
    }

    /**
     *
     * Add the Position of the GyroSensor event as start Condition.
     * Checks if the event is a valid GyroSensor-event or null.
     * Events will only be added if its a valid Gyro-Sensor Event.
     *
     * @param portID
     * @param pos_event
     */
    public void addPosition(EV3PortID portID, IEV3SensorEvent pos_event){
        if(pos_event == null || portID == null){
            return;
        }
        //Check if event is from a gyrosensor
        if(SensorMessages.SensorMode_.ANGLE == pos_event.getSensorMode() || SensorMessages.SensorMode_.RATEANDANGLE == pos_event.getSensorMode()) {
            relative_position.put(portID, pos_event);
        }else{
            if(relative_position.containsKey(portID)){
                relative_position.remove(portID);
            }
        }
    }

    public IEV3SensorEvent getPosition(EV3PortID portID){
        return relative_position.get(portID);
    }

    public long getStateActiveTime(){
        return t_state_active;
    }

    public void setStateActiveTime(long t_state_active){
        this.t_state_active = t_state_active;
    }

    public static StartCondition getInstance(){
        return ourInstance;
    }

    @Override
    public String toString() {
        return "StartCondition{" +
                "t_state_active=" + t_state_active +
                ", relative_position=" + relative_position +
                '}';
    }
}
