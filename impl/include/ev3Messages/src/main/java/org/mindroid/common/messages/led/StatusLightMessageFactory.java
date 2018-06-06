package org.mindroid.common.messages.led;

public class StatusLightMessageFactory {
	
	/**
	 * 
	 * val explanation (on a EV3):
	 *  
	 * val = 0: turn off button lights
	 * val = 1 or 2 or 3: static light green or red or yellow
	 * val = 4 or 5 or 6: normal blinking light green or red or yellow
	 * val = 7 or 8 or 9: fast blinking light green or red or yellow
	 * val greater 9: same as 9.
	 * @param val the status light code
	 * @return the prepared message
	 */
	public static SetStatusLightMessage createSetStatusLightMessage(int val){
		SetStatusLightMessage slm = new SetStatusLightMessage();
		slm.setVal(val);
		return slm;
	}
	

}
