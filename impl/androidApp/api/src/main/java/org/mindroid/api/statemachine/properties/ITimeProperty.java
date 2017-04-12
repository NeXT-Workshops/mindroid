package org.mindroid.api.statemachine.properties;

import org.mindroid.api.statemachine.IState;

/**
 * Created by torben on 10.03.2017.
 */
public interface ITimeProperty extends IProperty {

    public long getTime();
    public void setSource(IState sourceState);
    public IState getSource();
}
