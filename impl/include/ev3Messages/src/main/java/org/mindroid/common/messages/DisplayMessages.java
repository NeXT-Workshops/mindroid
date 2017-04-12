package org.mindroid.common.messages;

public class DisplayMessages {



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
	
	public static DrawString drawString(String str, int x, int y){
		DrawString ds = new DrawString();
		ds.setStr(str);
		ds.setX(x);
		ds.setY(y);
		return ds;
	}
	
	public static class DrawString{
		String str;
		int x;
		int y;
		
		DrawString(){}

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
	
	public static ClearDisplay clearDisplay(){
		return new ClearDisplay();
	}
	
	public static class ClearDisplay{
		ClearDisplay(){};
	}
}
