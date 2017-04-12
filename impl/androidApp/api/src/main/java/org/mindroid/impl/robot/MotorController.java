package org.mindroid.impl.robot;

import org.mindroid.api.motor.Motor;
import org.mindroid.api.motor.RegulatedMotor;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.common.messages.RegulatedMotorMessages;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.EV3MotorManager;
import org.mindroid.impl.robot.Robot;

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

    @Override
    public void setMotorSpeed(EV3PortID motorPort, int speed) {
        Motor motor = getMotor(motorPort);
        if(motor != null){
            motor.setSpeed(speed);
        }else{
            System.err.println("Unknown motorport! at "+motorPort);
        }
    }

    @Override
    public void setMotorDirection(EV3PortID motorPort, int direction) {
        Motor motor = getMotor(motorPort);
        if(motor != null){
            switch(direction) {
                case FORWARD:   motor.forward(); break;
                case BACKWARD:  motor.backward(); break;
                default:
                    System.err.println("unknown direction at "+motorPort);
            }
        }else{
                System.err.println("Unknown motorport! "+motorPort);
        }
    }

    @Override
    public void stop(EV3PortID motorPort) {
        Motor motor = getMotor(motorPort);

        if(motor != null){
            motor.stop();
        }else{
            System.err.println("Unknown motorport! "+motorPort);
        }
    }

    @Override
    public void rotate(EV3PortID motorPort,int angle) {
        Motor motor = getMotor(motorPort);

        if(motor != null){
            if(motor instanceof RegulatedMotor){
                ((RegulatedMotor) motor).rotate(angle);
            }else{
                System.err.println("Motortype does not support this operation (Rotate()) at "+motorPort);
            }
        }else{
            System.err.println("Unknown motorport!");
        }
    }

    @Override
    public void rotateTo(EV3PortID motorPort,int angle) {
        Motor motor = getMotor(motorPort);

        if(motor != null){
            if(motor instanceof RegulatedMotor){
                ((RegulatedMotor) motor).rotateTo(angle);
            }else{
                System.err.println("Motortype does not support this operation (rotateTo()) at "+motorPort);
            }
        }else{
            System.err.println("Unknown motorport! "+motorPort);
        }
    }


    private Motor getMotor(EV3PortID motorPort){
        if(motorPort.equals(EV3PortIDs.PORT_A)){
            return robot.getMotor_A();
        }else if(motorPort.equals(EV3PortIDs.PORT_B)){
            return  robot.getMotor_B();
        }else if(motorPort.equals(EV3PortIDs.PORT_C)){
            return  robot.getMotor_C();
        }else if(motorPort.equals(EV3PortIDs.PORT_D)){
            return  robot.getMotor_D();
        }

        return null;
    }

}
