package org.mindroid.api.statemachine.properties;

import org.mindroid.impl.ev3.EV3PortID;

/**
 *
 *
 * Created by torben on 10.03.2017.
 *
 *
 */
public abstract class EV3SensorPorperty implements IEV3SensorPorperty {

    private EV3PortID port;

    public EV3SensorPorperty(EV3PortID port) {
        this.port = port;
    }


    @Override
    public EV3PortID getSensorPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EV3SensorPorperty that = (EV3SensorPorperty) o;

        return port != null ? port.equals(that.port) : that.port == null;
    }

    @Override
    public int hashCode() {
        return port != null ? port.hashCode() : 0;
    }
}
