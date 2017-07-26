package mindroid.common.ev3.app;

import java.io.IOException;

import com.esotericsoftware.kryonet.Listener;

import org.mindroid.common.messages.Motors;
import lejos.hardware.port.Port;
import mindroid.common.ev3.endpoints.MotorEndpoint;
import mindroid.common.ev3.endpoints.motors.ev3.LargeRegulatedMotor;
import mindroid.common.ev3.endpoints.motors.ev3.MediumRegulatedMotor;
import mindroid.common.ev3.endpoints.motors.ev3.EV3UnregulatedMotor;
import mindroid.common.ev3.server.BrickServerImpl;

public class MotorManager extends HardwareInterfaceManager{


	
	public MotorManager(BrickType bType) {
		super(bType);

		// TODO Auto-generated constructor stub
	}

	
	public boolean createMotorEndpoint(Port port, Motors motortype, int networkPort) throws IOException {
		if(endpoints.containsKey(port)){
			//Ports bereits offen
			if(((MotorEndpoint)endpoints.get(port)).getMotor() != null) {
				if(((MotorEndpoint)endpoints.get(port)).getMotor().getMotortype() == motortype){
					return true;
				}
				//System.out.println("Motor.createMotorEndpoint() at "+port.toString()+" -> aktueller motortyp passt nicht");
			}
			
			((MotorEndpoint)endpoints.get(port)).getMotor().close();//TODO
			//System.out.println("Motor.createMotorEndpoint() at "+port.toString()+" -> aktueller motortyp passt nicht - erzeuge neuen motortyp");
			switch(motortype){
				case UnregulatedMotor:
					((MotorEndpoint)endpoints.get(port)).setMotor(new EV3UnregulatedMotor(port));
					return true;
				case MediumRegulatedMotor:
					((MotorEndpoint)endpoints.get(port)).setMotor(new MediumRegulatedMotor(port));
					return true;
				case LargeRegulatedMotor:
					((MotorEndpoint)endpoints.get(port)).setMotor(new LargeRegulatedMotor(port));
					return true;
				default: return false;
			}
		}
		
		Listener endpoint = null;

		switch(motortype){
			case UnregulatedMotor:
				endpoint = new MotorEndpoint(new EV3UnregulatedMotor(port));
				break;
			case MediumRegulatedMotor:
				endpoint = new MotorEndpoint(new MediumRegulatedMotor(port));
				break;
			case LargeRegulatedMotor:
				endpoint = new MotorEndpoint(new LargeRegulatedMotor(port));
				break;
			default: return false;
		}
		
		if(endpoint != null){
			endpoints.put(port,endpoint);
			
			//Create Server for Motor //TODO remove to ServerBuilder/-Manager.class
			BrickServerImpl brickServer = new BrickServerImpl(networkPort);
			brickServer.addListener(endpoint);
			
			return true;
		}else{
			return false;
		}
		
	}
	
}
