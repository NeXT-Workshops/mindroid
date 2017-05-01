package org.mindroid.api.statemachine.properties;

import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 10.03.2017.
 */
public abstract class EV3SensorPorperty implements IEV3SensorPorperty {

    private float value;
    private EV3PortID port;

    public EV3SensorPorperty(float value, EV3PortID port) {
        this.value = value;
        this.port = port;
    }

    @Override
    public float getValue() {
        return value;
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

        if (Float.compare(that.value, value) != 0) return false;
        return port != null ? port.equals(that.port) : that.port == null;
    }

    @Override
    public int hashCode() {
        int result = (value != +0.0f ? Float.floatToIntBits(value) : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }
}
