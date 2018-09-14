package org.mindroid.common.messages.server;

import java.util.Objects;

/**
 * Created by Felicia Ruppel on 19.04.17.
 */
public class Destination {

    public static final Destination SERVER_LOG = new Destination("Server");
    public static final Destination BROADCAST = new Destination("Broadcast");

    private final String value;

    public Destination(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Destination [" + value + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination destination = (Destination) o;
        return Objects.equals(value, destination.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
