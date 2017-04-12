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
        final ServerLogMessage msg = new ServerLogMessage(new RobotId("Robot 1"), LogLevel.INFO, "Content");
        Assert.assertEquals(new RobotId("Robot 1"), msg.getSource());
        Assert.assertEquals(LogLevel.INFO, msg.getLogLevel());
        Assert.assertEquals("Content", msg.getContent());
    }
}
