package org.mindroid.common.messages.server;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roland Kluge - Initial implementation
 */
public class ServerLogMessageTest {
    @Test
    public void testConstruction()
    {
        final MindroidMessage msg = new MindroidMessage(new RobotId("Robot 1"),Destination.SERVER_LOG, MessageType.INFO, "Content");
        Assert.assertEquals(new RobotId("Robot 1"), msg.getSource());
        Assert.assertEquals(MessageType.INFO, msg.getMessageType());
        Assert.assertEquals("Content", msg.getContent());
    }
}
