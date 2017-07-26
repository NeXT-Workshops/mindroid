package mindroid.common.ev3.endpoints.brick;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import org.mindroid.common.messages.DisplayMessageFactory;
import org.mindroid.common.messages.SoundMessageFactory;
import org.mindroid.common.messages.StatusLightMessages.SetStatusLightMsg;
import lejos.hardware.ev3.LocalEV3;
import mindroid.common.ev3.app.DeviceManager;


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
		connection.sendTCP(DisplayMessageFactory.getHelloDisplayMsg());
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
		if(object.getClass() == SetStatusLightMsg.class){
			SetStatusLightMsg msg = (SetStatusLightMsg) object;
			LocalEV3.get().getLED().setPattern(msg.getVal());

		}
	}

	/**
	 * Handle Display messages
	 * @param object
	 */
	private void handleDisplayMessages(Object object) {
		if(object.getClass() == DisplayMessageFactory.DrawStringMsg.class){
			DisplayMessageFactory.DrawStringMsg ds = (DisplayMessageFactory.DrawStringMsg) object;
			LocalEV3.get().getGraphicsLCD().setColor(GraphicsLCD.BLACK);
			LocalEV3.get().getGraphicsLCD().setFont(Font.getDefaultFont());
			LocalEV3.get().getGraphicsLCD().drawString(ds.getStr(),ds.getX(),ds.getY(),GraphicsLCD.TOP);
			return;
		}
		
		if(object.getClass() == DisplayMessageFactory.ClearDisplayMsg.class){
			LCD.clearDisplay();
			return;
		}
	}

	private void handleSoundMessages(Object object){
		//System.out.println(object.toString());
		//Handle Beep Message
		if(object.getClass() ==  SoundMessageFactory.BeepMessage.class){
			switch(((SoundMessageFactory.BeepMessage)object).getBeep()){
				case SoundMessageFactory.Beeptype.SINGLE_BEEP:
					Sound.beep(); break;
				case SoundMessageFactory.Beeptype.DOUBLE_BEEP:
					Sound.twoBeeps(); break;
				case SoundMessageFactory.Beeptype.BEEP_SEQUENCE_DOWNWARDS:
					Sound.beepSequence(); break;
				case SoundMessageFactory.Beeptype.BEEP_SEQUENCE_UPWARDS:
					Sound.beepSequenceUp(); break;
				case SoundMessageFactory.Beeptype.LOW_BUZZ:
					Sound.buzz(); break;

				default: //DO nothing;
			}
			return;
		}

		//Handle Volume Message
		if(object.getClass() ==  SoundMessageFactory.SoundVolumeMessage.class){
			Sound.setVolume(((SoundMessageFactory.SoundVolumeMessage)object).getVolume());
			return;
		}
	}

	
}
