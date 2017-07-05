package org.mindroid.android.app.acitivites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.mindroid.android.app.R;

import org.mindroid.common.messages.NetworkPortConfig;



public class SettingsActivity extends AppCompatActivity {

    public static SharedPreferences connectionProperties;

    private Button btn_saveSettings;

    public static EditText txtEdit_EV3IP;
    public static EditText txtEdit_ServerIP;

    public static EditText txtEdit_EV3TCPPort;
    public static EditText txtEdit_ServerTCPPort;

    public static final String DEFAULT_EV3_IP = "192.168.0.240";
    public static final String DEFAULT_SERVER_IP = "192.168.0.111";
    public static final String DEFAULT_EV3_TCP_PORT = ""+NetworkPortConfig.BRICK_PORT;
    public static final String DEFAULT_SERVER_TCP_PORT = ""+NetworkPortConfig.SERVER_PORT;


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
        txtEdit_EV3IP = (EditText) findViewById(R.id.txtEdit_EV3IP);
        txtEdit_EV3TCPPort = (EditText) findViewById(R.id.txtEdit_EV3TCPPort);

        txtEdit_ServerIP = (EditText) findViewById(R.id.txtEdit_ServerIP);
        txtEdit_ServerTCPPort = (EditText) findViewById(R.id.txtEdit_ServerTCPPort);

        /** Set ports **/
        txtEdit_EV3IP.setText(DEFAULT_EV3_IP);
        txtEdit_EV3TCPPort.setText(DEFAULT_EV3_TCP_PORT);

        txtEdit_ServerIP.setText(DEFAULT_SERVER_IP);
        txtEdit_ServerTCPPort.setText(DEFAULT_SERVER_TCP_PORT);

        connectionProperties = MainActivity.connectionProperties;

        if(connectionProperties != null) {
            //Set Textfields (ipEV3,portEV3,ipServer,portServer)
            String savedVal = connectionProperties.getString(KEY_EV3_IP, DEFAULT_EV3_IP);
            txtEdit_EV3IP.setText((savedVal.isEmpty()) ? DEFAULT_EV3_IP : savedVal);

            savedVal = connectionProperties.getString(KEY_EV3_TCP_PORT, DEFAULT_EV3_TCP_PORT);
            txtEdit_EV3TCPPort.setText((savedVal.isEmpty()) ? DEFAULT_EV3_TCP_PORT : savedVal);

            savedVal = connectionProperties.getString(KEY_SERVER_IP, DEFAULT_SERVER_IP);
            txtEdit_ServerIP.setText((savedVal.isEmpty()) ? DEFAULT_SERVER_IP : savedVal);

            savedVal = connectionProperties.getString(KEY_SERVER_TCP_PORT, DEFAULT_SERVER_TCP_PORT);
            txtEdit_ServerTCPPort.setText((savedVal.isEmpty()) ? DEFAULT_SERVER_TCP_PORT : savedVal);
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
        e.putString(KEY_EV3_IP,txtEdit_EV3IP.getText().toString());
        //e.putString(KEY_EV3_TCP_PORT,txtEdit_EV3TCPPort.getText().toString()); Not changeable by the user. Not neccessary to save.

        /** Data to connect to Server **/
        e.putString(KEY_SERVER_IP,txtEdit_ServerIP.getText().toString());
        //e.putString(KEY_SERVER_TCP_PORT,txtEdit_ServerTCPPort.getText().toString()); Not changeable by the user. Not neccessary to save.

        e.commit();

    }

}
