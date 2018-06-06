package mindroid.common.ev3.endpoints.sensors.ev3;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3GyroSensor extends AbstractSensor {


    public EV3GyroSensor(Port sensorPort) {
        super(Sensors.EV3GyroSensor,sensorPort,Sensors.EV3GyroSensor.getModes()[0],SensorSampleRates.SENS_GYRO_SAMPLERATE);
    }

    @Override
    public boolean setSensorMode(Sensormode newMode) {
        if(Sensors.EV3GyroSensor.isValidMode(newMode)){
            sensor.setCurrentMode(newMode.getValue());
            this.sensormode = newMode;
            return true;
        }
        return false;
    }
    
	@Override
	public String toString() {
		return "EV3GyroSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", sampleRate=" + sampleRate + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}
}
