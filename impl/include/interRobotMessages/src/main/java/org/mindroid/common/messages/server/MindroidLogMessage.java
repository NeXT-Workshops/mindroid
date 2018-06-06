package org.mindroid.common.messages.server;

public class MindroidLogMessage {
    private final RobotId robotID;
    private final String log;

    public MindroidLogMessage(RobotId robotID, String log) {
        this.robotID = robotID;
        this.log = log;
    }

    public RobotId getRobotID() {
        return robotID;
    }

    public String getLog() {
        return log;
    }

    @Override
    public String toString() {
        return "MindroidLogMessage{" +
                "robotID=" + robotID.getValue() +
                ", log='" + log + '\'' +
                '}';
    }
}
