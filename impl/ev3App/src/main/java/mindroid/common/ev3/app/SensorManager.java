package mindroid.common.ev3.app;

import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import lejos.hardware.port.Port;
import mindroid.common.ev3.endpoints.SensorEndpoint;
import mindroid.common.ev3.endpoints.sensors.ev3.EV3ColorSensor;
import mindroid.common.ev3.endpoints.sensors.ev3.EV3GyroSensor;
import mindroid.common.ev3.endpoints.sensors.ev3.EV3IRSensor;
import mindroid.common.ev3.endpoints.sensors.ev3.EV3TouchSensor;
import mindroid.common.ev3.endpoints.sensors.ev3.EV3UltrasonicSensor;
import mindroid.common.ev3.server.BrickServerImpl;
import org.mindroid.common.messages.Sensors;

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
			
			switch(sensortype){ // je nach gewolltem sensortyp dem sensorendpoint einen neuen Sensor zuweisen.
				case EV3ColorSensor:
					((SensorEndpoint)endpoints.get(port)).setSensor(new EV3ColorSensor(port,sensortype.getModes()[0])); //(Port sensorPort,SensorMessages.SensorMode_ mode)
					//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> passender Sensortyp EV3Color gesetzt");
					return true;
				case EV3UltrasonicSensor:
					((SensorEndpoint)endpoints.get(port)).setSensor(new EV3UltrasonicSensor(port,sensortype.getModes()[0]));
					//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> passender Sensortyp EV3Ultra gesetzt");
					return true;
				case EV3GyroSensor:
					((SensorEndpoint)endpoints.get(port)).setSensor(new EV3GyroSensor(port,sensortype.getModes()[0]));
					//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> passender Sensortyp EV3Gyro gesetzt");
					return true;
				case EV3IRSensor:
					((SensorEndpoint)endpoints.get(port)).setSensor(new EV3IRSensor(port,sensortype.getModes()[0]));
					//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> passender Sensortyp EV3IRr gesetzt");
					return true;
				case EV3TouchSensor:
					((SensorEndpoint)endpoints.get(port)).setSensor(new EV3TouchSensor(port,sensortype.getModes()[0]));
					//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> passender Sensortyp EV3Touch gesetzt");
					return true;
				default: endpoints.remove(port); //loesche Endpoint!
			}
			
		}
		//SensorEndpoint noch nicht geoeffnet. Erstelle mit passendem Sensor!
		//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> Endpoint noch nicht geoeffnet, erzeuge!");
		Listener endpoint;
		switch(sensortype){
			case EV3ColorSensor:
				endpoint = new SensorEndpoint(new EV3ColorSensor(port,sensortype.getModes()[0])); //(Port sensorPort,SensorMessages.SensorMode_ mode)
				break;
			case EV3UltrasonicSensor:
				endpoint = new SensorEndpoint(new EV3UltrasonicSensor(port,sensortype.getModes()[0]));
				break; 
			case EV3GyroSensor:
				endpoint = new SensorEndpoint(new EV3GyroSensor(port,sensortype.getModes()[0]));
				break; 
			case EV3IRSensor:			
				endpoint = new SensorEndpoint(new EV3IRSensor(port,sensortype.getModes()[0]));
				break; 
			case EV3TouchSensor:
				endpoint = new SensorEndpoint(new EV3TouchSensor(port,sensortype.getModes()[0]));
				break;
		default: endpoint = null; 
		}
		
		if(endpoint != null){
			endpoints.put(port,endpoint);
			//Create Server for Sensor
			BrickServerImpl brickServer = new BrickServerImpl(networkPort);
			brickServer.addListener(endpoint);
			//System.out.println("SensorManager.createSensorEndpoint() at "+port.toString()+" -> Endpoint noch nicht geoeffnet, Server gestartet!");
			return true;
		}else{
			return false;
		}
	}
}
