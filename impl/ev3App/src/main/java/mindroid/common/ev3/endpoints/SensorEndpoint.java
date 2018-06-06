package mindroid.common.ev3.endpoints;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.mindroid.common.messages.sensor.ChangeSensorModeMessage;
import org.mindroid.common.messages.sensor.SensorMessageFactory;
import lejos.hardware.port.PortException;
import mindroid.common.ev3.endpoints.sensors.ev3.AbstractSensor;
import mindroid.common.ev3.endpoints.sensors.ev3.SensorListener;
import org.mindroid.common.messages.hardware.Sensormode;

/**
 * The Sensor Endpoint class is a Listener of the Kyro.server and also the interface to an EV3-Sensor.
 * The EV3 Sensor collects its data and sends it to this Endpoint by calling handleSensorData();
 */
public class SensorEndpoint extends Listener implements SensorListener {

	protected AbstractSensor sensor;
	
	protected Connection connection;

	public SensorEndpoint(AbstractSensor sensor) {
		setSensor(sensor); //Also adds calls sensor.addListener(this)
	}

	/*
	@Override
	public void registerMessages(EndPoint endpoint) {
		SensorMessageFactory.register(endpoint);
	}*/

	@Override
	public void connected(Connection connection) {
		if(this.connection == null) { //Only one client is allowed to connect at a time - connection gets set to null when disconnected gets called.
			try {
				this.connection = connection;
				connection.sendTCP(SensorMessageFactory.createStatusMessage("Connected to Endpoint. Sensor: " + sensor + ".Connection from " + connection));
			} catch (PortException e) {
				connection.close();
				e.printStackTrace();
				throw new RuntimeException("Sensor port already in use.");
			}
		}
	}

	
	@Override
	public void disconnected(Connection connection) {
		//System.out.println("SensorEndpoint - Connection disconnected: "+connection+" " + sensor.toString());
		this.connection = null;
	}

	
	@Override
	public void received(Connection connection, Object object) {
		//Received a request from smartphone to change to a different sensor mode
		if (object instanceof ChangeSensorModeMessage){ //TODO may change to .getClass() == ChangeSensorModeMsg.class
			Sensormode newMode = ((ChangeSensorModeMessage) object).getNewMode();
			
			sensor.setSensorMode(newMode);
			
			broadcastNewMode(newMode);
		}
	}

	/**
	 * This function broadcasts to the endpoint if the mode changed successfully.
	 * Called when the sensor is first connected and whenever Smartphone sent an request to change the mode.
	 * 
	 * @param mode the new active mode
	 */
	public void broadcastNewMode(Sensormode mode){
		if(this.connection != null) {
			this.connection.sendTCP(SensorMessageFactory.sensorModeChangedTo(mode));
		}
	}


	@Override
	public void handleSensorData(float[] sample) {
		if(this.connection != null && sample != null){
			this.connection.sendTCP(SensorMessageFactory.sensorEvent(sample,sensor.getSensormode(), System.nanoTime()));
		}
	}

	public AbstractSensor getSensor() {
		return sensor;
	}

	public void setSensor(AbstractSensor sensor) {
		this.sensor = sensor;
		sensor.addListener(this);
	}
}
