package org.mindroid.android.app.robodancer;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import org.mindroid.android.app.R;
import org.mindroid.android.app.acitivites.MainActivity;
import org.mindroid.android.app.fragments.home.ConnectionProgressFragment;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.fragments.myrobot.HardwareMapping;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Torben on 18.04.2017.
 *
 * Singleton SettingsProvider keeps track of changes of Port-Configuration Attributes, Robot Attributes, and Connection Attributes.
 * Classes of the App use this class to get the current Attributes.
 * Classes that change Attributes actively will inform this class about changes so that this class always provides the current Attributes.
 *
 */
public class SettingsProvider implements ConnectionPropertiesChangedListener, RobotConfigurationChangedListener {

    /** public Robot Attributes **/
    private String robotID = "ROBOT_ID";
    private String groupID = "GROUP_ID";

    //Selected Implementation ID - Selected by Spinner in UI of the HomeFragment
    public String selectedImplementationID = "";

    /** public Connection Attributes **/
    private String ev3IP = "-";
    private int ev3TCPPort = -1;
    //MsgServerIP
    private String serverIP = "-";
    //MsgServerPort
    private int serverTCPPort = -1;

    /** public Configuration Attributes **/
    private String sensorS1 = HardwareMapping.notDefined;
    private String sensorS2 = HardwareMapping.notDefined;
    private String sensorS3 = HardwareMapping.notDefined;
    private String sensorS4 = HardwareMapping.notDefined;

    private String sensorModeS1 = HardwareMapping.notDefined;
    private String sensorModeS2 = HardwareMapping.notDefined;
    private String sensorModeS3 = HardwareMapping.notDefined;
    private String sensorModeS4 = HardwareMapping.notDefined;

    private String motorA = HardwareMapping.notDefined;
    private String motorB = HardwareMapping.notDefined;
    private String motorC = HardwareMapping.notDefined;
    private String motorD = HardwareMapping.notDefined;

    /** private Setting attributes **/
    private Resources resources;
    private SharedPreferences connectionProperties;
    private SharedPreferences portConfigProperties;
    private boolean isInitialized = false;

    private String androidId = "Android ID not set";

    private static SettingsProvider ourInstance = new SettingsProvider();
    private Bundle robotConfigBundle;

    public static SettingsProvider getInstance() {
        return ourInstance;
    }

    private static final List<String> DEFAULT_NAMES = Arrays.asList(
            "Mia", "Emma", "Sofia", "Hannah", "Emilia",
            "Anne", "Marie", "Mila", "Lina", "Lea",
            "Amelie", "Luisa", "Johanna", "Emily", "Clara",//
            "Ben", "Paul", "Jonas", "Elias", "Leon",
            "Finn", "Noah", "Louis", "Lukas", "Felix",
            "Max", "Henry", "Oskar", "Emil", "Liam"
    );


    private SettingsProvider() {

    }

