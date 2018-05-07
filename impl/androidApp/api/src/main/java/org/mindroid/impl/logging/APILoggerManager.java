package org.mindroid.impl.logging;


import java.util.HashSet;
import java.util.logging.Logger;

public class APILoggerManager {

    private static APILoggerManager ourInstance = new APILoggerManager();

    public static APILoggerManager getInstance() {
        return ourInstance;
    }

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    private final HashSet<Logger> registeredLoggers = new HashSet<Logger>();

    private APILoggerManager() {

    }

    public void registerLogger(Logger logger){
        if(registeredLoggers.contains(logger)) {
            //already registerd!
            return;
        }
        //Register Logger to handler and into set
        for (int i = 0; i < LOGGER.getHandlers().length; i++) {
            logger.addHandler(LOGGER.getHandlers()[i]);
        }
        registeredLoggers.add(logger);

    }

    public Logger getLogger(){
        return LOGGER;
    }
}
