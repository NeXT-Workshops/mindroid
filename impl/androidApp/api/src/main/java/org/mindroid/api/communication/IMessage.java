package org.mindroid.api.communication;

/**
 * Created by torben on 04.04.2017.
 */
public interface IMessage {

    String getDestination();

    String getSource();

    String getMessage();
}
