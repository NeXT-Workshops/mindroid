package org.mindroid.impl.robot.context;

import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.sensor.EV3SensorEvent;

import java.util.HashMap;

/**
 * Created by torben on 24.03.2017.
 */
public class StartCondition {

    long t_state_active;
    HashMap<EV3PortID,EV3SensorEvent> relative_position;

    private static StartCondition ourInstance = new StartCondition();

    private StartCondition(){
        relative_position = new HashMap<EV3PortID,EV3SensorEvent>();
        t_state_active = 0;
    }

    public void addPosition(EV3PortID portID, EV3SensorEvent pos_event){
        //TODO may check if correct sensormode
        relative_position.put(portID,pos_event);
    }

    public EV3SensorEvent getPosition(EV3PortID portID){
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
