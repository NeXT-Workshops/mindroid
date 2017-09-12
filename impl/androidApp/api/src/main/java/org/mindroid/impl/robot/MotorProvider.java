package org.mindroid.impl.robot;

import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.motor.EV3RegulatedMotorEndpoint;
import org.mindroid.impl.motor.Motor;

/**
 *
 * This class is used to control all Motors. The wanted operation will be executed by the motor
 * given by the port.
 *
 *
 * Created by torben on 02.03.2017.
 */
public class MotorProvider implements org.mindroid.api.robot.control.MotorProvider {

    private Robot robot;

    public MotorProvider(Robot robot){
        this.robot = robot;
    }

    private final EV3RegulatedMotorEndpoint getMotorEndpoint(EV3PortID motorPort){
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

    /**
     * Returns a Motor-Object as an simpler interface,
     * @param motorPort port of the Motor you want to control.
     * @return Motor object to control the motor plugged into that port
     */
    public Motor getMotor(EV3PortID motorPort){
        if(motorPort == EV3PortIDs.PORT_A || motorPort == EV3PortIDs.PORT_B || motorPort == EV3PortIDs.PORT_C ||motorPort == EV3PortIDs.PORT_D){
            return new Motor(getMotorEndpoint(motorPort), motorPort);
        }
        return null;
        //TODO maybe throw an error if motor is null?
    }
}
