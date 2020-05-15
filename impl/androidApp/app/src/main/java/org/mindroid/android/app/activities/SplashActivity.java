package org.mindroid.android.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import org.mindroid.android.app.R;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.android.app.util.ShellService;
import org.mindroid.android.app.util.USBService;

public class SplashActivity extends Activity {

    private final int WELCOME_SCREEN_USB_TIMEOUT = 6000;
    private final int WELCOME_SCREEN_NO_USB_TIMEOUT = 1000;
    private final int DELAY_ACTIVATE_ADB_SERVICE = 10;
    private final int DELAY_GRANT_PERMISSIONS = 2000;
    private final int DELAY_ACTIVATE_TETHERING = 500;

    private TextView txtView_currentAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        this.txtView_currentAction = (TextView) findViewById(R.id.txtView_currentAction);

        //Initialize SettingsProvider
        initialiseSettings();

        final boolean isUsbConnected = USBService.isUSBConnected(this);
        int timeout =  WELCOME_SCREEN_NO_USB_TIMEOUT;
        if(isUsbConnected){
            timeout = WELCOME_SCREEN_USB_TIMEOUT;
        }


        //Splash Intent
        final Handler handler = new Handler();
        //Start MainActivity and destroy this one after the given Timeout
        final Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtView_currentAction.setText(getResources().getString(R.string.txt_starting_adb_service));
                setupADBService();

            }
        }, DELAY_ACTIVATE_ADB_SERVICE);

        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //GRANT READ AND WRITE PERMISSIONS (necessary to bypass manual activation)
                                    ShellService.grantReadExternalStoragePermission();
                                    ShellService.grantWriteExternalStoragePermission();
                                }
                            }

                , DELAY_GRANT_PERMISSIONS);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtView_currentAction.setText(getResources().getString(R.string.txt_activate_tethering));
                setupTethering(isUsbConnected);
            }
        }, DELAY_ACTIVATE_TETHERING);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtView_currentAction.setText(getResources().getString(R.string.txt_connecting_to_msg_server));
                connectToMsgServer();

            }
        }, timeout - 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(mainIntent);
                //Distroy activity
                finish();
            }
        }, timeout);
    }

    /**
     * Initializes the SettingsProvider Instance
     */
    private void initialiseSettings() {
        SharedPreferences connectionProperties = this.getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.shared_pref_connection_Data), Context.MODE_PRIVATE);
        SharedPreferences portConfigProperties = this.getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.shared_pref_portConfiguration),Context.MODE_PRIVATE);
        SharedPreferences adminProperties = this.getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.shared_pref_admin),Context.MODE_PRIVATE);

        SettingsProvider.getInstance().setAndroidId(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        SettingsProvider.getInstance().initialize(getResources(),connectionProperties,portConfigProperties, adminProperties);
    }


    private void setupADBService(){
        //Start ADB Service on phone
        this.txtView_currentAction.setText(getResources().getText(R.string.txt_starting_adb_service));
        ShellService.startADB(ShellService.ADB_DEFAULT_PORT);
    }

    /**
     * Starts ADB Service on Phone.
     * Activates Tethering.
     */
    private void setupTethering(boolean isUsbConnected) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //GET ssid of the currently connected wifi
        /*String connectedSSID = wifiManager.getConnectionInfo().getSSID();
        WifiConfiguration currentWifiConfiguration = null;
        if(connectedSSID != null && !connectedSSID.equals("\"<unknown ssid>\"")){ // <unknown ssid> is quoted!
            //Currently connected to a wifi network

            for (WifiConfiguration wifiConfiguration : wifiManager.getConfiguredNetworks()) {
                if(wifiConfiguration.SSID.equals(connectedSSID)){
                    //Network configuration found
                    currentWifiConfiguration = wifiConfiguration;
                    break;
                }
            }
        }
        */

        wifiManager.setWifiEnabled(false);


        //Check USB Connection
        txtView_currentAction.setText(getResources().getText(R.string.txt_checking_usb));

        //activate Tethering
        if(isUsbConnected) {
            this.txtView_currentAction.setText(getResources().getText(R.string.txt_activate_tethering));
            ShellService.setTethering(true);
        }

        wifiManager.setWifiEnabled(true);
        /*if(currentWifiConfiguration != null){
            //was conneceted to a wifi network before shutdown
            String tmpConnectedSSID = wifiManager.getConnectionInfo().getSSID();
            //SSID of the connected wifi network
            if(!tmpConnectedSSID.equals(connectedSSID)){
                //Connected to another wifi network as before
                //close connection and connect to the old one
                wifiManager.disconnect();
                wifiManager.enableNetwork(currentWifiConfiguration.networkId,true);
                wifiManager.reconnect();
            }
        }*/
    }

    private void connectToMsgServer(){
        AsyncTask task = new ConnectToMSGServerTask();
        task.execute();
    }

    private class ConnectToMSGServerTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            return MainActivity.robot.connectMessenger(SettingsProvider.getInstance().getMsgServerIP(),SettingsProvider.getInstance().getMsgServerPort());
        }
    }
}