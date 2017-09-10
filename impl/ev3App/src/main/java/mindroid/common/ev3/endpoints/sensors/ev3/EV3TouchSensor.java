package mindroid.common.ev3.endpoints.sensors.ev3;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3TouchSensor extends AbstractSensor {

    public EV3TouchSensor(Port sensorPort) {
        super(Sensors.EV3TouchSensor,sensorPort,Sensors.EV3TouchSensor.getModes()[0],SensorSampleRates.SENS_TOUCH_SAMPLERATE);
    }

    @Override
    public boolean setSensorMode(Sensormode newMode) {
        if(Sensors.EV3TouchSensor.isValidMode(newMode)){
            sensor.setCurrentMode(newMode.getValue());
            return true;
        }
        return false;
    }
    
	@Override
	public String toString() {
		return "EV3TouchSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", sampleRate=" + sampleRate + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}
}
