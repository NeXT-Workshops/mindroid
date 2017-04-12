package org.mindroid.common.messages.server;

import org.json.JSONObject;
import org.json.JSONWriter;

/**
 * @author Roland Kluge - Initial implementation
 */
public class ServerMessageMarshaller {

    private static final String KEY_SOURCE = "source";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_LOGLEVEL = "logLevel";
    private static final String KEY_TYPE = "type";
    private static final String VALUE_LOGMESSAGE_TYPE = "LogMessageType";

    public String serialize(final ServerLogMessage logMessage) {
        final JSONObject serializedMessage = new JSONObject();
        serializedMessage.put(KEY_TYPE, VALUE_LOGMESSAGE_TYPE);
        serializedMessage.put(KEY_SOURCE, serialize(logMessage.getSource()));
        serializedMessage.put(KEY_CONTENT, logMessage.getContent());
        serializedMessage.put(KEY_LOGLEVEL, serialize(logMessage.getLogLevel()));
        return serializedMessage.toString(2);
    }

    public ServerLogMessage deserializeLogMessage(final String serializedLogMessage) throws IllegalArgumentException {
        final JSONObject jsonObject = new JSONObject(serializedLogMessage);

        if (!VALUE_LOGMESSAGE_TYPE.equals(jsonObject.get(KEY_TYPE)))
            throw new IllegalArgumentException("Invalid or missing property '" + KEY_TYPE + "'. Expected: '" + VALUE_LOGMESSAGE_TYPE + "'");

        final ServerLogMessage logMessage = new ServerLogMessage(
                deserializeRobotId(jsonObject.get(KEY_SOURCE)),
                deserializeLogLevel(jsonObject.get(KEY_LOGLEVEL)),
                deserializeContent(jsonObject.get(KEY_CONTENT)));
        return logMessage;
    }

    private String deserializeContent(final Object content) {
        if (!(content instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + content.getClass());

        return String.class.cast(content);
    }

    private LogLevel deserializeLogLevel(final Object logLevel) {
        if (!(logLevel instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + logLevel.getClass());

        return LogLevel.valueOf(String.class.cast(logLevel));
    }

    private RobotId deserializeRobotId(final Object source) {
        if (!(source instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + source.getClass());

        return new RobotId(String.class.cast(source));
    }

    private String serialize(final LogLevel logLevel) {
        return logLevel.toString();
    }

    private String serialize(final RobotId source) {
        return source.getValue();
    }


}
