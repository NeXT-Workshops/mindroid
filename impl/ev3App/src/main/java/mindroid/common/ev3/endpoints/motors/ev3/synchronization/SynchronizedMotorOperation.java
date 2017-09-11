package mindroid.common.ev3.endpoints.motors.ev3.synchronization;

public enum SynchronizedMotorOperation {
    NO_OPERATION,
    FLT,
    ROTATE,
    ROTATE_TO,
    STOP;

    private int value = 0;

    SynchronizedMotorOperation(){

    }

    /**
     * Meaning of value:
     *  ROTATE: angle - through which the motor will rotate
     *  ROTATE TO: limitAngle - to which the motor will rotate, and then stop (in degrees). Includes any positive or negative int, even values > 360.
     *
     *  NO MEANING IN OTHER OPERATIONS THAN LISTED ABOVE!
     *
     * @param value value
     */
    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
//TODO check if it works with forward and backward as well?
