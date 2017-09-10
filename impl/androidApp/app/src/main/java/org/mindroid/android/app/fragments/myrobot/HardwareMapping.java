package org.mindroid.android.app.fragments.myrobot;

import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;

import java.util.HashMap;

import static org.mindroid.common.messages.Sensors.*;


public class HardwareMapping {

    private static boolean isMappingInitialized = false;
    private static HashMap<String,Motors> motorMapping;
    private static HashMap<String,Sensors> sensorMapping;
    private static HashMap<String,SensorMessages.SensorMode_> modeMapping;

    public static String notDefined = "-";

    private static void initMapping() {
        if(!isMappingInitialized){
            //Motors
            motorMapping = new HashMap(3);
            motorMapping.put(notDefined,null);
            motorMapping.put(Motors.UnregulatedMotor.getName(),Motors.UnregulatedMotor);
            motorMapping.put(Motors.LargeRegulatedMotor.getName(),Motors.LargeRegulatedMotor);

            //Sensors
            sensorMapping = new HashMap(6);
            sensorMapping.put(notDefined,null);
            sensorMapping.put(EV3ColorSensor.getName(),EV3ColorSensor);
            sensorMapping.put(EV3UltrasonicSensor.getName(),EV3UltrasonicSensor);
            sensorMapping.put(EV3TouchSensor.getName(),EV3TouchSensor);
            sensorMapping.put(EV3GyroSensor.getName(),EV3GyroSensor);
            sensorMapping.put(EV3IRSensor.getName(),EV3IRSensor);

            //Sensormodes
            modeMapping = new HashMap(12);
            modeMapping.put(notDefined,null);
            modeMapping.put(SensorMessages.SensorMode_.RED.getValue(), SensorMessages.SensorMode_.RED);
            modeMapping.put(SensorMessages.SensorMode_.AMBIENT.getValue(),SensorMessages.SensorMode_.AMBIENT);
            modeMapping.put(SensorMessages.SensorMode_.COLOR_ID.getValue(),SensorMessages.SensorMode_.COLOR_ID);
            modeMapping.put(SensorMessages.SensorMode_.RGB.getValue(),SensorMessages.SensorMode_.RGB);
            modeMapping.put(SensorMessages.SensorMode_.DISTANCE.getValue(),SensorMessages.SensorMode_.DISTANCE);
            modeMapping.put(SensorMessages.SensorMode_.LISTEN.getValue(),SensorMessages.SensorMode_.LISTEN);
            modeMapping.put(SensorMessages.SensorMode_.SEEK.getValue(),SensorMessages.SensorMode_.SEEK);
            modeMapping.put(SensorMessages.SensorMode_.ANGLE.getValue(),SensorMessages.SensorMode_.ANGLE);
            modeMapping.put(SensorMessages.SensorMode_.RATE.getValue(), SensorMessages.SensorMode_.RATE);
            modeMapping.put(SensorMessages.SensorMode_.RATEANDANGLE.getValue(),SensorMessages.SensorMode_.RATEANDANGLE);
            modeMapping.put(SensorMessages.SensorMode_.TOUCH.getValue(),SensorMessages.SensorMode_.TOUCH);

            isMappingInitialized = true;
        }
    }

    /**
     * Returns the sensormode mapped to the given parameter
     * @param key_mode - value/key of SensorMode
     * @return Enum of mapped Sensormode
     */
    public static SensorMessages.SensorMode_ getSensorMode(String key_mode){
        initMapping();
        return modeMapping.get(key_mode);
    }

    /**
     * Returns the Sensortype mapped to the given parameter
     * @param key_type - value/key of Sensor
     * @return Enum of mapped Sensor
     */
    public static Sensors getSensorType(String key_type){
        initMapping();
        return sensorMapping.get(key_type);
    }

    /**
     * Returns the motortype mapped to the given parameter
     * @param key_type key of the motor
     * @return Enum of mapped Motor
     */
    public static Motors getMotorType(String key_type){
        initMapping();
        return motorMapping.get(key_type);
    }

    /**
     * Returns the motor labels as String array
     * @return array of motor labels
     */
    public static String[] getMotorLabels(){
        return motorMapping.keySet().toArray(new String[motorMapping.keySet().size()]);
    }

    /**
     * Returns the Sensorlabels as a String array
     * @return array of the sensor labels
     */
    public static String[] getSensorLabels(){
        return sensorMapping.keySet().toArray(new String[sensorMapping.keySet().size()]);
    }

}
