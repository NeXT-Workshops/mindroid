package org.mindroid.api.robot;

public interface IDifferentialPilot {

    /**
     * Drive driveDistanceForward the given distance
     * This is a blocked operation.
     * @param distance - distance to drive in cm
     */
    void driveDistanceForward(float distance);

    /**
     * Drive driveDistanceForward the given distance
     * This is a blocked operation.
     * @param distance - distance to drive in cm
     * @param speed - speed
     */
    void driveDistanceForward(float distance, int speed);

    /**
     * Drive driveDistanceBackward the given distance
     * This is a blocked operation.
     * @param distance - distance to drive in cm
     */
    void driveDistanceBackward(float distance);

    /**
     * Drive driveDistanceBackward the given distance
     * This is a blocked operation.
     * @param distance - distance to drive in cm
     * @param speed - speed
     */
    void driveDistanceBackward(float distance, int speed);

    /**
     * Drives both motors synchronized forward.
     * Method is unblocked.
     *
     * @param speed - speed of the motors [0-1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    void driveForward(int speed);

    /**
     * Drives both motors synchronized forward.
     * Method is unblocked.
     *
     * Note: Uses the current speed of the motor
     */
    void driveForward();

    /**
     * Drives both motors  synchronized backward.
     * Method is unblocked.
     *
     * @param speed - speed of the motors [0-1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    void driveBackward(int speed);

    /**
     * Drives both motors synchronized forward.
     * Method is unblocked.
     *
     * Note: Uses the current speed of the motor
     */
    void driveBackward();

    /**
     * Both motors will be set in flt mode.
     * Motors are synchronized and method is unblocking
     */
    void flt();


    /**
     * Turn left by given degrees.
     * This is a blocked operation.
     * @param degrees degrees to turn
     */
    void turnLeft(int degrees);

    /**
     * Turn left by given degrees.
     * This is a blocked operation.
     * @param degrees degrees to turn
     * @param speed speed 0 - 1000
     */
    void turnLeft(int degrees, int speed);

    /**
     * Turn right
     * This is a blocked operation.
     * @param degrees  degrees to turn
     */
    void turnRight(int degrees);

    /**
     * Turn right.
     * This is a blocked operation.
     * @param degrees  degrees to turn
     * @param speed speed 0 - 1000
     */
    void turnRight(int degrees, int speed);

    /**
     * Blocked synchronized stop operation.
     * Left and Right motor will be stopped synchronized.
     */
    void stop();

    /**
     * Sets the motor speed of left and right motors.
     * Not a synchronized motor operation. Unblocking.
     *
     * @param speed - motorspeed
     */
    void setMotorSpeed(int speed);
}
