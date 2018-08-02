package org.mindroid.android.app.robodancer;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import org.mindroid.android.app.R;
import org.mindroid.android.app.acitivites.MainActivity;
import org.mindroid.android.app.fragments.home.ConnectionProgressDialogFragment;
import org.mindroid.android.app.fragments.myrobot.HardwareMapping;
import org.mindroid.android.app.serviceloader.ImplementationService;
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

    private static final String ADMIN_MODE_UNLOCKED = "ADMIN_MODE_UNLOCKED";

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

    private int maxShownLog = 50;

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
    private SharedPreferences adminProperties;

    private boolean adminModeUnlocked;

    private boolean isInitialized = false;
    private boolean isSimulationEnabled = false;

    private String androidId = "Android ID not set";

    private static SettingsProvider ourInstance = new SettingsProvider();
    private Bundle robotConfigBundle;
    private String selectedProgramSet = ImplementationService.getInstance().getDefaultSet();

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

    private static final List<String> DEFAULT_GROUP_NAMES = Arrays.asList(
            "Red", "Green", "Blue", "Yellow", "Pink",
            "Purple", "Orange"
    );


    private SettingsProvider() {

    }

    /**
     * Initializes the SettingsProvider, should be executed once and before using it!
     * @param res App resources
     * @param connectionProperties Shared Preferences containing Connection-Properties
     * @param portConfigProperties Shared Preferences containing the Port Configuration
     * @param adminProperties
     */
    public void initialize(Resources res, SharedPreferences connectionProperties, SharedPreferences portConfigProperties, SharedPreferences adminProperties){
        if(!isInitialized) {
            this.resources = res;
            this.connectionProperties = connectionProperties;
            this.portConfigProperties = portConfigProperties;
            this.adminProperties = adminProperties;

            //Initial loading
            loadConnectionProperties();
            loadRobotPortConfiguration();
            loadAdminModeProperties();

            //Initialization complete
            this.isInitialized = true;
        }
    }

    private void loadAdminModeProperties() {
        adminModeUnlocked = adminProperties.getBoolean("adminModeUnlocked", false);
    }

    public void setAndroidId(final String androidId) {
        this.androidId = androidId;
    }

    public String generateUniqueRobotName() {
        final int seed = androidId.hashCode();
        final String selectedName = DEFAULT_NAMES.get(new Random(seed).nextInt(DEFAULT_NAMES.size()));
        return selectedName;
    }

    public String generateUniqueGroupName() {
        final int seed = androidId.hashCode();
        final String selectedName = DEFAULT_GROUP_NAMES.get(new Random(seed).nextInt(DEFAULT_GROUP_NAMES.size()));
        return selectedName;
    }


    public void loadConnectionProperties(){
        if (connectionProperties != null) {
            String savedVal;
            if(!MainActivity.robot.isMessengerConnected()) {
                savedVal = connectionProperties.getString(resources.getString(R.string.KEY_ROBOT_ID), SettingsProvider.getInstance().generateUniqueRobotName());
                SettingsProvider.getInstance().robotID = savedVal;

                savedVal = connectionProperties.getString(resources.getString(R.string.KEY_GROUP_ID), generateUniqueGroupName());
                SettingsProvider.getInstance().groupID = savedVal;
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
     * Config is stored in the SharedPreferences, if element is not there it will be set to notDefined ("-")
     * Therefore also sets the Default setup at the first start of the app
     */
    public void loadRobotPortConfiguration() {
        if(portConfigProperties != null) {
            //--SENSORS--
            //Sensor - S1
            sensorS1 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S1), DefaultRobotPortConfig.SENSOR_S1);
            sensorModeS1 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S1), DefaultRobotPortConfig.SENSORMODE_S1);
            //Sensor - S2
            sensorS2 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S2), DefaultRobotPortConfig.SENSOR_S2);
            sensorModeS2 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S2), DefaultRobotPortConfig.SENSORMODE_S2);
            //Sensor - S3
            sensorS3 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S3), DefaultRobotPortConfig.SENSOR_S3);
            sensorModeS3 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S3), DefaultRobotPortConfig.SENSORMODE_S3);
            //Sensor - S4
            sensorS4 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSOR_S4), DefaultRobotPortConfig.SENSOR_S4);
            sensorModeS4 = portConfigProperties.getString(resources.getString(R.string.KEY_SENSORMODE_S4), DefaultRobotPortConfig.SENSORMODE_S4);

            //--MOTORS--
            motorA = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_A), DefaultRobotPortConfig.MOTOR_A);
            motorB = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_B), DefaultRobotPortConfig.MOTOR_B);
            motorC = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_C), DefaultRobotPortConfig.MOTOR_C);
            motorD = portConfigProperties.getString(resources.getString(R.string.KEY_MOTOR_D), DefaultRobotPortConfig.MOTOR_D);

        }else{
            //TODO errorhandling
        }
    }

    public Bundle getRobotConfigBundle() {
        loadRobotPortConfiguration();
        Bundle bundle = new Bundle();
        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P1,getSensorS1() == null ? null : getSensorS1().getName()+":"+getSensorModeS1());
        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P2,getSensorS2() == null ? null : getSensorS2().getName()+":"+getSensorModeS2());
        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P3,getSensorS3() == null ? null : getSensorS3().getName()+":"+getSensorModeS3());
        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P4,getSensorS4() == null ? null : getSensorS4().getName()+":"+getSensorModeS4());

        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_MOT_A,getMotorA() == null ? null : getMotorA().getName());
        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_MOT_B,getMotorB() == null ? null : getMotorB().getName());
        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_MOT_C,getMotorC() == null ? null : getMotorC().getName());
        bundle.putString(ConnectionProgressDialogFragment.KEY_PARAM_MOT_D,getMotorD() == null ? null : getMotorD().getName());

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

    public int getMaxShownLog(){
        return maxShownLog;
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

    public boolean isSimulationEnabled() {
        return isSimulationEnabled;
    }

    public void setSimulationEnabled(boolean simulationEnabled) {
        isSimulationEnabled = simulationEnabled;
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


    public boolean isAdminModeUnlocked() {
        return adminModeUnlocked;
    }

    public void setAdminModeUnlocked(boolean adminModeUnlocked) {
        SharedPreferences.Editor e = adminProperties.edit();
        e.putBoolean("adminModeUnlocked",adminModeUnlocked);
        e.commit();
        this.adminModeUnlocked = adminModeUnlocked;
    }

    public String getSelectedProgramSet() {
        return selectedProgramSet;
    }

    public void setSelectedProgramSet(String selectedProgramSet) {
        this.selectedProgramSet = selectedProgramSet;
    }
}
