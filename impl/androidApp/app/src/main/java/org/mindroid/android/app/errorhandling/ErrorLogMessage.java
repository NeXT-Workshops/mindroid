package org.mindroid.android.app.errorhandling;

/**
 * Created by torben on 09.08.2017.
 */
public class ErrorLogMessage {

    private Class source;
    private Exception exception;
    private String message;
    private long time;

    public ErrorLogMessage(Class source, Exception exception, String message) {
        this.source = source;
        this.exception = exception;
        this.message = message;
        this.time = System.currentTimeMillis();
    }

    public Class getSource() {
        return source;
    }

    public Exception getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeInMillis() {
        return time;
    }

    @Override
    public String toString() {
        return "ErrorLogMessage{" +
                "source=" + source +
                ", exception=" + exception +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
