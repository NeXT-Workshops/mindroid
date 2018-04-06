package org.mindroid.android.app.acitivites;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;
import org.mindroid.android.app.R;
import org.mindroid.android.app.util.ShellService;
import org.mindroid.android.app.util.USBService;

public class SplashActivity extends Activity {

    private final int WELCOME_SCREEN_TIMEOUT = 5000;

    private TextView txtView_currentAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Splash Intent
        final Handler handler = new Handler();
        //Start MainActivity and destroy this one after the given Timeout
        final Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(mainIntent);
                //Distroy activity
                finish();
            }
        }, this.WELCOME_SCREEN_TIMEOUT);

        setup();
    }


    private void setup() {
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        this.txtView_currentAction = (TextView) findViewById(R.id.txtView_currentAction);

        //Start ADB Service on phone
        this.txtView_currentAction.setText(getResources().getText(R.string.txt_starting_adb_service));
        ShellService.startADB(ShellService.ADB_DEFAULT_PORT);

        //Check USB Connection
        txtView_currentAction.setText(getResources().getText(R.string.txt_checking_usb));

        //activate Tethering
        this.txtView_currentAction.setText(getResources().getText(R.string.txt_activate_tethering));
        ShellService.setTethering(true);

        //TODO connect to message server
    }
}