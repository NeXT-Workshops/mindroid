package mindroid.common.ev3.endpoints.sensors.ev3;

import lejos.robotics.filter.MeanFilter;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3UltrasonicSensor extends AbstractSensor {

    public EV3UltrasonicSensor(Port sensorPort) {
        super(Sensors.EV3UltrasonicSensor,sensorPort,Sensors.EV3UltrasonicSensor.getModes()[0],SensorSampleRates.SENS_ULTRASONIC_SAMPLERATE);
    }

    @Override
    public boolean setSensorMode(Sensormode newMode) {
        if(Sensors.EV3UltrasonicSensor.isValidMode(newMode)){
            sensor.setCurrentMode(newMode.getValue());
            return true;
        }
        return false;
    }
    
	@Override
	public String toString() {
		return "EV3UltrasonicSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", sampleRate=" + sampleRate + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}

	@Override
    protected void sendSensorData() {
        //Uses Meanfilter for the data
        MeanFilter tmpFilter = null;
        if(sensor != null) {
            tmpFilter = new MeanFilter(sensor, 5);
        }
        final MeanFilter filter = tmpFilter;

        Runnable run = new Runnable() {
            @Override
            public void run() {
                float[] sample = new float[filter.sampleSize()];
                while (filter != null /*&& filter != null*/) {
                    try {
                        filter.fetchSample(sample, 0);

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
                        //System.err.println("SensorEndpoint - Thread could not sleep.");
                    }
                }
            }
        };
        new Thread(run).start(); //Starts sending data
    }


}
