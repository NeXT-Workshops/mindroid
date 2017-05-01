package org.mindroid.api.statemachine.constraints;

import org.mindroid.api.statemachine.properties.IMessageProperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.ITimeProperty;

/**
 * Created by torben on 10.03.2017.
 */
public abstract class AbstractComparator implements IComparator {

    IProperty property;

    public AbstractComparator(IProperty property){
        this.property = property;
    }

    @Override
    public IProperty getProperty() {
        return property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractComparator that = (AbstractComparator) o;

        return property != null ? property.equals(that.property) : that.property == null;
    }

    @Override
    public int hashCode() {
        return property != null ? property.hashCode() : 0;
    }


}
