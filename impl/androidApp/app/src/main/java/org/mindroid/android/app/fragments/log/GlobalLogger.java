package org.mindroid.android.app.fragments.log;


import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.impl.logging.APILoggerManager;

import java.util.ArrayList;
import java.util.logging.*;

public class GlobalLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    private final static LogHandler LOG_HANDLER = new LogHandler();
    private final static String GLOBAL_LOGGER_NAME =".GlobalLogger";
    public static ArrayList<LogRecord> logs = LogHandler.logs;



    static{
        //Register API Logger
        registerLogger(APILoggerManager.getInstance().getLogger());
    }


    public GlobalLogger(){

    }



    /**
     * All used Loggers need to be registered to the given logHandler.
     *
     * @param logger The Logger to register
     */
    public static void registerLogger(Logger logger){
        logger.addHandler(LOG_HANDLER);
        logs.add(createRegistrationLog(logger));
    }

    private static LogRecord createRegistrationLog(Logger logger) {
        LogRecord regLog = new LogRecord(Level.CONFIG,"Registered Logger: "+logger.getName());
        if(regLog.getLoggerName() == null) {
            regLog.setLoggerName(GLOBAL_LOGGER_NAME);
        }
        return regLog;
    }


    /**
     * Keeps track of the logs.
     */
    private static class LogHandler extends Handler{
        static ArrayList<LogRecord> logs = new ArrayList<LogRecord>();

        @Override
        public void publish(LogRecord logRecord) {
            logs.add(logRecord);
        }

        @Override
        public void flush() {
            //Nothing to do here
        }

        @Override
        public void close() throws SecurityException {
            //Nothing to do here
        }
    }
}
