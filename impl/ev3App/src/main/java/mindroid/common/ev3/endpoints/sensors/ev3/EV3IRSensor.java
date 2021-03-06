package mindroid.common.ev3.endpoints.sensors.ev3;

import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3IRSensor extends AbstractSensor {


    public EV3IRSensor(Port sensorPort) {
        super(Sensors.EV3IRSensor,sensorPort,Sensors.EV3IRSensor.getModes()[0],SensorSampleRates.SENS_IR_SAMPLERATE);
    }

    @Override
    public boolean setSensorMode(Sensormode newMode) {
        if(Sensors.EV3IRSensor.isValidMode(newMode)){
            sensor.setCurrentMode(newMode.getValue());
            this.sensormode = newMode;
            return true;
        }
        return false;
    }
    
	@Override
	public String toString() {
		return "EV3IRSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", samplePeriodLength=" + samplePeriodLength + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}
}
