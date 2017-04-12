package org.mindroid.api.ev3;

import org.mindroid.impl.ev3.EV3PortID;

import java.util.Collection;

public interface EV3RotationMotorEnabled
{
   Collection<EV3PortID> getMotorPorts();
   
   void setMotorSpeed(int motorSpeed);
   
   void setMotorSpeed(int motorSpeed, EV3PortID port);
   
   void stopMotor(EV3PortID port);
   
   void stopMotors();
}
