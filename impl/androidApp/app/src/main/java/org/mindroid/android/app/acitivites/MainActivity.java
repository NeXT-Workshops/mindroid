package org.mindroid.android.app.acitivites;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.google.android.gms.common.api.GoogleApiClient;

import org.mindroid.android.app.R;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.android.app.robodancer.Settings;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;


/**
 * How to Connect to Brick.
 * PHONE WITH CYANOGENMOD:
 *  1. CYANOGENMOD PHONE
 *  2. CONNECT PHONE TO BRICK VIA USB
 *  3. ACTIVATE USB TETHERING
 *  3. ON BRICK: Go to PAN - USB Einstellungen. Set all properties to automated (IP,Subnetmask,etc)
 *  4. GO back and brick will now save the settings.
 *  5. IP should be displayed on the Brick. (IF NOT, IT WILL NOT WORK!)
 *  6. start Robot App.
 *  7. App: Go to Settings and enter the IP address of the brick.
 *  8. Save settings.
 *  9. Click on connect-to-brick button
 *
 * PHONE WITH ANDROID:
 *  1. Android phone
 *  2. Activate USB Debugging (Notwendig, wenn USB-Tethering nicht aktiviert werden kann!), Activate USB Tethering.
 *  3. Connect Phone to brick via usb.
 *  4. ON BRICK: Go to PAN - USB Einstellungen. Set all properties to automated (IP,Subnetmask,etc)
 *  5. GO back and brick will now save the settings.
 *  6. IP should be displayed on the Brick. (IF NOT, IT WILL NOT WORK!)
 *  7. Deactivate USB-Debugging.
 *  8. App: Go to Settings and enter the IP address of the brick.
 *  9. Save settings.
 *  10. Click on connect-to-brick button.
 *
 *  Alternative:
 *  1. Brick: USB-PAN. IP,Subnetmask etc auf automated.
 *  2. Brick ausschalten.
 *  3. Smartphone per USB Kabel mit Brick verbinden.
 *  4. Tethering auf dem Smartphone aktivieren.
 *  5. Brick starten.
 *  6. Brick gestartet. IP sollte auf dem Display stehen.
 *  7. App starten und IP in Settings eintragen.
 *  8. Connect to Brick.
 */
public class MainActivity extends AppCompatActivity {

    public static SharedPreferences connectionProperties;

    //TODO Add reset Button - if Program on Brick gets resetted and you need to reconnect to the brick
    private Button btn_initConfiguration;
    private Button btn_connect;
    private Button btn_startRobot;
    private Button btn_stopRobot;
    private Button btn_settings;

    private TextView txt_isConnected;
    private TextView txt_robotState;
    private Spinner spinner_selectedStatemachine;


    /** Information Box **/
    private FrameLayout layout_info;
    private TextView txt_info;
    private Button btn_activateTethering;

    private ProgressDialog pd;

    public static Robot robot;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public final Activity instance = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        robot = new Robot(this);

        /** Instantiate buttons and textviews **/
        btn_initConfiguration = (Button) findViewById(R.id.btn_initConfig);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        spinner_selectedStatemachine = (Spinner) findViewById(R.id.spinner_selectedStatemachine);
        btn_startRobot = (Button) findViewById(R.id.btn_startRobot);
        btn_stopRobot = (Button) findViewById(R.id.btn_stopRobot);
        btn_settings = (Button) findViewById(R.id.btn_settings);

        txt_isConnected = (TextView) findViewById(R.id.txt_isConnected);
        txt_robotState = (TextView) findViewById(R.id.txt_stateRobot);


        /** Information Box **/
        layout_info = (FrameLayout) findViewById(R.id.layout_infobox);
        txt_info = (TextView) findViewById(R.id.txt_info);
        btn_activateTethering = (Button) findViewById(R.id.btn_activateTethering);

        /** Load Connection Properties **/
        loadConnectionProperties();

        /** get current IP **/
//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

        setButtonListeners();

