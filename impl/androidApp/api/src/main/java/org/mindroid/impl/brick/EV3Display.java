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

	private boolean ready = false;
/*
	@Override
	protected void registerMessages(Client client) {
		DisplayMessages.register(client);		
	}
*/
	@Override
	public void received(Connection connection, Object object) {
		System.out.println("Local-EV3Display: Received Message "+object.toString());
		if(object.getClass() == BrickMessages.EndpointCreatedMessage.class){
			BrickMessages.EndpointCreatedMessage msg = (EndpointCreatedMessage) object;
			if(!msg.isMotor() && !msg.isSensor()){
				if(msg.isSuccess() && msg.getMsg().equals("DISPLAY")){
					connect();
					System.out.println("EV3-Display: connecting to endpoint!");
				}
			}
			return;
		}
		
		if(object.getClass() == DisplayMessages.HelloDisplay.class){
			ready = true;
		}
		
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
		if(ready){
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
		if(ready){
			client.sendTCP(DisplayMessages.clearDisplay());
			return true;
		}
		return false;
	}

	public boolean isReady() {
		return ready;
	}
	
	

}
