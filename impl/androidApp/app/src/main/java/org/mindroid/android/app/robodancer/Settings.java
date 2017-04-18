package org.mindroid.android.app.robodancer;

import org.mindroid.android.app.acitivites.SettingsActivity;

/**
 * Created by Torbe on 18.04.2017.
 */
public class Settings {


    public String robotID = "ROBOT_ID";
    public String groupID = "GROUP_ID";
    public String ev3IP = SettingsActivity.DEFAULT_EV3_IP;
    public int ev3TCPPort = 0;
    public String serverIP = SettingsActivity.DEFAULT_SERVER_IP;
    public int serverTCPPort = 0;

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
