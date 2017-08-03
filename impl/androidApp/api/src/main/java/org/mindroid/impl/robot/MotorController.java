package org.mindroid.impl.robot;

import org.mindroid.api.motor.IMotor;
import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;

/**
 * Created by torben on 02.03.2017.
 */

public class MotorController implements IMotorControl {

    public static final int FORWARD = 0; //TODO
    public static final int BACKWARD = 1; //TODO

    private Robot robot;

    public MotorController(Robot robot){
        this.robot = robot;
    }

    /**
     * Set the motor speed
     * @param motorPort port of the motor
     * @param speed 1-100% motor speed value
     */
    @Override
    public final void setMotorSpeed(EV3PortID motorPort, int speed) {
        IMotor IMotor = getMotor(motorPort);
        if(IMotor != null){
            IMotor.setSpeed(speed);
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
    public final  void setMotorDirection(EV3PortID motorPort, int direction) {
        IMotor IMotor = getMotor(motorPort);
        if(IMotor != null){
            switch(direction) {
                case FORWARD:   IMotor.forward(); break;
                case BACKWARD:  IMotor.backward(); break;
                default:
                    System.err.println("unknown direction at "+motorPort);
            }
        }else{
                System.err.println("Unknown motorport! "+motorPort);
        }
    }

    /**
     * Stop the motor
     * @param motorPort stops the motor at motor port
     */
    @Override
    public final void stop(EV3PortID motorPort) {
        IMotor IMotor = getMotor(motorPort);

        if(IMotor != null){
            IMotor.stop();
            IMotor.setSpeed(0);
        }else{
            System.err.println("Unknown motorport! "+motorPort);
        }
    }

    /**
     * Rotate the motor: Only works with regulated Motor
     * @param motorPort port of the motor
     * @param angle +/- angle to rotate
     */
    @Override
    public final void rotate(EV3PortID motorPort,int angle) {
        IMotor IMotor = getMotor(motorPort);

        if(IMotor != null){
            if(IMotor instanceof RegulatedMotor){
                ((RegulatedMotor) IMotor).rotate(angle);
            }else{
                System.err.println("Motortype does not support this operation (Rotate()) at "+motorPort);
            }
        }else{
            System.err.println("Unknown motorport!");
        }
    }

    /**
     * Rotate to a specified angle: Only works with regulated Motor
     * @param motorPort port of the motor
     * @param angle angle to rotate to
     */
    @Override
    public final void rotateTo(EV3PortID motorPort,int angle) {
        IMotor IMotor = getMotor(motorPort);

        if(IMotor != null){
            if(IMotor instanceof RegulatedMotor){
                ((RegulatedMotor) IMotor).rotateTo(angle);
            }else{
                System.err.println("Motortype does not support this operation (rotateTo()) at "+motorPort);
            }
        }else{
            System.err.println("Unknown motorport! "+motorPort);
        }
    }


    private final IMotor getMotor(EV3PortID motorPort){
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
