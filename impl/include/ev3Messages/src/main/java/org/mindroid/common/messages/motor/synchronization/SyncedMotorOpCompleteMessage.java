package org.mindroid.common.messages.motor.synchronization;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

/**
 * Sent if an blocked Synchronized Motor Operation is completed.
 */
public class SyncedMotorOpCompleteMessage implements ILoggable {

    public SyncedMotorOpCompleteMessage(){

    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
