package mindroid.common.ev3.endpoints.brick;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.mindroid.common.messages.DisplayMessages;
import org.mindroid.common.messages.DisplayMessages.DrawString;
import org.mindroid.common.messages.StatusLightMessages;
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
		
		//endpointManager.registerMessages(super.server);
				
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.sendTCP(DisplayMessages.helloDisplayMsg());
	}

	@Override
	public void received(Connection connection, Object object) {
		if(!DeviceManager.isBlocked){
			checkForDisplayMessages(object);
			checkForStatusLightMessages(object);
		}
	}

	private void checkForStatusLightMessages(Object object) {
		if(object.getClass() == StatusLightMessages.SetStatusLightMsg.class){
			SetStatusLightMsg msg = (SetStatusLightMsg) object;
			LocalEV3.get().getLED().setPattern(msg.getVal());
		}
	}

	private void checkForDisplayMessages(Object object) {
		if(object.getClass() == DisplayMessages.DrawString.class){
			DrawString ds = (DrawString) object;
			LocalEV3.get().getTextLCD().clear();
			LocalEV3.get().getTextLCD().drawString(ds.getStr(), ds.getX(), ds.getY());
			return;
		}
		
		if(object.getClass() == DisplayMessages.ClearDisplay.class){
			LocalEV3.get().getTextLCD().clear();
			return;
		}
	}

	
}
