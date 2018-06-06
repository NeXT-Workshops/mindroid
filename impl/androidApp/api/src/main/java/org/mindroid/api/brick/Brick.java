package org.mindroid.api.brick;

import org.mindroid.api.robot.control.IBrickControl;

public abstract class Brick implements IBrickControl {

    public abstract boolean isBrickReady();

    public abstract void resetBrickState();

    public abstract boolean connect();

    public abstract void disconnect();
}
