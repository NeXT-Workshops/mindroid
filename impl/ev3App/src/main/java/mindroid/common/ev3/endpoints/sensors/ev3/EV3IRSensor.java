package mindroid.common.ev3.endpoints.sensors.ev3;

import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3IRSensor extends AbstractSensor {


    public EV3IRSensor(Port sensorPort) {
        super(Sensors.EV3IRSensor,sensorPort,Sensors.EV3IRSensor.getModes()[0],SensorSampleRates.SENS_IR_SAMPLERATE);
    }

    @Override
    public boolean setSensorMode(SensorMessages.SensorMode_ newMode) {
        if(Sensors.EV3IRSensor.isValidMode(newMode)){
            sensor.setCurrentMode(newMode.getValue());
            return true;
        }
        return false;
    }
    
	@Override
	public String toString() {
		return "EV3IRSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", sampleRate=" + sampleRate + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}
}
