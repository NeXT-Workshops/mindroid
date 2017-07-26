package org.mindroid.android.app.robodancer;



/**
 * Created by Torben on 18.04.2017.
 */
public class Settings {


    public String robotID = "ROBOT_ID";
    public String groupID = "GROUP_ID";
    public String ev3IP = "-";
    public int ev3TCPPort = -1;
    public String serverIP = "-";
    public int serverTCPPort = -1;
    public int robotServerPort = -1;

    public String selectedStatemachineID = "";



    private static Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {

    }

    @Override
    public String toString() {
        return "Settings{" +
                "robotID='" + robotID + '\'' +
                ", groupID='" + groupID + '\'' +
                ", ev3IP='" + ev3IP + '\'' +
                ", ev3TCPPort=" + ev3TCPPort +
                ", serverIP='" + serverIP + '\'' +
                ", serverTCPPort=" + serverTCPPort +
                '}';
    }
}
