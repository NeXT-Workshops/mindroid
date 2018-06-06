package org.mindroid.api.ev3;

public enum EV3StatusLightColor
{
	OFF(0),
	GREEN(1),
	RED(2),
	YELLOW(3);

	int val;
	
	EV3StatusLightColor(int val){
		this.val = val;
	}

	public int getValue() {
		return val;
	}
	
	
}

