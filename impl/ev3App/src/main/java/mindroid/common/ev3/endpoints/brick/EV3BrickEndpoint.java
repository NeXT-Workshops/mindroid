package mindroid.common.ev3.endpoints.brick;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import mindroid.common.ev3.app.Display;
import org.mindroid.common.messages.display.ClearDisplayMessage;
import org.mindroid.common.messages.display.DisplayMessageFactory;
import org.mindroid.common.messages.display.DrawStringMessage;
import org.mindroid.common.messages.led.SetStatusLightMessage;
import org.mindroid.common.messages.sound.BeepMessage;
import lejos.hardware.ev3.LocalEV3;
import mindroid.common.ev3.app.DeviceManager;
import org.mindroid.common.messages.sound.SoundVolumeMessage;


/**
 * Represents the EV3Brick
 * 
 * Used to control display;status led; and Buttons;
 *  
 * @author mindroid
 *
 */
public class EV3BrickEndpoint extends Listener {

	
	public EV3BrickEndpoint() throws IOException {
		

	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.sendTCP(DisplayMessageFactory.getHelloDisplayMessage());
	}

	@Override
	public void received(Connection connection, Object object) {
		if(!DeviceManager.isBlocked){
			handleDisplayMessages(object);
			handleStatusLightMessages(object);
			handleSoundMessages(object);
		}
	}

	/**
	 * Handle Status light messages
	 * @param object
	 */
	private void handleStatusLightMessages(Object object) {
		if(object.getClass() == SetStatusLightMessage.class){
			SetStatusLightMessage msg = (SetStatusLightMessage) object;
			LocalEV3.get().getLED().setPattern(msg.getVal());

		}
	}

	/**
	 * Handle Display messages
	 * @param object
	 */
	private void handleDisplayMessages(Object object) {
		if(object.getClass() == DrawStringMessage.class){
			DrawStringMessage ds = (DrawStringMessage) object;
			LocalEV3.get().getGraphicsLCD().setColor(GraphicsLCD.BLACK);
			switch (ds.getTextsize()){
				case 1: Display.configureFont(Font.getSmallFont()); break;
				case 2: Display.configureFont(Font.getDefaultFont()); break;
				case 3: Display.configureFont(Font.getLargeFont()); break;
				default: Display.configureFont(Font.getDefaultFont());
			}
			Display.drawString(ds.getStr(),ds.getX(),ds.getY(),GraphicsLCD.TOP);
			return;
		}
		
		if(object.getClass() == ClearDisplayMessage.class){
			LCD.clearDisplay();
			return;
		}
	}

	private void handleSoundMessages(Object object){
		//System.out.println(object.toString());
		//Handle Beep Message
		if(object.getClass() ==  BeepMessage.class){
			switch(((BeepMessage)object).getBeep()){
				case BeepMessage.Beeptype.SINGLE_BEEP:
					Sound.beep(); break;
				case BeepMessage.Beeptype.DOUBLE_BEEP:
					Sound.twoBeeps(); break;
				case BeepMessage.Beeptype.BEEP_SEQUENCE_DOWNWARDS:
					Sound.beepSequence(); break;
				case BeepMessage.Beeptype.BEEP_SEQUENCE_UPWARDS:
					Sound.beepSequenceUp(); break;
				case BeepMessage.Beeptype.LOW_BUZZ:
					Sound.buzz(); break;

				default: //DO nothing;
			}
			return;
		}

		//Handle Volume Message
		if(object.getClass() ==  SoundVolumeMessage.class){
			Sound.setVolume(((SoundVolumeMessage)object).getVolume());
			return;
		}
	}

	
}
