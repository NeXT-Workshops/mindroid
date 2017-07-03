package org.mindroid.common.messages;

public class DisplayMessageFactory {



	/**
	 * creates a HelloDisplay Msg;
	 * @return
	 */
	public static HelloDisplay helloDisplayMsg(){
		return new HelloDisplay();
	}
	
	/**
	 * send after the client Display endpoint is connnected;
	 * @author mindroid
	 *
	 */
	public static class HelloDisplay{
		public HelloDisplay() {};
	}
	
	public static DrawStringMsg createDrawStringMsg(String str, int x, int y){
		DrawStringMsg ds = new DrawStringMsg();
		ds.setStr(str);
		ds.setX(x);
		ds.setY(y);
		return ds;
	}
	
	public static class DrawStringMsg {
		String str;
		int x;
		int y;
		
		public DrawStringMsg(){}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		};
	}
	
	public static ClearDisplayMsg createClearDisplayMsg(){
		return new ClearDisplayMsg();
	}
	
	public static class ClearDisplayMsg {
		public ClearDisplayMsg(){};
	}
}
