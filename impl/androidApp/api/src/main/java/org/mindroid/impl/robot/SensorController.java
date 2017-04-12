package org.mindroid.impl.robot;

import org.mindroid.api.robot.control.ISensorControl;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.sensor.EV3Sensor;


/**
 * Created by torben on 02.03.2017.
 */

public class SensorController implements ISensorControl {

    private Robot robot;

    public SensorController(Robot robot){
        this.robot = robot;
    }

    @Override
    public void changeSensorMode(EV3PortID sensport, SensorMessages.SensorMode_ mode) {
        EV3Sensor sensor = getSensor(sensport);
        if(sensor != null){
            sensor.changeSensorToMode(mode);
        }else{
            System.err.println("Unknown sensorport "+sensport);
        }
    }

    private EV3Sensor getSensor(EV3PortID sensport){
        if(sensport.equals(EV3PortIDs.PORT_1)){
            return robot.getSensor_S1();
        }else if(sensport.equals(EV3PortIDs.PORT_2)){
            return  robot.getSensor_S2();
        }else if(sensport.equals(EV3PortIDs.PORT_3)){
            return  robot.getSensor_S3();
        }else if(sensport.equals(EV3PortIDs.PORT_4)){
            return  robot.getSensor_S4();
        }

        return null;
    }
}
