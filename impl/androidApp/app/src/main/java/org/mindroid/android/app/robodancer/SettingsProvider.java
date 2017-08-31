package org.mindroid.android.app.robodancer;

import android.content.SharedPreferences;
import android.content.res.Resources;
import org.mindroid.android.app.R;
import org.mindroid.android.app.fragments.myrobot.HardwareMapping;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;

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
    public String selectedStatemachineID = "";

    /** public Connection Attributes **/
    private String ev3IP = "-";
    private int ev3TCPPort = -1;
    private String serverIP = "-";
    private int serverTCPPort = -1;
    private int robotServerPort = -1;

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

    private static SettingsProvider ourInstance = new SettingsProvider();

    public static SettingsProvider getInstance() {
        return ourInstance;
    }

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


    public void loadConnectionProperties(){
        if (connectionProperties != null) {
            String savedVal;
            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_ROBOT_ID),resources.getString(R.string.DEFAULT_ROBOT_ID));
            SettingsProvider.getInstance().robotID = ( (savedVal.isEmpty()) ? resources.getString(R.string.KEY_ROBOT_ID) : savedVal);

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_GROUP_ID),resources.getString(R.string.DEFAULT_GROUP_ID));
            SettingsProvider.getInstance().groupID = ( (savedVal.isEmpty()) ? resources.getString(R.string.KEY_GROUP_ID) : savedVal);

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_EV3_IP), resources.getString(R.string.DEFAULT_EV3_BRICK_IP));
            SettingsProvider.getInstance().ev3IP = ( (savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_EV3_BRICK_IP) : savedVal);

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_EV3_TCP_PORT),resources.getString(R.string.DEFAULT_EV3_BRICK_PORT));
            SettingsProvider.getInstance().ev3TCPPort = (Integer.parseInt((savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_EV3_BRICK_PORT) : savedVal));

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_SERVER_IP), resources.getString(R.string.DEFAULT_MSG_SERVER_IP));
            SettingsProvider.getInstance().serverIP = ( (savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_MSG_SERVER_IP) : savedVal);

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_SERVER_TCP_PORT),resources.getString(R.string.DEFAULT_MSG_SERVER_PORT));
            SettingsProvider.getInstance().serverTCPPort = (Integer.parseInt((savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_MSG_SERVER_PORT) : savedVal));

            savedVal = connectionProperties.getString(resources.getString(R.string.KEY_ROBOT_SERVER_TCP_PORT),resources.getString(R.string.DEFAULT_BRICK_MSG_SERVER_PORT));
            SettingsProvider.getInstance().robotServerPort = (Integer.parseInt((savedVal.isEmpty()) ? resources.getString(R.string.DEFAULT_BRICK_MSG_SERVER_PORT) : savedVal));

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

    public String getServerIP() {
        return serverIP;
    }

    public int getServerTCPPort() {
        return serverTCPPort;
    }

    public int getRobotServerPort() {
        return robotServerPort;
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

    public SensorMessages.SensorMode_ getSensorModeS1() {
        return HardwareMapping.getSensorMode(sensorModeS1);
    }

    public SensorMessages.SensorMode_ getSensorModeS2() {
        return HardwareMapping.getSensorMode(sensorModeS2);
    }

    public SensorMessages.SensorMode_ getSensorModeS3() {
        return HardwareMapping.getSensorMode(sensorModeS3);
    }

    public SensorMessages.SensorMode_ getSensorModeS4() {
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
                ", selectedStatemachineID='" + selectedStatemachineID + '\'' +
                ", ev3IP='" + ev3IP + '\'' +
                ", ev3TCPPort=" + ev3TCPPort +
                ", serverIP='" + serverIP + '\'' +
                ", serverTCPPort=" + serverTCPPort +
                ", robotServerPort=" + robotServerPort +
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
