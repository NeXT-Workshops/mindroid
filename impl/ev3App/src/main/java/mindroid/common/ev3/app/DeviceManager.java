package mindroid.common.ev3.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import lejos.hardware.lcd.GraphicsLCD;
import org.mindroid.common.messages.BrickMessages;
import org.mindroid.common.messages.BrickMessages.CreateMotorMessage;
import org.mindroid.common.messages.BrickMessages.CreateSensorMessage;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import mindroid.common.ev3.app.HardwareInterfaceManager.BrickType;
import mindroid.common.ev3.endpoints.brick.EV3BrickEndpoint;


/**
 * 
 * @author torben
 * 
 *
 * Listens on the Server to create sensor and motors when wanted by messages.
 *
 */
public class DeviceManager extends Listener {
	//TODO createServer : EV3BrickServer Method for every Port; Add Sensor as listener afterwards
	
	
	SensorManager sm;
	MotorManager mm;
	
	
	
	HashMap<String,Port> sensPorts = new HashMap<String,Port>(4);
	HashMap<String,Port> motorPorts = new HashMap<String,Port>(4);
	
	EV3BrickEndpoint brickEndpoint;
	
	/** Create-Motor/Sensor methods are queued to obtain stability of creation process. They then get handled one by one **/
	Queue<Thread> creationThreads = new LinkedList<Thread>();
	
	/** true -> Stop proccessing. Ignore Messages. LED will blink red. 
 	 * 
 	 *  Wrong Sensor/Motor at port -> true
 	 *  Sensor/Motor Detection faild -> true
 	 *  
 	 *  **/
	public static boolean isBlocked = false;
	
	public DeviceManager(){
		mm 	= new MotorManager(BrickType.EV3);
		sm 	= new SensorManager(BrickType.EV3);
		
		sensPorts.put("S1", SensorPort.S1);
		sensPorts.put("S2", SensorPort.S2);
		sensPorts.put("S3", SensorPort.S3);
		sensPorts.put("S4", SensorPort.S4);
	
		motorPorts.put("A", MotorPort.A);
		motorPorts.put("B", MotorPort.B);
		motorPorts.put("C", MotorPort.C);
		motorPorts.put("D", MotorPort.D);
		
		checkForCreationThread();
	}
	
	/**
	 * Queued Methods "Create-Motor/Sensor" are handled one by one in this method 
	 */
	private void checkForCreationThread(){
		Thread checkForThread = new Thread(){
			
			@Override
			public void run(){
				Thread currentThread = null;
				while(!isBlocked){
								
					if(creationThreads.size() > 0 && currentThread == null){
						currentThread = creationThreads.poll();
					}
					
					if(currentThread != null){
						currentThread.start();
						//System.out.println("CREATION THREAD STARTED!");
						while(currentThread.isAlive()){
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//System.out.println("CREATION THREAD COMPLETE!");
						currentThread = null;
					}
					
					while(creationThreads.size() == 0 ){
						try{
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//TODO maybe end Thread after a while when no threads get added
					}
				}
				//System.out.println("IS_BLOCKED "+isBlocked);
				if(isBlocked){
					//System.out.println("CreationThreadMethod is blocked!");
					LocalEV3.get().getLED().setPattern(5); //Blink red
				}
			}
			
			
		};
		checkForThread.start();
	}
	
	
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);	
		connection.sendTCP(BrickMessages.newHelloThereMessage("Hello, this is the Endpoint Manager of the EV3Brick - Connection established!"));
		Display.showSystemIsReadyAndConnected();
	}

		
	@Override
	public void received(Connection connection, Object object) {		
		//Received a request from smartphone to change to a different sensor mode
		//System.out.println("Received a Message"+object.toString());
		final Connection conn = connection;
		
 		if(!isBlocked){
 			
			if (object.getClass() == BrickMessages.CreateSensorMessage.class){
				//System.out.println("Create a Sensor Message!");
				final CreateSensorMessage msg = (BrickMessages.CreateSensorMessage) object;
				
				handleCreateSensorMessage(conn, msg);
				return;
			}
			
			if (object.getClass() == BrickMessages.CreateMotorMessage.class){
				final CreateMotorMessage msg = (BrickMessages.CreateMotorMessage) object;
				handleCreateMotorMessage(conn, msg);
				//connection.sendTCP(BrickMessages.createEndpointCreatedMessage(true, msg.getPort(), "Motor endpoint created: "+msg.getPort()));
					return;
			}
 		}
	}
	@Override
	public void disconnected(Connection connection){
		super.disconnected(connection);
		Display.showSystemIsReady();
	}

	private void handleCreateMotorMessage(final Connection conn, final CreateMotorMessage msg) {
		Thread createMotor = new Thread(){
			@Override
			public void run(){
				LocalEV3.get().getLED().setPattern(9);
				
				if(motorPorts.containsKey(msg.getPort())){
					if(createMotorEndpoint( motorPorts.get(msg.getPort()),msg.getMotorType(),msg.getNetworkPort() )){
						conn.sendTCP(BrickMessages.createEndpointCreatedMessage(true, msg.getPort(), "Motor endpoint created: "+msg.getPort(), false, true));		
					}else{
						isBlocked = true;
						//System.out.println("System got blocked by handleCreateMotorMethod");
						conn.sendTCP(BrickMessages.createEndpointCreatedMessage(false, msg.getPort(), "Motor endpoint not created: "+msg.getPort(), false, true));						
						
					}
				}else{
					conn.sendTCP(BrickMessages.createEndpointCreatedMessage(false, msg.getPort(), "Port not found!"+msg.getPort(), false, true));
				}	
			}
			};
		creationThreads.add(createMotor);
		//createMotor.start();
	}

	private void handleCreateSensorMessage(final Connection conn, final CreateSensorMessage msg) {
		Thread createSensor = new Thread(){
			@Override
			public void run(){
				LocalEV3.get().getLED().setPattern(6);
				
				if(sensPorts.containsKey(msg.getPort())){
					try {
						if(createSensorEndpoint( sensPorts.get(msg.getPort()),msg.getSensorType(),msg.getNetworkPort() )){
							conn.sendTCP(BrickMessages.createEndpointCreatedMessage(true, msg.getPort(), "Sensor endpoint created: "+msg.getPort(), true, false));		
						}else{
							isBlocked = true;
							//System.out.println("System got blocked by handleCreateSensorMethod");
							conn.sendTCP(BrickMessages.createEndpointCreatedMessage(false, msg.getPort(), "Sensor endpoint not created: "+msg.getPort(), true, false));					
						}
					} catch (IOException e) {
						conn.sendTCP(BrickMessages.createEndpointCreatedMessage(false, msg.getPort(), "IOException: Sensor endpoint not created: "+msg.getPort(), true, false)+"\n "+e.toString());				
						e.printStackTrace();
					}
					
					
				}else{
					conn.sendTCP(SensorMessages.createSensorErrorMessage(null, "Port not found!"));
				}	
			}
		};
		
		creationThreads.add(createSensor);
		return;
	}
	
	
	/**
	 * 
	 * @param port
	 * @param sensorType
	 * @param networkPort
	 * @throws IOException
	 */
	private boolean createSensorEndpoint(Port port, Sensors sensorType, int networkPort) throws IOException {
		return sm.createSensorEndpoint(port, sensorType, networkPort);
	}

	/**
	 * 
	 * @param port
	 * @param motorType
	 * @param networkPort
	 * @throws IOException
	 */
	private boolean createMotorEndpoint(Port port, Motors motorType, int networkPort) {
		try {
			return mm.createMotorEndpoint(port,motorType,networkPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