    /**
     * Initializes the SettingsProvider, should be executed once and before using it!
     * @param res App resources
     * @param connectionProperties Shared Preferences containing Connection-Properties
     * @param portConfigProperties Shared Preferences containing the Port Configuration
     */
    public void initialize(Resources res, SharedPreferences connectionProperties, SharedPreferences portConfigProperties){
        if(!isInitialized) {
            this.resources = res;
            this.connectionProperties = connectionProperties;
            this.portConfigProperties = portConfigProperties;

            //Initial loading
            loadConnectionProperties();
            loadRobotPortConfiguration();

            //Initialization complete
            this.isInitialized = true;
        }
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


    public void loadConnectionProperties(){
        if (connectionProperties != null) {
            String savedVal;
            if(!MainActivity.robot.isMessengerConnected()) {
                savedVal = connectionProperties.getString(resources.getString(R.string.KEY_ROBOT_ID), resources.getString(R.string.DEFAULT_ROBOT_ID));
                SettingsProvider.getInstance().robotID = ((savedVal.isEmpty()) ? SettingsProvider.getInstance().generateUniqueRobotName() : savedVal);

                savedVal = connectionProperties.getString(resources.getString(R.string.KEY_GROUP_ID), resources.getString(R.string.DEFAULT_GROUP_ID));
                SettingsProvider.getInstance().groupID = ((savedVal.isEmpty()) ? resources.getString(R.string.KEY_GROUP_ID) : savedVal);
            }

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_EV3_IP), resources.getString(R.string.DEFAULT_EV3_BRICK_IP));
            SettingsProvider.getInstance().ev3IP = ( (savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_EV3_BRICK_IP) : savedVal);

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_EV3_TCP_PORT),resources.getString(R.string.DEFAULT_EV3_BRICK_PORT));
            SettingsProvider.getInstance().ev3TCPPort = (Integer.parseInt((savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_EV3_BRICK_PORT) : savedVal));

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_SERVER_IP), resources.getString(R.string.DEFAULT_MSG_SERVER_IP));
            SettingsProvider.getInstance().serverIP = ( (savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_MSG_SERVER_IP) : savedVal);

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_SERVER_TCP_PORT),resources.getString(R.string.DEFAULT_MSG_SERVER_PORT));
            SettingsProvider.getInstance().serverTCPPort = (Integer.parseInt((savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_MSG_SERVER_PORT) : savedVal));

        }else{
            /*if(getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).showErrorDialog("Couldn't load Connection Properties!","Connection Properties are null! Goto SettingsProvider and save thema again!");
            }*/
            //TODO errorhandling
        }
    }

    /**
     * Loads the Hardware Port Configuration of the Robot and sets it.
     */
    public void loadRobotPortConfiguration() {
        if(portConfigProperties != null) {
            //--SENSORS--
            //Sensor - S1
            sensorS1 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S1), HardwareMapping.notDefined);
            sensorModeS1 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S1), HardwareMapping.notDefined);
            //Sensor - S2
            sensorS2 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S2), HardwareMapping.notDefined);
            sensorModeS2 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S2), HardwareMapping.notDefined);
            //Sensor - S3
            sensorS3 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S3), HardwareMapping.notDefined);
            sensorModeS3 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S3), HardwareMapping.notDefined);
            //Sensor - S4
            sensorS4 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S4), HardwareMapping.notDefined);
            sensorModeS4 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S4), HardwareMapping.notDefined);

            //--MOTORS--
            motorA = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_A), HardwareMapping.notDefined);
            motorB = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_B), HardwareMapping.notDefined);
            motorC = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_C), HardwareMapping.notDefined);
            motorD = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_D), HardwareMapping.notDefined);

        }else{
            //TODO errorhandling
        }
    }

    public Bundle getRobotConfigBundle() {
        loadRobotPortConfiguration();
        Bundle bundle = new Bundle();
        bundle.putString(ConnectionProgressFragment.KEY_PARAM_SEN_P1,getSensorS1() == null ? null : getSensorS1().getName()+":"+getSensorModeS1());
        bundle.putString(ConnectionProgressFragment.KEY_PARAM_SEN_P2,getSensorS2() == null ? null : getSensorS2().getName()+":"+getSensorModeS2());
        bundle.putString(ConnectionProgressFragment.KEY_PARAM_SEN_P3,getSensorS3() == null ? null : getSensorS3().getName()+":"+getSensorModeS3());
        bundle.putString(ConnectionProgressFragment.KEY_PARAM_SEN_P4,getSensorS4() == null ? null : getSensorS4().getName()+":"+getSensorModeS4());

        bundle.putString(ConnectionProgressFragment.KEY_PARAM_MOT_A,getMotorA() == null ? null : getMotorA().getName());
        bundle.putString(ConnectionProgressFragment.KEY_PARAM_MOT_B,getMotorB() == null ? null : getMotorB().getName());
        bundle.putString(ConnectionProgressFragment.KEY_PARAM_MOT_C,getMotorC() == null ? null : getMotorC().getName());
        bundle.putString(ConnectionProgressFragment.KEY_PARAM_MOT_D,getMotorD() == null ? null : getMotorD().getName());

        return bundle;
    }

    public String getRobotID() {
        return robotID;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getEv3IP() {
        return ev3IP;
    }

    public int getEv3TCPPort() {
        return ev3TCPPort;
    }

    public String getMsgServerIP() {
        return serverIP;
    }

    public int getMsgServerPort() {
        return serverTCPPort;
    }

    public Sensors getSensorS1() {
        return HardwareMapping.getSensorType(sensorS1);
    }

    public Sensors getSensorS2() {
        return HardwareMapping.getSensorType(sensorS2);
    }

    public Sensors getSensorS3() {
        return HardwareMapping.getSensorType(sensorS3);
    }

    public Sensors getSensorS4() {
        return HardwareMapping.getSensorType(sensorS4);
    }

    public Sensormode getSensorModeS1() {
        return HardwareMapping.getSensorMode(sensorModeS1);
    }

    public Sensormode getSensorModeS2() {
        return HardwareMapping.getSensorMode(sensorModeS2);
    }

    public Sensormode getSensorModeS3() {
        return HardwareMapping.getSensorMode(sensorModeS3);
    }

    public Sensormode getSensorModeS4() {
        return HardwareMapping.getSensorMode(sensorModeS4);
    }

    public Motors getMotorA() {
        return HardwareMapping.getMotorType(motorA);
    }

    public Motors getMotorB() {
        return HardwareMapping.getMotorType(motorB);
    }

    public Motors getMotorC() {
        return HardwareMapping.getMotorType(motorC);
    }

    public Motors getMotorD() {
        return HardwareMapping.getMotorType(motorD);
    }

    @Override
    public void onConnectionPropertiesChangedListener() {
        loadConnectionProperties();
    }

    @Override
    public void onRobotConfigurationChangedListener() {
        loadRobotPortConfiguration();
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public String toString() {
        return "SettingsProvider{" +
                "robotID='" + robotID + '\'' +
                ", groupID='" + groupID + '\'' +
                ", selectedImplementationID='" + selectedImplementationID + '\'' +
                ", ev3IP='" + ev3IP + '\'' +
                ", ev3TCPPort=" + ev3TCPPort +
                ", serverIP='" + serverIP + '\'' +
                ", serverTCPPort=" + serverTCPPort +
                ", sensorS1='" + sensorS1 + '\'' +
                ", sensorS2='" + sensorS2 + '\'' +
                ", sensorS3='" + sensorS3 + '\'' +
                ", sensorS4='" + sensorS4 + '\'' +
                ", sensorModeS1='" + sensorModeS1 + '\'' +
                ", sensorModeS2='" + sensorModeS2 + '\'' +
                ", sensorModeS3='" + sensorModeS3 + '\'' +
                ", sensorModeS4='" + sensorModeS4 + '\'' +
                ", motorA='" + motorA + '\'' +
                ", motorB='" + motorB + '\'' +
                ", motorC='" + motorC + '\'' +
                ", motorD='" + motorD + '\'' +
                '}';
    }


}
