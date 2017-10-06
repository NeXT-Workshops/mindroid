package org.mindroid.common.messages.display;

/**
 * DisplayMessageFactory to create a Display Message
 * @author Torben
 */
public class DisplayMessageFactory {

	/**
	 * Creates a HelloDisplayMessage
	 * @return HelloDisplayMessage
	 */
	public static HelloDisplayMessage getHelloDisplayMessage(){
		return new HelloDisplayMessage();
	}

	/**
	 * Creates a draw String Message with the given parameters
	 * @param str - String to draw
	 * @param x - Position x
	 * @param y - Position y
	 * @return a Draw String message with the given Parameters
	 */
	public static DrawStringMessage createDrawStringMessage(final String str, final int textsize,final int x, final int y){
		DrawStringMessage ds = new DrawStringMessage();
		ds.setStr(str);
		ds.setTextsize(textsize);
		ds.setX(x);
		ds.setY(y);
		return ds;
	}

	/**
	 * Creates a Message clearing the Display
	 * @return ClearDisplayMessage
	 */
	public static ClearDisplayMessage createClearDisplayMessage(){
		return new ClearDisplayMessage();
	}
	



}
