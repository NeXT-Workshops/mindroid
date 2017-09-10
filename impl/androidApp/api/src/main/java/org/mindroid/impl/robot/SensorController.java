package org.mindroid.impl.robot;

import org.mindroid.api.robot.control.ISensorControl;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.sensor.EV3Sensor;


/**
 * Created by torben on 02.03.2017.
 */

public class SensorController implements ISensorControl{

    private Robot robot;

    public SensorController(Robot robot){
        this.robot = robot;
    }

    /**
     * Change the sensor mode
     * @param sensport port of the sensor
     * @param mode new sensor mode
     */
    @Override
    public void changeSensorMode(EV3PortID sensport, Sensormode mode) {
        EV3Sensor sensor = getSensor(sensport);
        if(sensor != null){
            sensor.changeSensorToMode(mode);
        }else{
            System.err.println("Unknown sensorport "+sensport);
        }
    }

    private EV3Sensor getSensor(EV3PortID sensport){
        if(sensport.equals(EV3PortIDs.PORT_1)){
            return robot.getSensorS1();
        }else if(sensport.equals(EV3PortIDs.PORT_2)){
            return  robot.getSensorS2();
        }else if(sensport.equals(EV3PortIDs.PORT_3)){
            return  robot.getSensorS3();
        }else if(sensport.equals(EV3PortIDs.PORT_4)){
            return  robot.getSensorS4();
        }

        return null;
    }

}
