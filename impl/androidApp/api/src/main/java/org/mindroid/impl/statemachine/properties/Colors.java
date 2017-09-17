package org.mindroid.impl.statemachine.properties;

import org.mindroid.impl.statemachine.properties.sensorproperties.Color;

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
}
