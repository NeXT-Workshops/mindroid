package mindroid.common.ev3.endpoints.sensors.ev3;

import java.util.ArrayList;

import lejos.hardware.sensor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.filter.MeanFilter;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public abstract class AbstractSensor {

    BaseSensor sensor = null;
    Sensors sensortype = null;
    SensorMessages.SensorMode_ sensormode = null;
    Port sensorPort;

    final long sampleRate;
    boolean isSensorCreated = false;

    ArrayList<SensorListener> listener = new ArrayList<SensorListener>(1);

    public AbstractSensor(long sampleRate){
        this.sampleRate = sampleRate;
    }

    protected boolean create(Port sensorPort,Sensors sensortype,SensorMessages.SensorMode_ sensormode) {
        assert sensortype != null && sensorPort != null && sensormode != null;

        this.sensorPort = sensorPort;
        
        switch(sensortype){
            case EV3ColorSensor:
                this.sensor = new EV3ColorSensor(sensorPort);
                this.sensortype = Sensors.EV3ColorSensor;
                break;
            case EV3UltrasonicSensor:
            	this.sensor = new EV3UltrasonicSensor(sensorPort);
            	this.sensortype = Sensors.EV3UltrasonicSensor;
                break;
            case EV3GyroSensor:
            	this.sensor = new EV3GyroSensor(sensorPort);
            	this.sensortype = Sensors.EV3GyroSensor;
                break;
            case EV3IRSensor:
            	this.sensor = new EV3IRSensor(sensorPort);
            	this.sensortype = Sensors.EV3IRSensor;
                break;
            case EV3TouchSensor:
            	this.sensor = new EV3TouchSensor(sensorPort);
            	this.sensortype = Sensors.EV3TouchSensor;
                break;
            default: return false;
        }

        if(sensor != null && setSensorMode(sensormode)){
            this.sensormode = sensormode;
        }
        return true;
    }

    public abstract boolean setSensorMode(SensorMessages.SensorMode_ newMode);

    protected void sendSensorData() {
        //TODO may use MeanFilter
        /*MeanFilter tmpFilter = null;
        if(sensor != null) {
            tmpFilter = new MeanFilter(sensor, 7);
        }
        final MeanFilter filter = tmpFilter;*/

        Runnable run = new Runnable() {
            @Override
            public void run() {
                float[] sample = new float[sensor.sampleSize()];
                while (sensor != null /*&& filter != null*/) {
                    try {

                        sensor.fetchSample(sample, 0);
                        //filter.fetchSample(sample,0);

                        for (SensorListener tmp_listener : listener) {
                            if(tmp_listener != null) {
                                tmp_listener.handleSensorData(sample);
                            }
                        }
                    }catch(IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(sampleRate);
                    } catch (InterruptedException e) {
                        System.err.println("SensorEndpoint - Thread could not sleep.");
                    }
                }
            }
        };
        new Thread(run).start(); //Starts sending data
    }
    
    /**
     * Only one listener is allowed!
     * @param tmpListener
     */
    public void addListener(SensorListener tmpListener){
    	this.listener.clear();
    	this.listener.add(tmpListener);
    }

    public BaseSensor getSensor() {
        return sensor;
    }

    public Sensors getSensortype() {
        return sensortype;
    }

    public int getSampleSize(){
        return sensor.sampleSize();
    }


}
