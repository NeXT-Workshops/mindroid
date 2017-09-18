package org.mindroid.api.robot;

public interface IDifferentialPilot {

    /**
     * Drive forward the given distance
     * @param distance in cm
     */
    void forward(float distance);

    /**
     * Drive backward the given distance
     * @param distance in cm
     */
    void backward(float distance);

    /**
     * Turn left
     * @param degrees degrees to turn
     */
    void turnLeft(int degrees);

    /**
     * Turn right
     * @param degrees  degrees to turn
     */
    void turnRight(int degrees);


}
