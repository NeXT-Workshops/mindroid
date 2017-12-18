package org.mindroid.android.app.fragments.log;


import android.provider.Settings;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

public class GlobalLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    private static LogHandler logHandler = new LogHandler();
    public static ArrayList<LogRecord> logs = LogHandler.logs;

    static public void setup() {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        logger.setLevel(Level.INFO);

        //Register private LogHandler
        registerLogHandler(GlobalLogger.logHandler);

        /*
        try {
            fileTxt = new FileHandler("Logging.txt");

            // create a TXT formatter
            formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);
        } catch (IOException e) {
            ErrorHandlerManager.getInstance().handleError(e,GlobalLogger.class,e.getMessage());
        }
        */
    }

    public static void registerLogHandler(Handler logHandler){
        Logger.getLogger(HomeFragment.class.getName()).addHandler(logHandler);
        Logger.getLogger(Robot.class.getName()).addHandler(logHandler);

        /*while(LogManager.getLogManager().getLoggerNames().hasMoreElements()){
            Logger logger = LogManager.getLogManager().getLogger(LogManager.getLogManager().getLoggerNames().nextElement());
            logger.addHandler(logHandler);
        }*/
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
