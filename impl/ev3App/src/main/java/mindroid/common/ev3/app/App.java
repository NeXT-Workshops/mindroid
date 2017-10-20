package mindroid.common.ev3.app;

import java.io.IOException;
import java.net.InetAddress;
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
 * @author Torben
 */
public class App implements Runnable {

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

			//Main Server Running on Brick receiving initialization messages, brick messages (Display,LED,Buttons, etc).
			BrickServerImpl server = new BrickServerImpl(NetworkPortConfig.BRICK_PORT);
			EV3BrickEndpoint brick = new EV3BrickEndpoint();
			
			//TODO Remove class - Use Sensor and Motor manager directly as Listeners.
			DeviceManager endpointManager = new DeviceManager(); 

			server.addListener(brick);
			server.addListener(new ButtonObserver());
			server.addListener(endpointManager);

			LocalEV3.get().getLED().setPattern(3);

			Display.showSystemIsReady();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		
		initBrick();
		Button.ESCAPE.waitForPressAndRelease();
		Sound.playTone(1000, 3);
		System.exit(0);		
	}
	

}
