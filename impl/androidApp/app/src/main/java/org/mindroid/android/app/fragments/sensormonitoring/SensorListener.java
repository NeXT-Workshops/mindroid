package org.mindroid.android.app.fragments.sensormonitoring;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.impl.ev3.EV3PortID;

import java.util.Observable;

/**
 * Created by torben on 16.08.2017.
 */
public class SensorListener extends Observable implements IEV3SensorEventListener {

    public static final int SIZE_LAST_VALUES = 5;

    private float[] lastValues;

    private EV3PortID port;

    public SensorListener(EV3PortID port){
        this.port = port;
        getLastValuesArray();
    }


    @Override
    public void handleSensorEvent(EV3PortID port, IEV3SensorEvent event) {
        //System.out.println("[APP:SensorListener:handleSensorEvent] Called "+event);
        if(port.equals(this.port)){
            addValue(event.getSample()[0]); //TODO get that working with bigger samples
        }
    }

    private void addValue(float value){
        for (int i = 0; i < lastValues.length-1; i++) {
            lastValues[i] = lastValues[i+1];
        }
        lastValues[SIZE_LAST_VALUES-1] = value;

        setChanged();
        notifyObservers();
    }

    private void getLastValuesArray() {
        lastValues = new float[SIZE_LAST_VALUES];
        for (int i = 0; i < lastValues.length; i++) {
            lastValues[i] = -1;
        }
    }

    /**
     * Returns the Last Value
     * @param pos index of the value [0;SIZE_LAST_VALUES]
     * @return sensor value
     */
    public float getLastValueAt(int pos){
        if(pos >= 0 && pos < SIZE_LAST_VALUES){
            return lastValues[pos];
        }else{
            return -1f;
        }
    }

    /**
     *
     * @return the last received sensor value
     */
    public float getLastValue(){
        return getLastValueAt(SIZE_LAST_VALUES-1);
    }

    /**
     *
     * @return The avg value of the last SIZE_LAST_VALUES values
     */
    public float getAvgValue() {
        float sum = 0;
        for (float value : lastValues) {
            sum+=value;
        }

        return sum/SIZE_LAST_VALUES;
    }


}
