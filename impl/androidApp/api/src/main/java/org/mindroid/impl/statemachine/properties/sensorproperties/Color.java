package org.mindroid.impl.statemachine.properties.sensorproperties;

import org.mindroid.api.statemachine.properties.EV3SensorPorperty;
import org.mindroid.api.statemachine.properties.IProperty;
import org.mindroid.api.statemachine.properties.SimpleEV3SensorProperty;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.impl.ev3.EV3PortID;

/**
 * Created by torben on 10.03.2017.
 */
public class Color extends SimpleEV3SensorProperty {
    //NONE, BLACK, BLUE, GREEN, YELLOW, RED, WHITE, BROWN
    public static final float NONE = 0f;
    public static final float BLACK = 1f;
    public static final float BLUE = 2f;
    public static final float GREEN = 3f;
    public static final float YELLOW = 4f;
    public static final float RED = 5f;
    public static final float WHITE = 6f;
    public static final float BROWN = 7f;

    /**
     *
     * @param port
     */
    public Color(EV3PortID port) {
        super(port);
    }

    @Override
    public SensorMessages.SensorMode_ getSensormode() {
        return SensorMessages.SensorMode_.COLOR_ID;
    }

    @Override
    public IProperty copy() {
        return new Color(getSensorPort());
    }
}
