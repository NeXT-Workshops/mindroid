package org.mindroid.impl.errorhandling;

import org.mindroid.api.errorhandling.AbstractErrorHandler;

import java.util.ArrayList;

/**
 *
 * Created by torben on 08.08.2017.
 *
 * Manages Error Handlers.
 * Forwards error messages.
 *
 */
public class ErrorHandlerManager {
    private static ErrorHandlerManager ourInstance = new ErrorHandlerManager();

    public static ErrorHandlerManager getInstance() {
        return ourInstance;
    }

    public ArrayList<AbstractErrorHandler> listeners = new ArrayList<AbstractErrorHandler>();

    private ErrorHandlerManager() {
    }

    /**
     * Adds the error handler as listener.
     * If the Error Handler is already added, it won't be added again.
     * @param errorHandler - error handler to add
     */
    public void registerErrorHandler(AbstractErrorHandler errorHandler){
        if(!listeners.contains(errorHandler)) {
            listeners.add(errorHandler);
        }
    }

    public void handleError(Exception e, Class source, String msg){
        for(AbstractErrorHandler listener : listeners){
            listener.handleError(e,source,msg);
        }
    }

}
