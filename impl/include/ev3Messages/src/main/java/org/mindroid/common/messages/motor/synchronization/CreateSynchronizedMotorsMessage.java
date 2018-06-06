package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.hardware.EV3MotorPort;

public class CreateSynchronizedMotorsMessage implements ILoggable {


    public CreateSynchronizedMotorsMessage(){

    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
