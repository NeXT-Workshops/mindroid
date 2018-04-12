package org.mindroid.common.messages.motor;

public class MotorState {

    private int rotationSpeed = -1;
    private int limitAngle = -1;
    private int acceleration = -1;
    private int tachoCount = -1;
    private int speed = -1;
    private float position = -1f;
    private float maxSpeed = -1f;
    private boolean isMoving = false;

    public MotorState() { }


    public MotorState(int rotationSpeed, int limitAngle, int acceleration, int tachoCount, float position,
                             int speed,float maxSpeed, boolean isMoving) {
        this.rotationSpeed = rotationSpeed;
        this.limitAngle = limitAngle;
        this.acceleration = acceleration;
        this.tachoCount = tachoCount;
        this.position = position;
        this.maxSpeed = maxSpeed;
        this.isMoving = isMoving;
        this.speed = speed;
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

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "MotorState{" +
                "rotationSpeed=" + rotationSpeed +
                ", limitAngle=" + limitAngle +
                ", acceleration=" + acceleration +
                ", tachoCount=" + tachoCount +
                ", speed=" + speed +
                ", position=" + position +
                ", maxSpeed=" + maxSpeed +
                ", isMoving=" + isMoving +
                '}';
    }
}
