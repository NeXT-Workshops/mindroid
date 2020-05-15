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
    private static final String KEY_SESSION_COUNT = "sessionCount";

    private static final String VALUE_LOGMESSAGE_TYPE = "LogMessageType";

    public String serialize(final MindroidMessage msg) {
        final JSONObject serializedMessage = new JSONObject();
        //serializedMessage.put(KEY_TYPE, VALUE_LOGMESSAGE_TYPE);
        serializedMessage.put(KEY_SOURCE, serialize(msg.getSource()));
        serializedMessage.put(KEY_DESTINATION, serialize(msg.getDestination()));
        serializedMessage.put(KEY_CONTENT, msg.getContent());
        serializedMessage.put(KEY_TYPE, serialize(msg.getMessageType()));
        serializedMessage.put(KEY_SESSION_COUNT, serialize(msg.getSessionRobotCount()));
        return serializedMessage.toString(2);
    }

    public MindroidMessage deserializeMessage(final String serMsg) throws IllegalArgumentException {
        final JSONObject jsonObject = new JSONObject(serMsg);

        return new MindroidMessage(
                deserializeRobotId(jsonObject.get(KEY_SOURCE)),
                deserializeRobotId(jsonObject.get(KEY_DESTINATION)), deserializeType(jsonObject.get(KEY_TYPE)),
                deserializeContent(jsonObject.get(KEY_CONTENT)),
                deserializeSessionCount(jsonObject.get(KEY_SESSION_COUNT)));
    }

    private int deserializeSessionCount(final Object sessionCount) {
        if (!(sessionCount instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + sessionCount.getClass());
        return Integer.valueOf((String) sessionCount);
    }

    private String deserializeContent(final Object content) {
        if (!(content instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + content.getClass());

        return String.class.cast(content);
    }

    private MessageType deserializeType(final Object logLevel) {
        if (!(logLevel instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + logLevel.getClass());

        return MessageType.valueOf(String.class.cast(logLevel));
    }

    private RobotId deserializeRobotId(final Object source) {
        if (!(source instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + source.getClass());

        return new RobotId(String.class.cast(source));
    }

    private String serialize(final MessageType messageType) {
        return messageType.toString();
    }

    private String serialize(final RobotId source) {
        return source.getValue();
    }

    private String serialize(final int sessionSize){
        return String.valueOf(sessionSize);
    }


}
