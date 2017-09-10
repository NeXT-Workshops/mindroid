package org.mindroid.android.app.fragments.sensormonitoring;


import org.mindroid.api.sensor.IEV3SensorEvent;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.impl.ev3.EV3PortID;


import java.util.Observable;

/**
 * Created by torben on 16.08.2017.
 */
public class SensorListener extends Observable implements IEV3SensorEventListener {

    public static final int SIZE_LAST_VALUES = 5;

    private float[] values;

    private EV3PortID port;
    private Sensormode mode;


    public SensorListener(EV3PortID port){
        this.port = port;
    }


    @Override
    public void handleSensorEvent(EV3PortID port, IEV3SensorEvent event) {
        //System.out.println("[APP:SensorListener:handleSensorEvent] Called "+event);
        this.mode = event.getSensorMode();
        if(port.equals(this.port)){
            this.mode = event.getSensorMode();
            addValue(event.getSample()); //TODO get that working with bigger samples
        }
    }

    private void addValue(float[] value){
        this.values = value;

        setChanged();
        notifyObservers();
    }

    public float getValue(int i){
        if(values != null && i < values.length) {
            return values[i];
        }else{
            return -1f;
        }
    }

    public int getValueSize(){

        if(values != null) {
            return values.length;
        }else{
            return 0;
        }
    }

    public Sensormode getMode() {
        return mode;
    }

    public void reset(){
        mode = null;
        values = null;
    }


}
