package org.mindroid.api.robot;

public interface IDifferentialPilot {

    /**
     * Drive forward the given distance
     * This is a blocked operation.
     * @param distance in cm
     */
    void forward(float distance);

    /**
     * Drive backward the given distance
     * This is a blocked operation.
     * @param distance in cm
     */
    void backward(float distance);

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


}