        checkCurrentState();
    }

    private void setButtonListeners() {
        //Handle settings button
        btn_settings.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent nexScreen = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(nexScreen);
                }
            });


        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robot.connectToBrick();
            }
        });

        btn_initConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robot.configurateRobot();
            }
        });

        btn_startRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robot.startRobot();
            }
        });

        btn_stopRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robot.stopRobot();
            }
        });

        btn_activateTethering.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
            }
        });
    }

    public void setStatemachineIDs(String[] items){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner_selectedStatemachine.setAdapter(adapter);
    }

    public String getSelectedStatemachine(){
        return spinner_selectedStatemachine.getSelectedItem().toString();
    }


    /**
     * Continuesly checking the current state of:
     *  Robot state (connected ?, configuration ? , running? )
     *  USB State (Connected to usb ?, Tethering actiaveted? );
     *
     *  Updateing the UI in respect of the current state.
     */
    private void checkCurrentState() {
        final MainActivity main_activity = this;
        final Runnable taskUpdateButtonEnableState = new Runnable(){
            @Override
            public void run(){
                boolean positiveUSBState = isUSBConnected(main_activity) && isTetheringActivated(main_activity);

                btn_connect.setEnabled(!robot.isConnectedToBrick && positiveUSBState);

                btn_initConfiguration.setEnabled(robot.isConnectedToBrick && !robot.isConfigurationBuilt && positiveUSBState);

                btn_startRobot.setEnabled(robot.isConnectedToBrick && robot.isConfigurationBuilt && !robot.isRunning && positiveUSBState && !spinner_selectedStatemachine.getSelectedItem().toString().isEmpty());

                btn_stopRobot.setEnabled(robot.isRunning);

                spinner_selectedStatemachine.setEnabled(!robot.isRunning);

                btn_settings.setEnabled(!robot.isConnectedToBrick);


            }
        };

        final Runnable taskUpdateStatusView = new Runnable() {
            @Override
            public void run() {
                if(robot.isConnectedToBrick){
                    txt_isConnected.setText("Connected");
                }else{
                    txt_isConnected.setText("Not connected");
                }

                if(robot.isConfigurationBuilt){
                    //TODO
                }else{
                    //TODO
                }

                if(robot.isRunning){
                    txt_robotState.setText("running");
                }else{
                    txt_robotState.setText("not running");
                }
            }
        };

        final Runnable taskCheckUSBState = new Runnable() {
            @Override
            public void run() {
                StringBuffer sb = new StringBuffer();

                boolean isUSBConnected = false;
                boolean isTetheringActive = false;

                if(!(isUSBConnected = isUSBConnected(main_activity))){
                    sb.append("USB Kabel ist nicht eingesteckt. Prüfe die USB-Verbindung!");
                }

                if(isUSBConnected && !(isTetheringActive = isTetheringActivated(main_activity))){
                    sb.append("Tethering ist nicht aktiviert. Aktiviere Tethering, um eine Verbindung zum Brick herzustellen!");
                    btn_activateTethering.setVisibility(Button.VISIBLE);
                }else{
                    btn_activateTethering.setVisibility(Button.GONE);
                }

                if(!isUSBConnected || !isTetheringActive){
                    txt_info.setText(sb.toString());
                    layout_info.setVisibility(FrameLayout.VISIBLE);

                    //Stop robot
                    if(robot.isRunning){
                        robot.stopRobot();
                    }
                }else{
                    layout_info.setVisibility(FrameLayout.GONE);
                }
            }
        };

        //final Context cntxt_mainActivity = this;
        Runnable check = new Runnable() {
            @Override
            public void run() {
                while(true) {//TODO remove ?

                    //Enable/disable control buttons (Connect to brick, init configuration, start robot,stop robot)
                    MainActivity.this.runOnUiThread(taskUpdateButtonEnableState);

                    //Update View of the current State
                    MainActivity.this.runOnUiThread(taskUpdateStatusView);

                    //Update View of the current State
                    MainActivity.this.runOnUiThread(taskCheckUSBState);

                    //TODO remove those lines
                    //System.out.println("Usb device is rndis -> " + getUSBInfo(cntxt_mainActivity));
                    //txt_ipPhone.setText(getUSBInfo(cntxt_mainActivity));

                    try {
                        Thread.sleep(33);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };



        new Thread(check).start();
    } //USB_FUNCTION_RNDIS

    /**
     * Checks if USB is connected
     * @param context
     * @return
     */
    public boolean isUSBConnected(Context context){
        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));

        return intent.getExtras().getBoolean("connected");
    }

    /**
     * Checks if Tethering is activated.
     * @param context
     * @return
     */
    public boolean isTetheringActivated(Context context){
        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));

        return (intent.getExtras().getBoolean("rndis"));
    }


    private void loadConnectionProperties(){
        connectionProperties = getApplicationContext().getSharedPreferences("ConnectionData.xml",0);
        //TODO Refactor -> get settings from Settings.class
        if (connectionProperties != null) {
            String savedVal;
                savedVal = connectionProperties.getString(SettingsActivity.KEY_ROBOT_ID,SettingsActivity.DEFAULT_ROBOT_ID);
                Settings.getInstance().robotID = ( (savedVal.isEmpty()) ? SettingsActivity.KEY_ROBOT_ID : savedVal);

                savedVal = connectionProperties.getString(SettingsActivity.KEY_GROUP_ID,SettingsActivity.DEFAULT_GROUP_ID);
                Settings.getInstance().groupID = ( (savedVal.isEmpty()) ? SettingsActivity.KEY_GROUP_ID : savedVal);

                savedVal = connectionProperties.getString(SettingsActivity.KEY_EV3_IP, SettingsActivity.DEFAULT_EV3_IP);
                Settings.getInstance().ev3IP = ( (savedVal.isEmpty()) ? SettingsActivity.DEFAULT_EV3_IP : savedVal);

                savedVal = connectionProperties.getString(SettingsActivity.KEY_EV3_TCP_PORT,SettingsActivity.DEFAULT_EV3_TCP_PORT);
                Settings.getInstance().ev3TCPPort = (Integer.parseInt((savedVal.isEmpty()) ? SettingsActivity.DEFAULT_EV3_TCP_PORT : savedVal));

                savedVal = connectionProperties.getString(SettingsActivity.KEY_SERVER_IP, SettingsActivity.DEFAULT_SERVER_IP);
                Settings.getInstance().serverIP = ( (savedVal.isEmpty()) ? SettingsActivity.DEFAULT_SERVER_IP : savedVal);

                savedVal = connectionProperties.getString(SettingsActivity.KEY_SERVER_TCP_PORT,SettingsActivity.DEFAULT_SERVER_TCP_PORT);
                Settings.getInstance().serverTCPPort = (Integer.parseInt((savedVal.isEmpty()) ? SettingsActivity.DEFAULT_EV3_TCP_PORT : savedVal));

                savedVal = connectionProperties.getString(SettingsActivity.KEY_ROBOT_SERVER_TCP_PORT,SettingsActivity.DEFAULT_ROBOT_SERVER_PORT);
                Settings.getInstance().robotServerPort = (Integer.parseInt((savedVal.isEmpty()) ? SettingsActivity.DEFAULT_ROBOT_SERVER_PORT : savedVal));



            try {
                robot.makeRobot(); //Builds the robot with the Connection Settings
            } catch (StateAlreadyExists stateAlreadyExists) {
                showAlertDialog("State Already exists", stateAlreadyExists.getMessage());
            }
        }else{
            showAlertDialog("Error: Connection Properties","Couldn't Load connection properties. Check the Settings and may restart the application!");
        }

    }

    public void showAlertDialog(final String title,final String message){
        final MainActivity main_activity = this;

        Runnable createDialog = new Runnable() {
            public void run(){
                AlertDialog.Builder builder = new AlertDialog.Builder(main_activity);
                // Add the buttons
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                if (title == null) {
                    builder.setTitle("");
                } else {
                    builder.setTitle(title);
                }

                if (message == null) {
                    builder.setMessage("");
                } else {
                    builder.setMessage(message);
                }

                //builder.setIconAttribute( android.R.attr.alertDialogIcon);

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                }
        };

        runOnUiThread(createDialog);
    }


    public void showProgressDialog(final String title,final String message){
        final MainActivity main_activity = this;
        Runnable createProgressDialog = new Runnable() {
            public void run(){
                //pd is a global variable in this activity - so only one progress can be shown at the time
                pd = new ProgressDialog(main_activity,ProgressDialog.STYLE_SPINNER);
                pd.setTitle(title);
                pd.setMessage(message);
                pd.setCancelable(false);
                pd.show();
            }
        };

        runOnUiThread(createProgressDialog);
    }

    public void dismissCurrentProgressDialog(){
        final MainActivity main_activity = this;
        Runnable dismissProgressDialog = new Runnable() {
            public void run(){
                if(pd!=null) {
                    pd.dismiss();
                }
            }
        };

        runOnUiThread(dismissProgressDialog);
    }

}
