package mindroid.common.ev3.app;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import com.esotericsoftware.minlog.Log;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import org.mindroid.common.messages.NetworkPortConfig;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import mindroid.common.ev3.endpoints.brick.EV3BrickEndpoint;
import mindroid.common.ev3.server.BrickServerImpl;

/**
 * Opens one thread for each endpoint waiting for a connection from the smartphone.
 * 
 * @author Tprben
 */
public class App implements Runnable {
	//Brick main class
	
	
	EV3BrickEndpoint brickEndpoint;
	
	public static void main(String[] args) {
		Log.set(Log.LEVEL_NONE);

		Thread t = new Thread(new App());
		t.start();
	}


	private void initBrick(){
		
		try {
			LocalEV3.get().getLED().setPattern(2); //RED Light, always on, waiting for connection
			Display.showPleaseWait();
			//System.out.println("Current System Time"+ new Date(System.currentTimeMillis()) + " in millis "+ System.currentTimeMillis());
	
			
			BrickServerImpl server = new BrickServerImpl(NetworkPortConfig.BRICK_PORT);
			EV3BrickEndpoint brick = new EV3BrickEndpoint();
			
			//TODO Remove class - Use Sensor and Motor manager directly as Listeners.
			DeviceManager endpointManager = new DeviceManager(); 

			server.addListener(brick);
			server.addListener(endpointManager);

			//System.out.println("Waiting for Connection..");
			LocalEV3.get().getLED().setPattern(3);

			Display.showSystemIsReady();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		
		initBrick();
		
		/*
		
		// --------------- OLD ----------------------
		new Thread(){
			@Override
			public void run() {
				try {
					new UnregulatedMotorEndpoint(MotorPort.A, NetworkPortConfig.MOTOR_PORT_A);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			@Override
			public void run() {
				try {
					new UnregulatedMotorEndpoint(MotorPort.D, NetworkPortConfig.MOTOR_PORT_D);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			@Override
			public void run() {
				try {
					new EV3ColorSensorEndpointEndpoint(SensorMode_.RED, SensorPort.S1, NetworkPortConfig.SENSOR_PORT_1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			@Override
			public void run() {
				try {
					new EV3ColorSensorEndpointEndpoint(SensorMode_.RED, SensorPort.S4, NetworkPortConfig.SENSOR_PORT_4);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			@Override
			public void run() {
				try {
					new EV3UltrasonicSensorEndpointEndpoint(SensorMode_.DISTANCE, SensorPort.S2, NetworkPortConfig.SENSOR_PORT_2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		*/
		Button.ESCAPE.waitForPressAndRelease();
		Sound.playTone(1000, 3);
		System.exit(0);		
	}
	

}
