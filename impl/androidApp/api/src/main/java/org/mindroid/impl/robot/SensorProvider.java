package org.mindroid.impl.robot;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.Motor;
import org.mindroid.impl.sensor.EV3SensorEndpoint;
import org.mindroid.impl.sensor.IEV3SensorEndpoint;
import org.mindroid.impl.sensor.Sensor;

import java.util.HashMap;


/**
 * Created by torben on 02.03.2017.
 */

public class SensorProvider{

    private Robot robot;
    HashMap<EV3PortID,Sensor> sensors;

    public SensorProvider(Robot robot){
        this.robot = robot;
        sensors = new HashMap<EV3PortID, Sensor>(4);
    }

    private IEV3SensorEndpoint getSensorEndpoint(EV3PortID sensport){
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

    /**
     * Returns a Sensor-Object as an simpler interface,
     * @param sensorPort port of the Sensor you want to control.
     * @return Sensor object to control the Sensor plugged into that port
     */
    public Sensor getSensor(EV3PortID sensorPort){
        if(sensorPort == EV3PortIDs.PORT_1 || sensorPort == EV3PortIDs.PORT_2 || sensorPort == EV3PortIDs.PORT_3 ||sensorPort == EV3PortIDs.PORT_4){
            if(sensors.containsKey(sensorPort)){ //Check if motor object already created
                return sensors.get(sensorPort);
            }
            //When not already created, create and put into hashmap
            sensors.put(sensorPort,new Sensor(getSensorEndpoint(sensorPort), sensorPort));//TODO check what happens, when the sensorEndpoint is null? -> Send an info message?

            //This sleep is necessary to get the Sensor-Object up to date (receiving the first event messsage) Otherwise problems can occur. Maybe find another solution
            //TODO this part can be removed -> test with robot first
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return sensors.get(sensorPort);
        }
        return null;
    }


}
