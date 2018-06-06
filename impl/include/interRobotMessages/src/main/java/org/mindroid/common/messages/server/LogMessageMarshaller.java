package org.mindroid.common.messages.server;

import org.json.JSONObject;

public class LogMessageMarshaller {
    private static final String KEY_ROBOT_ID = "Robot ID";
    private static final String KEY_LOG_MSG = "Log";

    public String serialize(String robotID, String log) {
        final JSONObject serializedLogMessage = new JSONObject();

        serializedLogMessage.put(KEY_ROBOT_ID, robotID);
        serializedLogMessage.put(KEY_LOG_MSG, log);

        return serializedLogMessage.toString(2);
    }

    public MindroidLogMessage deserialize(final String serializedLogMessage){
        final JSONObject jsonObject = new JSONObject(serializedLogMessage);

        final MindroidLogMessage message = new MindroidLogMessage(
                deserializeRobotId(jsonObject.get(KEY_ROBOT_ID)),
                deserializeLog(jsonObject.get(KEY_LOG_MSG)));
        return message;
    }




    private RobotId deserializeRobotId(final Object source) {
        if (!(source instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + source.getClass());

        return new RobotId(String.class.cast(source));
    }

    private String deserializeLog(final Object source) {
        if (!(source instanceof String))
            throw new IllegalArgumentException("Expected String, but got " + source.getClass());

        return String.class.cast(source);
    }
}
