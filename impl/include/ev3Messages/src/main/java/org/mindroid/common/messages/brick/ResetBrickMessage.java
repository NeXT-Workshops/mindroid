package org.mindroid.common.messages.brick;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

/**
 * Resets all functionality provided by the brick to the default state:
 * LED
 * Display
 * Sounds
 * ..
 */
public class ResetBrickMessage implements ILoggable {

    public ResetBrickMessage(){

    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
