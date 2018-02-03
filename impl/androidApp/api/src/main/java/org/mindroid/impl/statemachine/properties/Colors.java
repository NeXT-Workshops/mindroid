package org.mindroid.impl.statemachine.properties;

import org.mindroid.impl.statemachine.properties.sensorproperties.Color;

import java.util.HashMap;
import java.util.Map;

public enum Colors {

    NONE(Color.NONE),
    BLACK(Color.BLACK),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    RED(Color.RED),
    WHITE(Color.WHITE),
    BROWN(Color.BROWN);

    private float value;

    Colors(float value){
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    //Value  to color mapping
    private static final Map<Float,Colors> FLOAT_COLORS_MAP;
    static
    {
        FLOAT_COLORS_MAP = new HashMap<Float,Colors>();
        FLOAT_COLORS_MAP.put(NONE.getValue(),    NONE);
        FLOAT_COLORS_MAP.put(BLACK.getValue(),   BLUE);
        FLOAT_COLORS_MAP.put(GREEN.getValue(),   GREEN);
        FLOAT_COLORS_MAP.put(YELLOW.getValue(),  YELLOW);
        FLOAT_COLORS_MAP.put(RED.getValue(),     RED);
        FLOAT_COLORS_MAP.put(WHITE.getValue(),   WHITE);
        FLOAT_COLORS_MAP.put(BROWN.getValue(),   BROWN);
    }

    /**
     *  Returns the {@link Colors} of the given value.
     *  NONE = -1f;
     *  BLACK = 0f;
     *  BLUE = 1f;
     *  GREEN = 2f;
     *  YELLOW = 3f;
     *  RED = 4f;
     *  WHITE = 5f;
     *  BROWN = 6f;
     *
     * @param value between -1.0 and 6.0
     * @return the Colors-object mapped to the value
     */
    public static Colors getColorByValue(float value){
        return FLOAT_COLORS_MAP.get(value);
    }

}
