package mindroid.common.ev3.endpoints.sensors.ev3;

import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3ColorSensor extends AbstractSensor {



    public EV3ColorSensor(Port sensorPort,SensorMessages.SensorMode_ mode){
        super(SensorSampleRates.SENS_COLOR_SAMPLERATE);
        isSensorCreated = create(sensorPort,Sensors.EV3ColorSensor,mode); //Creates Lejos.EV3ColorSensor. acceptable
        if(isSensorCreated){
            sendSensorData();
        }
        
        System.out.println(toString());;
    }

    @Override
    public boolean setSensorMode(SensorMessages.SensorMode_ newMode) {
        switch (newMode) {
            case RED:
                //Measures the level of reflected light from the sensors RED LED.
                sensor.setCurrentMode(newMode.getValue());
                return true;
            case AMBIENT:
                //Measures the level of ambient light while the sensors lights are off.
                sensor.setCurrentMode(newMode.getValue());
                return true;
            case COLOR_ID:
                //Measures the color ID of a surface.
                sensor.setCurrentMode(newMode.getValue());
                return true;
            case RGB:
                //Measures the level of red, green and blue light when illuminated by a white light source..
                sensor.setCurrentMode(newMode.getValue());
                return true;
            default:
                return false;
        }
    }

	@Override
	public String toString() {
		return "EV3ColorSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", sampleRate=" + sampleRate + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}
    
    
}
