package org.mindroid.server.app.log;

import org.mindroid.common.messages.server.MindroidLogMessage;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.server.app.MindroidServerConsoleFrame;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;

public class LogHandler {

    private static final String DIR = "Mindroid";
    private static final String FILE_TYPE = ".txt";

    private static HashMap<String,File> fileCache = new HashMap<>();

    public static void saveToFile(MindroidLogMessage logMsg,String filename){
        File logFile = getLogFile(filename);
        writeFile(logMsg, logFile);
    }

    private static void writeFile(MindroidLogMessage logMsg, File savefile) {
        try {
            FileWriter fw = new FileWriter(savefile,true);
            fw.write(logMsg.getLog());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Couldn't Save File (unable to write file): "+e.getMessage());
        }
    }

    public static String loadLog(String filename){
        if(!isExistent(filename)){
            return getFileContent(getLogFile(filename));
        }else{
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Couldn't load Log file");
            return "";
        }
    }

    public static boolean isExistent(String filename){
        File logfile = getLogFile(filename);
        if(!getLogFile(filename).exists()){
            return false;
        }else{
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Logfile does not exist: "+logfile.getPath());
            return true;
        }
    }

    public static File getLogFile(String filename){
        if(fileCache.containsKey(filename)){
            return fileCache.get(filename);
        }
        File dir = new File(getLogFilesDirPath());
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File logFile = new File(getLogFilePath(filename));

        fileCache.put(filename,logFile);
        return logFile;
    }

    private static String getFileContent(File file){
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            while(br.read() != -1){
                sb.append(br.readLine());
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Couldn't load String";
    }

    public static String getLogFilePath(String filename) {
        return getLogFilesDirPath().concat(filename);
    }

    private static String getLogFilesDirPath(){
        String path = System.getProperty("user.home");
        String separator = FileSystems.getDefault().getSeparator();
        path = path.concat(separator).concat(DIR).concat(separator);
        return path;
    }

    public static String getFilename(String robotID, InetSocketAddress inetSocketAddress){
        return robotID.concat("_").concat(inetSocketAddress.getAddress().hashCode()+"").concat(FILE_TYPE);
    }

}
