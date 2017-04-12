package org.mindroid.common.messages.server;

import java.util.Objects;

public class ServerLogMessage {
    private final RobotId source;
    private final LogLevel logLevel;
    private final String content;


    public ServerLogMessage(final RobotId source, final LogLevel logLevel, final String content) {
        this.source = source;
        this.logLevel = logLevel;
        this.content = content;
    }

    public RobotId getSource() {
        return source;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ServerLogMessage{" +
                "source=" + source +
                ", logLevel=" + logLevel +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerLogMessage that = (ServerLogMessage) o;
        return Objects.equals(getSource(), that.getSource()) &&
                getLogLevel() == that.getLogLevel() &&
                Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getLogLevel(), getContent());
    }
}
