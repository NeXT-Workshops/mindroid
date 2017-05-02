package org.mindroid.common.messages.server;

import java.util.Objects;

public class MindroidMessage {
    private final RobotId source;
    private final MessageType messageType;
    private final String content;
    private final Destination destination;


    public MindroidMessage(final RobotId source, final Destination destination, final MessageType messageType, final String content) {
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
                ", destination=" + destination +
                ", messageType=" + messageType +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MindroidMessage that = (MindroidMessage) o;
        return Objects.equals(getSource(), that.getSource()) &&
                Objects.equals(getDestination(), that.getDestination()) &&
                getMessageType() == that.getMessageType() &&
                Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getDestination(), getMessageType(), getContent());
    }
}
