package org.mindroid.common.messages.server;

import org.junit.Assert;
import org.junit.Test;

public class ServerMessageSerializationTest {

    @Test
    public void testSerializationAndDeserializationOfMessages() {
        final ServerMessageMarshaller serializer = new ServerMessageMarshaller();
        final ServerLogMessage logMessage = new ServerLogMessage(new RobotId("Robot 2"), LogLevel.INFO, "Everything OK");
        final String serializedLogMessage = serializer.serialize(logMessage);
        final ServerLogMessage restoredLogMessage = serializer.deserializeLogMessage(
                serializedLogMessage);
        Assert.assertEquals(logMessage, restoredLogMessage);
    }

}