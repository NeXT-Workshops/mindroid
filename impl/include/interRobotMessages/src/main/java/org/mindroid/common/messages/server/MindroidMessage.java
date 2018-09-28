package org.mindroid.common.messages.server;

import java.util.Objects;

public class MindroidMessage {
    private final RobotId source;
    private final Destination destination;
    private final MessageType messageType;
    private int sessionRobotCount;
    private final String content;
    public final static int QUIT_SESSION = -2;
    public final static int UNCOUPLED_SESSION = -1;
    public final static int START_SESSION = 0;

    public MindroidMessage(RobotId source, MessageType messageType, String content, Destination destination, int sessionRobotCount) {
        this.source = source;
        this.messageType = messageType;
        this.content = content;
        this.destination = destination;
        this.sessionRobotCount = sessionRobotCount;
    }

    public MindroidMessage(RobotId source, Destination destination, MessageType messageType, String content) {
        this.source = source;
        this.messageType = messageType;
        this.content = content;
        this.destination = destination;
        this.sessionRobotCount =-1;
    }

    public RobotId getSource() {
        return source;
    }

    public Destination getDestination() {
        return destination;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public int getSessionRobotCount() { return sessionRobotCount; }

    public boolean isLogMessage() {
        return destination.getValue().equals(Destination.SERVER_LOG.getValue()) && messageType.equals(MessageType.LOG) && !messageType.equals(MessageType.REGISTRATION);
    }
    // accepts any form of "Broadcast" in Destination as Broadcast message (case-insensitive)
    public boolean isBroadcastMessage() {
        return destination.getValue().toLowerCase().equals(Destination.BROADCAST.getValue().toLowerCase());
    }

    public boolean isRegistrationMessage(){
        return messageType.equals(MessageType.REGISTRATION);
    }
    public boolean isUnicastMessage(){
        return messageType.equals(MessageType.MESSAGE);
    }
    public boolean isSessionMessage() {
        return messageType.equals(MessageType.SESSION);
    }

    @Override
    public String toString() {
        return "MindroidMsg{" +
                "src=" + source +
                ", mType=" + messageType +
                ", content='" + content + '\'' +
                ", dest=" + destination +
                ", session=" + sessionRobotCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MindroidMessage that = (MindroidMessage) o;
        return sessionRobotCount == that.sessionRobotCount &&
                Objects.equals(source, that.source) &&
                messageType == that.messageType &&
                Objects.equals(content, that.content) &&
                Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, messageType, content, destination, sessionRobotCount);
    }
}
