package org.mindroid.api.statemachine.properties;

import org.mindroid.api.statemachine.IState;

/**
 * Created by torben on 21.03.2017.
 */
public abstract class AbstractTimeProperty implements ITimeProperty {
    /**
     * in millis
     */
    private long time;
    private IState source;

    public AbstractTimeProperty(long time_in_millies){
        this.time = time_in_millies;
    }

    @Override
    public long getTime(){
        return time;
    }

    @Override
    public IState getSource(){
        return this.source;
    }

    @Override
    public void setSource(IState source){
        this.source = source;
    }



}
