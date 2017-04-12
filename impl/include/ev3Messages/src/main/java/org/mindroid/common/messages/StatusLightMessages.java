package org.mindroid.common.messages;

public class StatusLightMessages {
	
	/**
	 * 
	 * val explanation (on a EV3):
	 *  
	 * val = 0: turn off button lights
	 * val = 1/2/3: static light green/red/yellow
	 * val = 4/5/6: normal blinking light green/red/yellow
	 * val = 7/8/9: fast blinking light green/red/yellow
	 * >9: same as 9.
	 * @param val
	 * @return
	 */
	public static SetStatusLightMsg setStatusLight(int val){
		SetStatusLightMsg slm = new SetStatusLightMsg();
		slm.setVal(val);
		return slm;
	}
	
	public static class SetStatusLightMsg{
		
		public int val = 0;
		public SetStatusLightMsg(){ };
		public int getVal() {
			return val;
		}
		public void setVal(int val) {
			this.val = val;
		}
		
	}
}
