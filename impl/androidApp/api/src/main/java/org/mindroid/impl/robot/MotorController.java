package org.mindroid.impl.robot;

import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;

/**
 *
 * Used to control the Motor at a given port
 *
 * Created by torben on 02.03.2017.
 */
public class MotorController implements IMotorControl {

    private Robot robot;

    public MotorController(Robot robot){
        this.robot = robot;
    }

    /**
     * Set the motor speed
     * @param motorPort port of the motor
     * @param speedInPercentage 1-100% motor speed value
     */
    @Override
    public final void setMotorSpeed(EV3PortID motorPort, int speedInPercentage) {
        RegulatedMotor motor = getMotor(motorPort);
        if(motor != null){
            motor.setSpeed(speedInPercentage);
        }else{
            System.err.println("Unknown motorport! at "+motorPort);
        }
    }

    /**
     * Set the Motor direction
     * @param motorPort port of the motor
     * @param direction forward/backward
     */
    @Override
    public final  void setMotorDirection(EV3PortID motorPort, final MotorDirection direction) {
        final RegulatedMotor motor = getMotor(motorPort);
        if(motor != null){
            switch(direction) {
                case FORWARD:   motor.forward(); break;
                case BACKWARD:  motor.backward(); break;
                default:
                    System.err.println("unknown direction at "+motorPort);
            }
        } else{
            System.err.println("Unknown motorport! "+motorPort);
        }
    }

    /**
     * Stop the motor
     * @param motorPort stops the motor at motor port
     */
    @Override
    public final void stop(EV3PortID motorPort) {
        RegulatedMotor motor = getMotor(motorPort);

        if(motor != null){
            motor.stop();
            motor.setSpeed(0);
        }else{
            System.err.println("Unknown motorport! "+motorPort);
            //TODO call App Error Handling
        }
    }

    /**
     * Rotate the motor: Only works with regulated Motor
     * @param motorPort port of the motor
     * @param angle +/- angle to rotate
     */
    @Override
    public final void rotate(EV3PortID motorPort,int angle) {
        RegulatedMotor motor = getMotor(motorPort);

        if(motor != null){
            motor.rotate(angle);
        }else{
            System.err.println("Unknown motorport!");
            //TODO Call App Error Handling
        }
    }

    /**
     * Rotate to a specified angle: Only works with regulated Motor
     * @param motorPort port of the motor
     * @param angle angle to rotate to
     */
    @Override
    public final void rotateTo(EV3PortID motorPort,int angle) {
        RegulatedMotor motor = getMotor(motorPort);

        if(motor != null){
            motor.rotateTo(angle);
        }else{
            System.err.println("Unknown motorport! "+motorPort);
            //TODO Call App Error Handling
        }
    }


    private final RegulatedMotor getMotor(EV3PortID motorPort){
        if(motorPort.equals(EV3PortIDs.PORT_A)){
            return robot.getIMotor_A();
        }else if(motorPort.equals(EV3PortIDs.PORT_B)){
            return  robot.getIMotor_B();
        }else if(motorPort.equals(EV3PortIDs.PORT_C)){
            return  robot.getIMotor_C();
        }else if(motorPort.equals(EV3PortIDs.PORT_D)){
            return  robot.getIMotor_D();
        }

        return null;
    }

}
