package org.mindroid.android.app.robodancer;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    private String androidId = "Android ID not set";

    private static Settings ourInstance = new Settings();

    private static final List<String> DEFAULT_NAMES = Arrays.asList(
        "Mia", "Emma", "Sofia", "Hannah", "Emilia",
        "Anne", "Marie", "Mila", "Lina", "Lea",
        "Amelie", "Luisa", "Johanna", "Emily", "Clara",//
        "Ben", "Paul", "Jonas", "Elias", "Leon",
        "Finn", "Noah", "Louis", "Lukas", "Felix",
        "Max", "Henry", "Oskar", "Emil", "Liam"
    );

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {

    }

    public void setAndroidId(final String androidId) {
        this.androidId = androidId;
    }

    public String generateUniqueRobotName() {
        // getResources().getString(R.string.DEFAULT_ROBOT_ID);
        final int seed = androidId.hashCode();
        final String selectedName = DEFAULT_NAMES.get(new Random(seed).nextInt(DEFAULT_NAMES.size()));
        return selectedName;
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
