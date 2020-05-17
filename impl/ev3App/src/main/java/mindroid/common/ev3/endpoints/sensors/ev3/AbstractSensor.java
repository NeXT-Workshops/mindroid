package mindroid.common.ev3.endpoints.sensors.ev3;

import java.util.ArrayList;

import lejos.hardware.sensor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.filter.MedianFilter;
import org.mindroid.common.messages.hardware.Sensors;
import lejos.hardware.port.Port;
import org.mindroid.common.messages.hardware.Sensormode;

/**
 * Created by torben on 27.01.2017.
 */

public abstract class AbstractSensor {

    BaseSensor sensor = null;
    Sensors sensortype = null;
    Sensormode sensormode = null;
    Port sensorPort;

    final long samplePeriodLength;
    boolean isSensorCreated = false;

    ArrayList<SensorListener> listener = new ArrayList<SensorListener>(1);


    /**
     * Create an Abstract Sensor
     * @param sensortype
     * @param sensorPort
     * @param mode
     * @param samplePeriodLength
     */
    public AbstractSensor(Sensors sensortype, Port sensorPort, Sensormode mode, long samplePeriodLength){
        this.sensortype = sensortype;
        this.sensorPort = sensorPort;
        this.sensormode = mode;
        this.samplePeriodLength = samplePeriodLength;
    }

    /**
     * Creates the specified Sensor
     * @return  true if creation was a success
     *          false if an error appeared
     */
    public boolean create() {
        if(this.sensortype == null || this.sensorPort == null || this.sensormode == null){
            System.out.println("[AbstractSensor:create] - Error - Port,Type or Mode is null");
            return false;
        }

        //this.sensorPort = sensorPort;

        try {
            switch (this.sensortype) {
                case EV3ColorSensor:
                    this.sensor = new EV3ColorSensor(this.sensorPort);
                    this.sensortype = Sensors.EV3ColorSensor;
                    break;
                case EV3UltrasonicSensor:
                    this.sensor = new EV3UltrasonicSensor(this.sensorPort);
                    this.sensortype = Sensors.EV3UltrasonicSensor;
                    break;
                case EV3GyroSensor:
                    this.sensor = new EV3GyroSensor(this.sensorPort);
                    this.sensortype = Sensors.EV3GyroSensor;
                    break;
                case EV3IRSensor:
                    this.sensor = new EV3IRSensor(this.sensorPort);
                    this.sensortype = Sensors.EV3IRSensor;
                    break;
                case EV3TouchSensor:
                    this.sensor = new EV3TouchSensor(this.sensorPort);
                    this.sensortype = Sensors.EV3TouchSensor;
                    break;
                default:
                    return false;
            }
        }catch(IllegalArgumentException IAE){
            /* May appear while creating a Sensor (Invalid Sensor mode) - error in lejos */
            return false;
        }

        //set sensormode
        setSensorMode(this.sensormode);

        return true;
    }

    public abstract boolean setSensorMode(Sensormode newMode);

    protected void sendSensorData() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                MedianFilter filter = new MedianFilter(sensor, 5);
                float[] sample = new float[sensor.sampleSize()];
                while (sensor != null /*&& filter != null*/) {
                    try {
                        //Check if array length is correct. Wrong length can appear when sensormode changes
                        if(sample.length != sensor.sampleSize()){
                            sample = new float[sensor.sampleSize()];
                        }

                        //sensor.fetchSample(sample, 0);
                        filter.fetchSample(sample,0);

                        for (SensorListener tmp_listener : listener) {
                            if(tmp_listener != null) {
                                tmp_listener.handleSensorData(sample);
                            }
                        }
                    }catch(IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(samplePeriodLength);
                    } catch (InterruptedException e) {
                        //System.err.println("SensorEndpoint - Thread could not sleep.");
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

    public Sensormode getSensormode() {
        return sensormode;
    }

    /**
     * Sensor Starts sending Data
     */
    public void startSensor(){
        sendSensorData();
    }

}
