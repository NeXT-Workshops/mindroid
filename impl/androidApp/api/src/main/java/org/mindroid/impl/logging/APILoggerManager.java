package org.mindroid.impl.logging;


import java.util.logging.Logger;

public class APILoggerManager {

    private static APILoggerManager ourInstance = new APILoggerManager();

    public static APILoggerManager getInstance() {
        return ourInstance;
    }

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    private APILoggerManager() {

    }

    public void registerLogger(Logger logger){
        for (int i = 0; i < LOGGER.getHandlers().length; i++) {
            logger.addHandler(LOGGER.getHandlers()[i]);
        }
    }

    public Logger getLogger(){
        return LOGGER;
    }
}
