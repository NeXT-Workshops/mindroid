package org.mindroid.impl.robot;

import org.mindroid.api.robot.IDifferentialPilot;
import org.mindroid.common.messages.motor.synchronization.SyncedMotorOpFactory;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.SynchronizedMotors;

/**
 * Precise controlling of the robot.
 */
public class DifferentialPilot implements IDifferentialPilot {

    private MotorProvider motorProvider;
    private EV3PortID leftMotor;
    private EV3PortID rightMotor;
    private float wheelDiameter;
    private float trackWidth;
    private final float wheelCircumference;
    private final float trackCircumference;

    /**
     *
     * @param motorProvider motorprovider to access synced motor group
     * @param leftMotor port of left motor
     * @param rightMotor port of right motor
     * @param wheelDiameter in cm
     * @param trackWidth in cm
     */
    public DifferentialPilot(MotorProvider motorProvider, EV3PortID leftMotor, EV3PortID rightMotor, float wheelDiameter, float trackWidth) {
        this.motorProvider = motorProvider;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.wheelDiameter = wheelDiameter;
        this.trackWidth = trackWidth;
        wheelCircumference = (float) (Math.PI * this.wheelDiameter);
        trackCircumference = (float) (Math.PI * this.trackWidth);
    }

    @Override
    public void forward(float distance) {
        int degrees = calculateDegreesByDistance(distance);
        System.out.println("[DifferentialPilot:forward("+distance+")] calculated degrees: "+degrees);
        SynchronizedMotorOperation forward = SyncedMotorOpFactory.createRotateOperation(degrees);

        SynchronizedMotorOperation[] operations = getNoOperationSet();

        if (!setMotorOperations(forward,forward, operations)){
            return; //TODO maybe errorhandling
        }

        motorProvider.getSynchronizedMotors().executeSynchronizedOperation(operations,true); //TODO Make Wait complete
    }

    @Override
    public void backward(float distance) {
        int degrees = (-1)*calculateDegreesByDistance(distance);

        SynchronizedMotorOperation backward = SyncedMotorOpFactory.createRotateOperation(degrees);

        SynchronizedMotorOperation[] operations = getNoOperationSet();

        if (!setMotorOperations(backward,backward, operations)){
            return; //TODO maybe errorhandling
        }

        motorProvider.getSynchronizedMotors().executeSynchronizedOperation(operations,true); //TODO Make Wait complete
    }



    @Override
    public void turnLeft(int degrees) {
        int rotateDegree = calculateCircularArc(degrees);

        SynchronizedMotorOperation forward = SyncedMotorOpFactory.createRotateOperation(rotateDegree);
        SynchronizedMotorOperation backward = SyncedMotorOpFactory.createRotateOperation((-1)*rotateDegree);

        SynchronizedMotorOperation[] operations = getNoOperationSet();

        if (!setMotorOperations(backward,forward, operations)){
            return; //TODO maybe errorhandling
        }

        motorProvider.getSynchronizedMotors().executeSynchronizedOperation(operations,true); //TODO Make Wait complete
    }

    @Override
    public void turnRight(int degrees) {
        int rotateDegree = calculateCircularArc(degrees);

        SynchronizedMotorOperation forward = SyncedMotorOpFactory.createRotateOperation(rotateDegree);
        SynchronizedMotorOperation backward = SyncedMotorOpFactory.createRotateOperation((-1)*rotateDegree);

        SynchronizedMotorOperation[] operations = getNoOperationSet();

        if (!setMotorOperations(forward,backward, operations)){
            return; //TODO maybe errorhandling
        }

        motorProvider.getSynchronizedMotors().executeSynchronizedOperation(operations,true); //TODO Make Wait complete
    }

    /**
     * Calculates the Degree the wheel has to rotate to travel the given distance.
     * @param distance to travel
     * @return degree the wheel needs to rotate to travel the wanted distance
     */
    private int calculateDegreesByDistance(float distance){
        return (int) ((distance/ wheelCircumference)*360f);
    }

    /**
     * Returns the Wheel rotation degree to turn the robot by given degrees
     * @param degrees - degrees to turn the robot
     * @return wheel rotation degree
     */
    private int calculateCircularArc(int degrees){
        return calculateDegreesByDistance((((float) degrees)/360f * trackCircumference));
    }


    /**
     * Returns the index of the Operation[] array to control the given motor
     * @param port port of the motor
     * @return index
     */
    private int getIndex(EV3PortID port){
        if(port == EV3PortIDs.PORT_A){
            return 0;
        }else if(port == EV3PortIDs.PORT_B) {
            return 1;
        }else if(port == EV3PortIDs.PORT_C) {
            return 2;
        }else if(port == EV3PortIDs.PORT_D) {
            return 3;
        }
        return -1;
    }

    /**
     * Sets the motor operation at the proper array slot
     * @param leftOperation - operation of the left motor
     * @param rightOperation - operation of the right motor
     * @param operations - operations array with length 4
     * @return true if success else false.
     */
    private boolean setMotorOperations(SynchronizedMotorOperation leftOperation,SynchronizedMotorOperation rightOperation, SynchronizedMotorOperation[] operations) {
        //Set left motor Operation
        int index = getIndex(leftMotor);
        if(index < 0 || index > 3){
            return false;
        }else{
            operations[index] = leftOperation;
        }

        //Set right motor Operation
        index = getIndex(rightMotor);
        if(index < 0 || index > 3){
            return false;
        }else{
            operations[index] = rightOperation;
        }
        return true;
    }

    /**
     * Returns an array of size 4 with filled with SynchronizedMotorOperation.NoOperation
     * @return array of synced motor operations
     */
    private SynchronizedMotorOperation[] getNoOperationSet(){
        SynchronizedMotorOperation noOp = SyncedMotorOpFactory.createNoOperation();
        return new SynchronizedMotorOperation[]{noOp,noOp,noOp,noOp};
    }

}
