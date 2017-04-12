package org.mindroid.common.messages.server;

import java.util.Objects;

/**
 * @author Roland Kluge - Initial implementation
 */
public class RobotId {
    private final String value;

    public RobotId(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RobotId [" + value + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RobotId robotId = (RobotId) o;
        return Objects.equals(value, robotId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
