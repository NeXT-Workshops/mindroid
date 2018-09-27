package org.mindroid.common.messages.server;

import java.util.Objects;

/**
 * Created by Felicia Ruppel on 19.04.17.
 */
public class Destination extends RobotId{

    public static final Destination SERVER_LOG = new Destination("Server");
    public static final Destination BROADCAST = new Destination("Broadcast");

    public Destination(String value) {
        super(value);
    }
}
