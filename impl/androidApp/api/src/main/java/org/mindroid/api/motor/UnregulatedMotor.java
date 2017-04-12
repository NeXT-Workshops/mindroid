package org.mindroid.api.motor;

public interface UnregulatedMotor extends Motor {
	
	
	public static final int MAX_POWER = 100;
	public static final int MIN_POWER = 0;
	

	public boolean isMoving();

	public int getPower();

	public int getTachoCount();
}
