package org.mindroid.api.motor;

public interface RegulatedMotor{
	
	public static final int MAX_SPEED = 1000;
	public static final int MIN_SPEED = 0;

	//TODO impement flt(),isStalled(), isMoving()
	/*
	 * This method returns true if the motor is attempting to rotate. The return value may not correspond to the actual motor movement.
	 * For example, If the motor is stalled, isMoving() will return true.
	 * After flt() is called, this method will return false even though the motor axle may continue to rotate by inertia. If the motor is stalled, isMoving() will return true. . A stall can be detected by calling isStalled();
	 *
	 * @return true iff the motor is attempting to rotate.
	 */
	//boolean isMoving();



	/**
	 * Causes motor to rotate forward until stop() or flt() is called.
	 */
	void forward();

	/**
	 * Causes motor to rotate backwards until stop() or flt() is called.
	 */
	void backward();

	/**
	 * Causes motor to stop, pretty much instantaneously.
	 */
	void stop();

	/**
	 * Sets desired motor speed , in degrees per second; The maximum reliably sustainable velocity is 100 x battery voltage under moderate load, such as a direct drive robot on the level.
	 * @param speed - value in degrees/sec
	 */
	void setSpeed(int speed);

	/**
	 * Rotate by the request number of degrees.
	 * @param angle - number of degrees to rotate relative to the current position
	 */
	void rotate(int angle);

	/**
	 * Rotate to the target angle. Do not return until the move is complete.
	 * @return limitAngle - Angle to rotate to.
	 */
	void rotateTo(int angle);

	/**
	 * Return the current velocity.
	 * @return current velocity in degrees/s
	 */
	int getRotationSpeed();

	/**
	 * Return the angle that this Motor is rotating to.
	 * @return angle - that this Motor is rotating to.
	 */
	int getLimitAngle();

	/**
	 * returns acceleration in degrees/second/second
	 * @return acceleration in degrees/second/second
	 */
	int getAcceleration();

	/**
	 * Returns the tachometer count.
	 * @return tachometer count
	 */
	int getTachoCount();

	/**
	 * From Lejos Doc:
	 * Returns the current position that the motor regulator is trying to maintain.
	 * Normally this will be the actual position of the motor and will be the same as the value returned by getTachoCount().
	 * However in some circumstances (activeMotors that are in the process of stalling, or activeMotors that have been forced out of position), the two values may differ.
	 * Note that if regulation has been suspended calling this method will restart it.
	 *
	 * @return the current position calculated by the regulator.
	 */
	float getPosition();

	/**
	 * Returns the maximum speed that can be maintained by the regulation system based upon the current state of the battery.
	 * @return the maximum speed of the Motor in degrees per second.
	 */
	float getMaxSpeed();




}
