package org.mindroid.android.app.imperativeimpl;

import org.mindroid.api.ImperativeAPI;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;

public class TestSmallServoMotor extends ImperativeAPI {


    public TestSmallServoMotor() {
        super("Test_small_servo");
    }

    @Override
    public void run() {
        getMotorProvider().getMotor(EV3PortIDs.PORT_B).setSpeed(300);
        /*delay(100);
        getMotorProvider().getMotor(EV3PortIDs.PORT_B).rotate(180);
        delay(100);
        getMotorProvider().getMotor(EV3PortIDs.PORT_B).rotateTo(360);
        delay(100);*/
        getMotorProvider().getMotor(EV3PortIDs.PORT_B).forward();
        delay(1000);
        //getMotorProvider().getMotor(EV3PortIDs.PORT_B).backward();
        delay(100        );
        getMotorProvider().getMotor(EV3PortIDs.PORT_B).flt();
    }
}
