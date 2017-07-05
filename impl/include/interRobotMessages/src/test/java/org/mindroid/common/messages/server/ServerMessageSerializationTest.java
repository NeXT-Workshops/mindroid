package org.mindroid.common.messages.server;

import org.junit.Assert;
import org.junit.Test;

public class ServerMessageSerializationTest {

    @Test
    public void testSerializationAndDeserializationOfMessages() {
        final MessageMarshaller serializer = new MessageMarshaller();
        final MindroidMessage logMessage = new MindroidMessage(new RobotId("Robot 2"), Destination.SERVER_LOG, MessageType.INFO, "Everything OK");
        final String serializedLogMessage = serializer.serialize(logMessage);
        final MindroidMessage restoredLogMessage = serializer.deserializeMessage(
                serializedLogMessage);
        Assert.assertEquals(logMessage, restoredLogMessage);
    }

}