package mindroid.common.ev3.app;

import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import lejos.hardware.port.Port;
import mindroid.common.ev3.endpoints.SensorEndpoint;
import mindroid.common.ev3.endpoints.sensors.ev3.*;
import mindroid.common.ev3.server.BrickServerImpl;
import org.mindroid.common.messages.hardware.Sensors;

public class SensorManager extends HardwareInterfaceManager {

	
	public SensorManager(BrickType bType){
		super(bType);
	}
	
	/**
	 * Create a sensor Endpoint
	 * 
	 * @param port
	 * @param sensortype
	 * @param networkPort
	 * @return
	 * @throws IOException
	 */
	public boolean createSensorEndpoint(Port port, Sensors sensortype, int networkPort) throws IOException {
		//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" sensortype: "+sensortype.toString()+" at port "+networkPort);
		boolean isCreated = false;
		AbstractSensor sensor = null;
		if(endpoints.containsKey(port)){ //Endpoint auf diesem Port bereits geoeffnet
			//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> containsPort = true");
			if(((SensorEndpoint)endpoints.get(port)).getSensor() != null) { //Pruefe ob der Sensor null ist.
				if(((SensorEndpoint)endpoints.get(port)).getSensor().getSensortype() == sensortype){
					//Pruefe ob der Sensortyp des geoeffneted Sensors mit dem zu initialisierenden uebereinstimmt
					return true; //Sensor bereits initialisiert!
				}
				//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> aktueller sensortyp passt nicht");
			}
			//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> Sensor == null oder typ passt nicht!");
			//Sensor ist null oder aktueller sensortyp passt nicht -> Sensortyp aendern
			
			((SensorEndpoint)endpoints.get(port)).getSensor().getSensor().close(); //Close old sensor

			sensor = getSensor(port, sensortype);
			//Create the sensor, and add to endpoint
			if(sensor != null) {
				isCreated = sensor.create();
				if (isCreated) {
					//Link Sensor and Endpoint
					((SensorEndpoint) endpoints.get(port)).setSensor(sensor);
					//Start sensor -> sensor starts sending data
					sensor.startSensor();
					return true; //Creation was successful
				} else {
					return false; //Sensor Creation failed
				}
			}else {
				return false; //Sensor Creation failed
			}
		}else {
			//SensorEndpoint noch nicht geoeffnet. Erstelle mit passendem Sensor!
			//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> Endpoint noch nicht geoeffnet, erzeuge!");
			Listener endpoint;
			sensor = getSensor(port,sensortype);

			if(sensor != null) {
				isCreated = sensor.create();
				if (isCreated) {
					//Create Endpoint with created sensor
					endpoint = new SensorEndpoint(sensor);
					//Save port endpoint combination
					endpoints.put(port, endpoint);
					//Create Server for Sensor
					BrickServerImpl brickServer = new BrickServerImpl(networkPort);
					brickServer.addListener(endpoint);
					//Start sensor -> sensor starts sending data
					sensor.startSensor();
					return true; //Creation was successful
				} else {
					return false; //Sensor Creation failed
				}
			}else {
				return false; //Sensor Creation failed
			}
		}
	}

	private AbstractSensor getSensor(Port port, Sensors sensortype) {
		AbstractSensor sensor = null;
		switch(sensortype){ // Create Sensorobject depending son given Sensortype
            case EV3ColorSensor:
                sensor = new EV3ColorSensor(port);
                break;
            case EV3UltrasonicSensor:
                sensor = new EV3UltrasonicSensor(port);
                break;
            case EV3GyroSensor:
                sensor = new EV3GyroSensor(port);
                break;
            case EV3IRSensor:
                sensor = new EV3IRSensor(port);
                break;
            case EV3TouchSensor:
                sensor = new EV3TouchSensor(port);
                break;
            default: if(endpoints.containsKey(port)){ endpoints.remove(port); } //delete Endpoint!
        }
        return sensor;
	}
}
