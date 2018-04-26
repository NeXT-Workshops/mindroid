package org.mindroid.android.app.fragments.log;


import android.os.Environment;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.logging.APILoggerManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.*;

/**
 * Receives Logs from API and also contains logs from the APP.
 * Opens a ServerSocket {@link LogServer} so its possible to access the log from the outside.
 *
 */
public class GlobalLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    private final LogHandler LOG_HANDLER = new LogHandler();
    private final String GLOBAL_LOGGER_NAME =".GlobalLogger";
    public static ArrayList<LogRecord> logs = LogHandler.logs;

    private final File FILE_SD_DIRECTORY = Environment.getDataDirectory(); // getDataDirectory  ## getExternalStorageDirectory
    private final String PATH_TO_LOGFILE = FILE_SD_DIRECTORY.getPath().concat("/Mindroid/Log/");
    private final String NAME_LOGFILE = "Log.txt";

    private static final GlobalLogger instance = new GlobalLogger();

    public static GlobalLogger getInstance(){ return instance; }

    private GlobalLogger(){
        registerLogger(APILoggerManager.getInstance().getLogger());
        LogServer serverLog = new LogServer(this);
        serverLog.openServer();
    }

    /**
     * Saves the current Log into the LogFile given by the path {@link #PATH_TO_LOGFILE} and filename {@link #NAME_LOGFILE}.
     * Appends the Log to the content of the File.
     *
     * calls method {@link #getLogFile()}
     */
    public void loadLog(){
        File logfile = getLogFile();
        try {
            BufferedReader br = new BufferedReader(new FileReader(logfile));
            String logline ="";
            while((logline = br.readLine()) != null){
                //LogRecord logRecord = new LogRecord();
                //TODO
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current Log into the LogFile given by the path {@link #PATH_TO_LOGFILE} and filename {@link #NAME_LOGFILE}.
     * Appends the Log to the content of the File.
     *
     * calls method {@link #getLogFile()} }
     */
    public boolean saveLog(){
        /*
            //TODO
            It is neccessary to grant access to the app manually to write on memory
            To avoid giving permisson manually on every phone try
            	adb shell pm grant com.replace.with.your.app.package android.permission.WRITE_EXTERNAL_STORAGE
                adb shell pm grant com.replace.with.your.app.package android.permission.READ_EXTERNAL_STORAGE
            using the ShellService
         */


        File logfile = getLogFile();
        System.out.println("## Saveing LOG to "+logfile.getPath());
        try {
            FileOutputStream fos = new FileOutputStream(logfile,true);

            fos.write(getLog().getBytes());
            fos.flush();
            fos.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the Log file if it exists, or Creates the logfile given by the path {@link #PATH_TO_LOGFILE} and filename {@link #NAME_LOGFILE} if not.
     * @return the created logfile
     */
    private File getLogFile(){
        File logfile = new File(PATH_TO_LOGFILE.concat(NAME_LOGFILE));//PATH /storage/emulated/0/Mindroid/Log/Log.txt
        if(!logfile.exists()){
            boolean result = logfile.mkdirs();
            if(!result){
                Exception e = new Exception("Could not create Logfile");
                ErrorHandlerManager.getInstance().handleError(e,GlobalLogger.class,e.getMessage());
            }
        }
        return logfile;
    }

    /**
     * All used Loggers need to be registered to the given logHandler.
     *
     * @param logger The Logger to register
     */
    public void registerLogger(Logger logger){
        logger.addHandler(LOG_HANDLER);
        logs.add(createRegistrationLog(logger));
    }

    private LogRecord createRegistrationLog(Logger logger) {
        LogRecord regLog = new LogRecord(Level.CONFIG,"Registered Logger: "+logger.getName());
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
        for (LogRecord log : logs) {
            sb.append(logRecordToString(log)).append("\r\n");
        }

        return sb.toString();
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
