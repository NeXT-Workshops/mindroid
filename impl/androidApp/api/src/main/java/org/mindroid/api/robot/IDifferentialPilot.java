package org.mindroid.api.robot;

public interface IDifferentialPilot {

    /**
     * Drive driveDistanceForward the given distance
     * This is a blocked operation.
     * @param distance in cm
     */
    void driveDistanceForward(float distance);

    /**
     * Drive driveDistanceBackward the given distance
     * This is a blocked operation.
     * @param distance in cm
     */
    void driveDistanceBackward(float distance);

    /**
     * Drives both motors synchronized forward.
     * Method is unblocked.
     *
     * @param speed - speed of the motors [0-1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    void driveFroward(int speed);

    /**
     * Drives both motors  synchronized backward.
     * Method is unblocked.
     *
     * @param speed - speed of the motors [0-1000] deg/sec. Possible MaxSpeed depends on battery power!
     */
    void driveBackward(int speed);

    /**
     * Both motors will be set in flt mode.
     * Motors are synchronized and method is blocking
     */
    void flt();


    /**
     * Turn left by given degrees.
     * This is a blocked operation.
     * @param degrees degrees to turn
     */
    void turnLeft(int degrees);

    /**
     * Turn right
     * This is a blocked operation.
     * @param degrees  degrees to turn
     */
    void turnRight(int degrees);

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
