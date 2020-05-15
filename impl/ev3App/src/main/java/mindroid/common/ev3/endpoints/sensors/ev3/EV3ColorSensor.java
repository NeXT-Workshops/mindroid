package mindroid.common.ev3.endpoints.sensors.ev3;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import lejos.hardware.port.Port;

import java.util.HashMap;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3ColorSensor extends AbstractSensor {

    private HashMap colorMapping = new HashMap<Float,Float>(8);

    public EV3ColorSensor(Port sensorPort){
        super(Sensors.EV3ColorSensor,sensorPort,Sensors.EV3ColorSensor.getModes()[0],SensorSampleRates.SENS_COLOR_SAMPLERATE);
        colorMapping = getColorMapping();
    }

    private HashMap<Float,Float> getColorMapping(){
        //colorMap = new int[]{-1, 7, 2, 1, 3, 0, 6, 13}; -> (NONE, BLACK, BLUE, GREEN, YELLOW, RED, WHITE, BROWN) == (-1,0,1,2,3,4,5,6)
        colorMapping = new HashMap<Float,Float>(8);
        colorMapping.put(-1f,-1f);
        colorMapping.put(7f,0f);
        colorMapping.put(2f,1f);
        colorMapping.put(1f,2f);
        colorMapping.put(3f,3f);
        colorMapping.put(0f,4f);
        colorMapping.put(6f,5f);
        colorMapping.put(13f,6f);
        return colorMapping;
    }

    private float getCorrectColorMapping(float value){
        if(colorMapping.containsKey(value)){
            return (float) colorMapping.get(value);
        }else{
            return -1f;
        }
    }

    @Override
    public boolean setSensorMode(Sensormode newMode) {
        if(Sensors.EV3ColorSensor.isValidMode(newMode)){
            sensor.setCurrentMode(newMode.getValue());
            this.sensormode = newMode;
            return true;
        }
        return false;
    }

    @Override
    protected void sendSensorData() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                float[] sample = new float[sensor.sampleSize()];
                while (sensor != null /*&& filter != null*/) {
                    try {
                        //Check if array length is correct. Wrong length can appear when sensormode changes
                        if(sample.length != sensor.sampleSize()){
                            sample = new float[sensor.sampleSize()];
                        }

                        if(sensormode == Sensormode.COLOR_ID) {
                            sample[0] = getCorrectColorMapping(((lejos.hardware.sensor.EV3ColorSensor) sensor).getColorID());
                        }else {
                            sensor.fetchSample(sample, 0);
                        }
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
                        Thread.sleep(samplePeriodLength);
                    } catch (InterruptedException e) {
                        //System.err.println("SensorEndpoint - Thread could not sleep.");
                    }
                }
            }
        };
        new Thread(run).start(); //Starts sending data
    }

	@Override
	public String toString() {
		return "EV3ColorSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", samplePeriodLength=" + samplePeriodLength + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}
    
    
}
