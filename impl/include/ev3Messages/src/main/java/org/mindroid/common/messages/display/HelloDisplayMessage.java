package org.mindroid.common.messages.display;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

/**
 *
 * Send after the client Display endpoint is connnected;
 * @author Torben
 * TODO deprecated?
 *
 */
public class HelloDisplayMessage implements ILoggable {

    public HelloDisplayMessage() {}

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }

}
