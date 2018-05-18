package org.mindroid.android.app.fragments.log;


import android.os.Environment;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.logging.APILoggerManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.*;

/**
 * Receives Logs from API and also contains logs from the APP.
 * Opens a ServerSocket {@link LogServer} so its possible to access the log from the outside.
 *
 */
public class GlobalLogger {

    private final LogHandler LOG_HANDLER = new LogHandler();
    private final String GLOBAL_LOGGER_NAME =".GlobalLogger";

    private final File FILE_SD_DIRECTORY = Environment.getExternalStorageDirectory(); // getDataDirectory  ## getExternalStorageDirectory
    private final String PATH_TO_LOGFILE = FILE_SD_DIRECTORY.getPath().concat("/Mindroid/Log/");
    private final String NAME_LOGFILE = "Log.txt";

    private final HashSet<Logger> registeredLogger = new HashSet<Logger>();

    private static final GlobalLogger instance = new GlobalLogger();

    public static GlobalLogger getInstance(){ return instance; }

    private GlobalLogger(){
        registerLogger(APILoggerManager.getInstance().getLogger());
        LogServer serverLog = new LogServer(this);
        serverLog.openServer();
        loadLog();
    }



    /**
     * Saves the current Log into the LogFile given by the path {@link #PATH_TO_LOGFILE} and filename {@link #NAME_LOGFILE}.
     * Appends the Log to the content of the File.
     *
     * calls method {@link #getLogFile()}
     */
    public void loadLog(){
        LOG_HANDLER.publish(createLog(Level.INFO,"loadLog(): loading Log"));
        File logfile = getLogFile();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(logfile));
            LOG_HANDLER.logs = (List<LogRecord>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            LOG_HANDLER.publish(createLog(Level.INFO,"loadLog(): Couldn't load log: "+e.getMessage()));
        }
    }

    /**
     * Saves the current Log into the LogFile given by the path {@link #PATH_TO_LOGFILE} and filename {@link #NAME_LOGFILE}.
     * Appends the Log to the content of the File.
     *
     * calls method {@link #getLogFile()} }
     */
    public boolean saveLog(){
        LOG_HANDLER.publish(createLog(Level.INFO,"saveLog(): Saving Log"));
        File logfile = getLogFile();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(logfile));
            oos.writeObject(LOG_HANDLER.logs);
            oos.flush();
            oos.close();
            return true;
        } catch (IOException e) {
            LOG_HANDLER.publish(createLog(Level.WARNING, "saveLog(): Exception saving log: "+e+" -- "+e.getMessage()+ " -> "+e.getLocalizedMessage()));
            return false;
        }
    }

    /**
     * Deletes the Log File and clears the log cache.
     */
    public boolean deleteLog(){
        LOG_HANDLER.logs.clear();

        if(getLogFile().delete()){
            LOG_HANDLER.publish(createLog(Level.INFO, "Log deleted!"));
            return true;
        }else {
            LOG_HANDLER.publish(createLog(Level.INFO, "Deleting logfile failed!"));
            return false;
        }
    }

    /**
     * Returns the Log file if it exists, or Creates the logfile given by the path {@link #PATH_TO_LOGFILE} and filename {@link #NAME_LOGFILE} if not.
     * @return the created logfile
     */
    private File getLogFile(){
        File dir = createDir();
        File logfile = new File(dir.getPath().concat(NAME_LOGFILE));//PATH /storage/emulated/0/Mindroid/Log/Log.txt
        if(!logfile.exists()){
            try {
                boolean result = logfile.createNewFile();
                if(!result){
                    Exception e = new Exception("Could not create Logfile");
                    ErrorHandlerManager.getInstance().handleError(e,GlobalLogger.class,e.getMessage());
                }
            } catch (IOException e) {
                LOG_HANDLER.publish(createLog(Level.WARNING, "getLogFile(): Exception getting Log File: "+e.getMessage()));
            }
        }
        return logfile;
    }

    private File createDir() {
        File dir = new File(PATH_TO_LOGFILE);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                Exception e = new Exception("Could not create dir for Logfile. Missing Permissions? Try to activate manually");
                ErrorHandlerManager.getInstance().handleError(e,GlobalLogger.class,e.getMessage());
            }
        }
        return dir;
    }

    /**
     * All used Loggers need to be registered to the given logHandler.
     *
     * @param logger The Logger to register
     */
    public void registerLogger(Logger logger){
        if(!registeredLogger.contains(logger)) {
            logger.addHandler(LOG_HANDLER);
            LOG_HANDLER.logs.add(createRegistrationLog(logger));
            registeredLogger.add(logger);
        }
    }

    private LogRecord createRegistrationLog(Logger logger) {
        return createLog(Level.CONFIG,"Registered Logger: "+logger.getName());
    }

    /**
     * Creates a Log Record of the GlobalLogger
     * @param lvl - loglevel
     * @param msg - msg
     * @return LogRecord of the GlobalLogger
     */
    private LogRecord createLog(Level lvl, String msg){
        LogRecord regLog = new LogRecord(lvl,msg);
        if(regLog.getLoggerName() == null) {
            regLog.setLoggerName(GLOBAL_LOGGER_NAME);
        }
        return regLog;
    }

    /**
     * Returns the whole log in String format.
     * @return log
     */
    public String getLog(){
        StringBuffer sb = new StringBuffer();
        for (LogRecord log : LOG_HANDLER.logs) {
            sb.append(logRecordToString(log)).append("\r\n");
        }

        return sb.toString();
    }

    public List<LogRecord> getLogs() {
        return LOG_HANDLER.logs;
    }

    private String logRecordToString(LogRecord log){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY - HH:mm:ss:SS", Locale.GERMAN);
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(fixedLengthString(log.getLevel().toString(),7)).append("/").append(sdf.format(log.getMillis())).append("]");
        sb.append(fixedLengthString(log.getLoggerName(),50)).append(" - ");
        sb.append(log.getMessage());
        return sb.toString();

    }

    /**
     * Returns a string of the specific length
     * @param string string to return in the specific length
     * @param length fixed length
     * @return string of the given length
     */
    private String fixedLengthString(String string, int length) {
        String str = String.format("%1$"+length+ "s", string);
        return str.substring(str.length()-length,str.length());
    }

    /**
     * Keeps track of the logs.
     */
    private static class LogHandler extends Handler{
        static List<LogRecord> logs = new SyncedArrayList<LogRecord>(new ArrayList<LogRecord>());

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
