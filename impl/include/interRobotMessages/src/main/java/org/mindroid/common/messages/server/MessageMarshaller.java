package org.mindroid.common.messages.server;

import org.json.JSONObject;

/**
 * @author Roland Kluge - Initial implementation
 */
public class MessageMarshaller {

    private static final String KEY_SOURCE = "source";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TYPE = "type";
    private static final String VALUE_LOGMESSAGE_TYPE = "LogMessageType";
    private static final String KEY_RUNTIME_ID = "runtimeID";

    public String serialize(final MindroidMessage logMessage) {
        final JSONObject serializedMessage = new JSONObject();
        serializedMessage.put(KEY_TYPE, VALUE_LOGMESSAGE_TYPE);
        serializedMessage.put(KEY_SOURCE, serialize(logMessage.getSource()));
        serializedMessage.put(KEY_DESTINATION, serialize(logMessage.getDestination()));
        serializedMessage.put(KEY_CONTENT, logMessage.getContent());
        serializedMessage.put(KEY_TYPE, serialize(logMessage.getMessageType()));
        serializedMessage.put(KEY_RUNTIME_ID, serialize(logMessage.getRuntimeID()));
        return serializedMessage.toString(2);
    }

    public MindroidMessage deserializeMessage(final String serializedLogMessage) throws IllegalArgumentException {
        final JSONObject jsonObject = new JSONObject(serializedLogMessage);

        final MindroidMessage message = new MindroidMessage(
                deserializeRobotId(jsonObject.get(KEY_SOURCE)),
                deserializeDestination(jsonObject.get(KEY_DESTINATION)),
                deserializeLogLevel(jsonObject.get(KEY_TYPE)),
                deserializeContent(jsonObject.get(KEY_CONTENT)),
                deserializeRuntimeID(jsonObject.get(KEY_RUNTIME_ID)));
        return message;
    }

    private int deserializeRuntimeID(final Object runtimeID) {
        if (!(runtimeID instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + runtimeID.getClass());
        return Integer.valueOf((String) runtimeID);
    }

    private String deserializeContent(final Object content) {
        if (!(content instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + content.getClass());

        return String.class.cast(content);
    }

    private MessageType deserializeLogLevel(final Object logLevel) {
        if (!(logLevel instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + logLevel.getClass());

        return MessageType.valueOf(String.class.cast(logLevel));
    }

    private RobotId deserializeRobotId(final Object source) {
        if (!(source instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + source.getClass());

        return new RobotId(String.class.cast(source));
    }

    private Destination deserializeDestination(final Object source) {
        if (!(source instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + source.getClass());

        return new Destination(String.class.cast(source));
    }

    private String serialize(final MessageType messageType) {
        return messageType.toString();
    }

    private String serialize(final RobotId source) {
        return source.getValue();
    }

    private String serialize(final Destination destination) {
        return destination.getValue();
    }

    private String serialize(final int implementationID){
        return String.valueOf(implementationID);
    }


}
