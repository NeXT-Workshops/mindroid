package org.mindroid.api.errorhandling;

/**
 * Created by torben on 08.08.2017.
 *
 * Handles errormessages
 */
public abstract class AbstractErrorHandler {

    public abstract void handleError(Exception e, Class source, String msg);
}
