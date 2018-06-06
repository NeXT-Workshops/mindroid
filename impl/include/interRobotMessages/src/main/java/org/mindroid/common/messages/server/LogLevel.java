package org.mindroid.common.messages.server;


/**
 * Created by Felicia Ruppel on 01.05.17.
 */
public enum LogLevel {
    DEBUG(MessageType.DEBUG),
    INFO(MessageType.INFO),
    WARN(MessageType.WARN),
    ERROR(MessageType.ERROR),
    FATAL(MessageType.FATAL);

    private final MessageType messageType;
    LogLevel(MessageType messageType){
        this.messageType = messageType;
    }
    public MessageType getMessageType() {
        return messageType;
    }

}
