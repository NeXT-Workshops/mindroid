package mindroid.common.ev3.app;

import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Listener;

import lejos.hardware.port.Port;

/**
 * Manages the HardwareInterfaces.
 * Creates Endpoints.
 * Checks if Sensor or motor is plugged in @ specified port
 * 
 * @author Torben
 *
 */
public abstract class HardwareInterfaceManager {
	HashMap<Port,Listener> endpoints = null;

	@Deprecated
	ArrayList<Port> ports = null;
	BrickType bType = null;
		
	public enum BrickType{
		EV3;
	}
	
	public HardwareInterfaceManager(BrickType bType){
		this.bType = bType;
		//this.app = app;
		endpoints = new HashMap<Port,Listener>(8);
		ports = new ArrayList<Port>(4);
	}
}
