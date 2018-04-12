package org.mindroid.impl.brick;


import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.brick.BrickMessagesFactory;
import org.mindroid.common.messages.display.DisplayMessageFactory;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.logging.EV3MsgLogger;

import java.util.logging.Logger;

/**
 * Display Endpoint classes. Used to send proper messages to the brick
 */
public class EV3Display extends Listener{

	private Connection connection = null;

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final EV3MsgLogger msgRcvdLogger;
	private final EV3MsgLogger msgSendLogger;

	public EV3Display() {
		//Init Loggers
		APILoggerManager.getInstance().registerLogger(LOGGER);
		msgRcvdLogger = new EV3MsgLogger(LOGGER,"Received ");
		msgSendLogger = new EV3MsgLogger(LOGGER,"Send ");
	}

	@Override
	public void connected(Connection connection){
		this.connection = connection;
	}

	@Override
	public void received(Connection connection, Object object) {
		//Has not to handle any message
		//Log msg
		if(object instanceof ILoggable){
			((ILoggable) object).accept(msgRcvdLogger);
		}
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
			//Log msg
			ILoggable msg = DisplayMessageFactory.createDrawStringMessage(str,textsize.getValue(),  posX, posY);
			msg.accept(msgSendLogger);

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
			//Log msg
			ILoggable msg = DisplayMessageFactory.createClearDisplayMessage();
			msg.accept(msgSendLogger);

			connection.sendTCP(DisplayMessageFactory.createClearDisplayMessage());
			return true;
		}
		return false;
	}

	public void drawImage(String str) {
		//TODO Impl
	}



}
