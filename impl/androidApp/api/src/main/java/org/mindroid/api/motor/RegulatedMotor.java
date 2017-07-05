package org.mindroid.api.motor;

public interface RegulatedMotor extends Motor{
	
	public static final int MAX_SPEED = 1000;
	public static final int MIN_SPEED = 0;

	
	/**
	 * @param speed 0 - 1000
	 */

	public void rotate(int angle);
	public void rotateTo(int angle);

	public int getRotationSpeed();

	public int getLimitAngle();

	public int getAcceleration();

	public int getTachoCount();

	public float getPosition();

	public float getMaxSpeed();
	
}
