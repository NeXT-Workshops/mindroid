package org.mindroid.impl.brick;


import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;
import org.mindroid.common.messages.display.DisplayMessageFactory;

/**
 * Display Endpoint classes. Used to send proper messages to the brick
 */
public class EV3Display extends Listener{

	private Connection connection = null;

	public EV3Display() {

	}

	@Override
	public void connected(Connection connection){
		this.connection = connection;
	}

	@Override
	public void received(Connection connection, Object object) {
		//Has not to handle any message
		System.out.println("[EV3-DISPLAY:Received message] "+object);

	}

	@Override
	public void disconnected(Connection connection){
		this.connection = null;
	}
	/**
	 * Draw a String on the IEV3Display
	 * 
	 * @param str String to draw
	 * @param textsize Size of the string drawn on the display
	 * @param posX startposition x of the string
	 * @param posY startposition y of the string
	 * @return false if display not ready 
	 */
	public boolean drawString(String str,Textsize textsize,int posX, int posY){
		if(connection != null){
			connection.sendTCP(DisplayMessageFactory.createDrawStringMessage(str,textsize.getValue(),  posX, posY));
			return true;
		}
		return false;
	}
	
	/**
	 * Clear the IEV3Display
	 * 
	 * @return false if display not ready 
	 */
	public boolean clearDisplay(){
		if(connection != null){
			connection.sendTCP(DisplayMessageFactory.createClearDisplayMessage());
			return true;
		}
		return false;
	}

	public void drawImage(String str) {
		//TODO Impl
	}



}
