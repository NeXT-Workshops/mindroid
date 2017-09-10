package org.mindroid.common.messages.motor;

public class MotorState {

    int rotationSpeed;
    int limitAngle;
    int acceleration;
    int tachoCount;
    float position;
    float maxSpeed;

    public MotorState() { }


    //TODO refactor
    public MotorState(int rotationSpeed, int limitAngle, int acceleration, int tachoCount, float position,
                             float maxSpeed) {
        super();
        this.rotationSpeed = rotationSpeed;
        this.limitAngle = limitAngle;
        this.acceleration = acceleration;
        this.tachoCount = tachoCount;
        this.position = position;
        this.maxSpeed = maxSpeed;
    }



    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public int getLimitAngle() {
        return limitAngle;
    }

    public void setLimitAngle(int limitAngle) {
        this.limitAngle = limitAngle;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getTachoCount() {
        return tachoCount;
    }

    public void setTachoCount(int tachoCount) {
        this.tachoCount = tachoCount;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
