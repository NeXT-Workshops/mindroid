package org.mindroid.impl.statemachine.properties;

import org.mindroid.api.statemachine.properties.AbstractTimeProperty;
import org.mindroid.api.statemachine.properties.IProperty;

/**
 * Created by torben on 10.03.2017.
 */
public class Seconds extends AbstractTimeProperty {

    public Seconds(long time){
        super((time*1000L));
    }

    @Override
    public IProperty copy() {
        Seconds sec = new Seconds(getTime()/1000);
        sec.setSource(getSource());
        return sec;
    }
}
