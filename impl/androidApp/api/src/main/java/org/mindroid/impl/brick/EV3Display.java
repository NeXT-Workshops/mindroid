package org.mindroid.impl.brick;


import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;

import org.mindroid.common.messages.BrickMessages;
import org.mindroid.common.messages.BrickMessages.EndpointCreatedMessage;
import org.mindroid.common.messages.DisplayMessages;

/**
 * Display Endpoint classes. Used to send proper messages to the brick
 */
public class EV3Display extends ClientEndpointImpl{

	public EV3Display(String ip, int tcpPort, int brickTimeout) {
		super(ip, tcpPort, brickTimeout);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void received(Connection connection, Object object) {

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
		if(isClientReady()){
			client.sendTCP(DisplayMessages.drawString(str, posX, posY));
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
		if(isClientReady()){
			client.sendTCP(DisplayMessages.clearDisplay());
			return true;
		}
		return false;
	}

	public void drawImage(String str) {
		//TODO Impl
	}

	
	

}
