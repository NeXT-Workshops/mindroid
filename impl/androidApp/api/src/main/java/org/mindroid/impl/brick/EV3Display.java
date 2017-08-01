package org.mindroid.impl.brick;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import org.mindroid.common.messages.DisplayMessageFactory;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;

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
	 * @param str
	 * @param posX
	 * @param posY
	 * @return false if display not ready 
	 */
	public boolean drawString(String str,int posX, int posY){
		if(connection != null){
			connection.sendTCP(DisplayMessageFactory.createDrawStringMsg(str, posX, posY));
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
			connection.sendTCP(DisplayMessageFactory.createClearDisplayMsg());
			return true;
		}
		return false;
	}

	public void drawImage(String str) {
		//TODO Impl
	}



}
