package org.mindroid.android.app.fragments.myrobot;


import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;

import java.util.HashMap;

import static org.mindroid.common.messages.hardware.Sensors.*;


public class HardwareMapping {

    private static HashMap<String,Motors> motorMapping;
    private static HashMap<String,Sensors> sensorMapping;
    private static HashMap<String,Sensormode> modeMapping;

    public static String notDefined = "-";

    //TODO Refactor -> put code of initMapping in a static{} block - remove boolean 'isMappingInitialized' and method

    static {
        //Motors
        motorMapping = new HashMap(3);
        motorMapping.put(notDefined,null);
        motorMapping.put(Motors.LargeRegulatedMotor.getName(),Motors.LargeRegulatedMotor);
        motorMapping.put(Motors.MediumRegulatedMotor.getName(),Motors.MediumRegulatedMotor);

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
        modeMapping.put(Sensormode.RED.getValue(), Sensormode.RED);
        modeMapping.put(Sensormode.AMBIENT.getValue(),Sensormode.AMBIENT);
        modeMapping.put(Sensormode.COLOR_ID.getValue(),Sensormode.COLOR_ID);
        modeMapping.put(Sensormode.RGB.getValue(),Sensormode.RGB);
        modeMapping.put(Sensormode.DISTANCE.getValue(),Sensormode.DISTANCE);
        modeMapping.put(Sensormode.LISTEN.getValue(),Sensormode.LISTEN);
        modeMapping.put(Sensormode.SEEK.getValue(),Sensormode.SEEK);
        modeMapping.put(Sensormode.ANGLE.getValue(),Sensormode.ANGLE);
        modeMapping.put(Sensormode.RATE.getValue(), Sensormode.RATE);
        modeMapping.put(Sensormode.RATEANDANGLE.getValue(),Sensormode.RATEANDANGLE);
        modeMapping.put(Sensormode.TOUCH.getValue(),Sensormode.TOUCH);
    }

    /**
     * Returns the sensormode mapped to the given parameter
     * @param key_mode - value/key of SensorMode
     * @return Enum of mapped Sensormode
     */
    public static Sensormode getSensorMode(String key_mode){
        return modeMapping.get(key_mode);
    }

    /**
     * Returns the Sensortype mapped to the given parameter
     * @param key_type - value/key of Sensor
     * @return Enum of mapped Sensor
     */
    public static Sensors getSensorType(String key_type){
        return sensorMapping.get(key_type);
    }

    /**
     * Returns the motortype mapped to the given parameter
     * @param key_type key of the motor
     * @return Enum of mapped Motor
     */
    public static Motors getMotorType(String key_type){
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
