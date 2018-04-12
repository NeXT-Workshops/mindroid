package org.mindroid.common.messages.display;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

/**
 * Message to Clear the Display
 * @author Torben
 */
public class ClearDisplayMessage implements ILoggable {
    public ClearDisplayMessage(){}

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
