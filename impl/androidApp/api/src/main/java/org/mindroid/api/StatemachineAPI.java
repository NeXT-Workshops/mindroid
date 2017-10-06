package org.mindroid.api;

import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.statemachine.StatemachineCollection;

/**
 * Created by Torbe on 03.05.2017.
 */
public abstract class StatemachineAPI extends BasicAPI implements IMindroidMain {

    public final StatemachineCollection statemachineCollection = new StatemachineCollection();


    @Override
    public final StatemachineCollection getStatemachineCollection() {
        return  statemachineCollection;
    }

    /**
     * Forwards motors at ports A and D.
     * Speed is set to 50.
     */
    public void forward() {
        getMotorProvider().getMotor(EV3PortIDs.PORT_A).forward();
        getMotorProvider().getMotor(EV3PortIDs.PORT_D).forward();
        getMotorProvider().getMotor(EV3PortIDs.PORT_A).setSpeed(50);
        getMotorProvider().getMotor(EV3PortIDs.PORT_D).setSpeed(50);

    }

    /**
     * Backwards motors at ports A and D
     * Speed is set to 50.
     */
    public void backward() {
        getMotorProvider().getMotor(EV3PortIDs.PORT_A).backward();
        getMotorProvider().getMotor(EV3PortIDs.PORT_D).backward();
        getMotorProvider().getMotor(EV3PortIDs.PORT_A).setSpeed(50);
        getMotorProvider().getMotor(EV3PortIDs.PORT_D).setSpeed(50);
    }

}
