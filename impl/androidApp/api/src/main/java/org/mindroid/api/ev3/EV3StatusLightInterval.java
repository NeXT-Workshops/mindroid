package org.mindroid.api.ev3;

public enum EV3StatusLightInterval
{
	ON(0), 
   	BLINKING(1), 
   	DOUBLE_BLINKING(2);
	
	int val;
	EV3StatusLightInterval(int val){
		this.val = val;
	}
	public int getValue() {
		return val;
	}
	
}
