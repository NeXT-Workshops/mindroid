package org.mindroid.common.messages.server;

import java.util.Objects;

public class MindroidMessage {
    private final RobotId source;
    private final MessageType messageType;
    private final String content;
    private final Destination destination;
    private int runtimeID = -1;


    public MindroidMessage(final RobotId source, final Destination destination, final MessageType messageType, final String content, final int runtimeID) {
        this.source = source;
        this.messageType = messageType;
        this.content = content;
        this.destination = destination;
        this.runtimeID = runtimeID;
    }

    public MindroidMessage(RobotId source, Destination destination, MessageType messageType, String content) {
        this.source = source;
        this.messageType = messageType;
        this.content = content;
        this.destination = destination;
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

    public int getRuntimeID() {
        return runtimeID;
    }

    public boolean isLogMessage() {
        return destination.getValue().equals(Destination.SERVER_LOG.getValue())&&!messageType.equals(MessageType.REGISTRATION);
    }

    public boolean isBroadcastMessage() {
        return destination.getValue().equals(Destination.BROADCAST.getValue());
    }

    @Override
    public String toString() {
        return "MindroidMessage{" +
                "source=" + source +
                ", messageType=" + messageType +
                ", content='" + content + '\'' +
                ", destination=" + destination +
                ", runtimeID=" + runtimeID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MindroidMessage that = (MindroidMessage) o;

        if (runtimeID != that.runtimeID) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (messageType != that.messageType) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return destination != null ? destination.equals(that.destination) : that.destination == null;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        result = 31 * result + runtimeID;
        return result;
    }
}
