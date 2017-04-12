package org.mindroid.api;

import org.mindroid.api.statemachine.ITimeEvent;

/**
 * Created by torben on 19.03.2017.
 */
public interface ITimeEventListener {

    public void handleTimeEvent(ITimeEvent timeEvent);
}
