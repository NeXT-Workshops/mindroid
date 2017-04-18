package org.mindroid.android.app.acitivites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.mindroid.android.app.R;





public class SettingsActivity extends AppCompatActivity {

    public static SharedPreferences connectionProperties;

    private Button btn_saveSettings;

    public static EditText txt_input_robotID;
    public static EditText txt_input_groupID;

    public static EditText txt_input_EV3IP;
    public static EditText txt_input_ServerIP;

    public static EditText txt_input_EV3TCPPort;
    public static EditText txt_input_ServerTCPPort;

    public static final String DEFAULT_EV3_IP = "192.168.0.240";
    public static final String DEFAULT_SERVER_IP = "192.168.0.111";
    public static final String DEFAULT_EV3_TCP_PORT = ""+33690; //TODO before: NetworkPortConfig.BRICK_PORT
    public static final String DEFAULT_SERVER_TCP_PORT = ""+33044;//TODO before: NetworkPortConfig.SERVER_PORT , but creates dependency with EV3Messages
    public static final String DEFAULT_ROBOT_ID = "No RobotID set";
    public static final String DEFAULT_GROUP_ID = "No groupID set";

    public static final String KEY_ROBOT_ID = "ROBOT_ID";
    public static final String KEY_GROUP_ID = "GROUP_ID";
    public static final String KEY_EV3_IP = "KEY_EV3_IP";
    public static final String KEY_EV3_TCP_PORT = "KEY_EV3_TCP_PORT";
    public static final String KEY_SERVER_IP = "KEY_SERVER_IP";
    public static final String KEY_SERVER_TCP_PORT = "KEY_SERVER_TCP_PORT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /** init button **/
        btn_saveSettings = (Button) findViewById(R.id.btn_saveSettings);

        /** INIT TextEdit fields **/
        txt_input_robotID = (EditText) findViewById(R.id.txt_input_robotID);
        txt_input_groupID = (EditText) findViewById(R.id.txt_input_groupID);

        txt_input_EV3IP = (EditText) findViewById(R.id.txt_input_ev3IP);
        txt_input_EV3TCPPort = (EditText) findViewById(R.id.txt_input_EV3TCPPort);

        txt_input_ServerIP = (EditText) findViewById(R.id.txt_input_ServerIP);
        txt_input_ServerTCPPort = (EditText) findViewById(R.id.txt_input_ServerTCPPort);

        /** Set ports **/
        txt_input_EV3IP.setText(DEFAULT_EV3_IP);
        txt_input_EV3TCPPort.setText(DEFAULT_EV3_TCP_PORT);

        txt_input_ServerIP.setText(DEFAULT_SERVER_IP);
        txt_input_ServerTCPPort.setText(DEFAULT_SERVER_TCP_PORT);

        connectionProperties = MainActivity.connectionProperties;

        if(connectionProperties != null) {
            //Set Textfields (ipEV3,portEV3,ipServer,portServer)
            String savedVal = connectionProperties.getString(KEY_EV3_IP, DEFAULT_EV3_IP);
            txt_input_EV3IP.setText((savedVal.isEmpty()) ? DEFAULT_EV3_IP : savedVal);

            savedVal = connectionProperties.getString(KEY_EV3_TCP_PORT, DEFAULT_EV3_TCP_PORT);
            txt_input_EV3TCPPort.setText((savedVal.isEmpty()) ? DEFAULT_EV3_TCP_PORT : savedVal);

            savedVal = connectionProperties.getString(KEY_SERVER_IP, DEFAULT_SERVER_IP);
            txt_input_ServerIP.setText((savedVal.isEmpty()) ? DEFAULT_SERVER_IP : savedVal);

            savedVal = connectionProperties.getString(KEY_SERVER_TCP_PORT, DEFAULT_SERVER_TCP_PORT);
            txt_input_ServerTCPPort.setText((savedVal.isEmpty()) ? DEFAULT_SERVER_TCP_PORT : savedVal);

            savedVal = connectionProperties.getString(KEY_ROBOT_ID, DEFAULT_ROBOT_ID);
            txt_input_robotID.setText((savedVal.isEmpty()) ? DEFAULT_ROBOT_ID : savedVal);

            savedVal = connectionProperties.getString(KEY_GROUP_ID, DEFAULT_GROUP_ID);
            txt_input_groupID.setText((savedVal.isEmpty()) ? DEFAULT_GROUP_ID : savedVal);
        }

        btn_saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();

                Intent nexScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nexScreen);
            }
        });
    }

    private void saveSettings(){
        SharedPreferences.Editor e = connectionProperties.edit();
        e.clear();

        /** Data to connect to EV3 Brick **/
        e.putString(KEY_EV3_IP,txt_input_EV3IP.getText().toString());
        //e.putString(KEY_EV3_TCP_PORT,txtEdit_EV3TCPPort.getText().toString()); Not changeable by the user. Not neccessary to save.

        /** Data to connect to Server **/
        e.putString(KEY_SERVER_IP,txt_input_ServerIP.getText().toString());
        //e.putString(KEY_SERVER_TCP_PORT,txtEdit_ServerTCPPort.getText().toString()); Not changeable by the user. Not neccessary to save.

        /** Data to connect to EV3 Brick **/
        e.putString(KEY_ROBOT_ID,txt_input_robotID.getText().toString());
        //e.putString(KEY_EV3_TCP_PORT,txtEdit_EV3TCPPort.getText().toString()); Not changeable by the user. Not neccessary to save.

        /** Data to connect to Server **/
        e.putString(KEY_GROUP_ID,txt_input_groupID.getText().toString());
        //e.putString(KEY_SERVER_TCP_PORT,txtEdit_ServerTCPPort.getText().toString()); Not changeable by the user. Not neccessary to save.


        e.commit();

    }

}
