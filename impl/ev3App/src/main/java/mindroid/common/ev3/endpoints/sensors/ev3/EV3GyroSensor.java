package mindroid.common.ev3.endpoints.sensors.ev3;

import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import lejos.hardware.port.Port;

/**
 * Created by torben on 27.01.2017.
 */

public class EV3GyroSensor extends AbstractSensor {


    public EV3GyroSensor(Port sensorPort, SensorMessages.SensorMode_ mode) {
        super(SensorSampleRates.SENS_GYRO_SAMPLERATE);
        isSensorCreated = create(sensorPort, Sensors.EV3GyroSensor,mode); //Creates Lejos.EV3ColorSensor. acceptable
        if(isSensorCreated){
            sendSensorData();
        }
        System.out.println(toString());;}

    @Override
    public boolean setSensorMode(SensorMessages.SensorMode_ newMode) {
        switch(newMode){
            //Measures the orientation of the sensor
            case ANGLE: sensor.setCurrentMode(newMode.getValue()); return true;
            //Measures the angular velocity of the sensor
            case RATE: sensor.setCurrentMode(newMode.getValue()); return true;
            //Measures both angle and angular velocity
            case RATEANDANGLE: sensor.setCurrentMode(newMode.getValue()); return true;
            default : return false;
        }
    }
    
	@Override
	public String toString() {
		return "EV3ColorSensor [sensor=" + sensor + ", sensortype=" + sensortype + ", sensormode=" + sensormode
				+ ", sensorPort=" + sensorPort + ", sampleRate=" + sampleRate + ", isSensorCreated=" + isSensorCreated
				+ "]";
	}
}
