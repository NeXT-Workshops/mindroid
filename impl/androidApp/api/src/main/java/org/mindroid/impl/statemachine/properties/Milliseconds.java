package org.mindroid.impl.statemachine.properties;

import org.mindroid.api.statemachine.properties.AbstractTimeProperty;
import org.mindroid.api.statemachine.properties.IProperty;

/**
 * Created by torben on 21.03.2017.
 */
public class Milliseconds extends AbstractTimeProperty {

    public Milliseconds(long time){
        super(time);
    }

    @Override
    public IProperty copy() {
        Milliseconds mill = new Milliseconds(getTime());
        mill.setSource(mill.getSource());
        return mill;
    }
}