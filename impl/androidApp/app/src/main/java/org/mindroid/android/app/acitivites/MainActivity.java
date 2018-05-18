package org.mindroid.android.app.acitivites;

import android.app.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.widget.DrawerLayout;

import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;
import org.mindroid.android.app.R;
import org.mindroid.android.app.dialog.ErrorDialog;
import org.mindroid.android.app.dialog.InfoDialog;
import org.mindroid.android.app.errorhandling.APIErrorHandler;
import org.mindroid.android.app.fragments.log.GlobalLogger;
import org.mindroid.android.app.fragments.log.LoggerFragment;
import org.mindroid.android.app.fragments.myrobot.MyRobotFragment;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.fragments.NavigationDrawerFragment;
import org.mindroid.android.app.fragments.home.RobotSetupInfoFragment;
import org.mindroid.android.app.fragments.sensormonitoring.SensorMonitoringFragment;
import org.mindroid.android.app.fragments.sensormonitoring.SensorObservationFragment;
import org.mindroid.android.app.fragments.settings.SettingsFragment;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.android.app.util.ShellService;
import org.mindroid.api.errorhandling.AbstractErrorHandler;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        SettingsFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        SensorMonitoringFragment.OnFragmentInteractionListener,
        SensorObservationFragment.OnFragmentInteractionListener,
        SettingsFragment.OnSettingsChanged,
        MyRobotFragment.OnFragmentInteractionListener,
        RobotSetupInfoFragment.OnFragmentInteractionListener,
        IErrorHandler{

    public static Robot robot = new Robot();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private APIErrorHandler errorHandler;

    /** Used Fragments **/ //TODO remove parameters, which are not used
    private Fragment HOME_FRAGMENT = HomeFragment.newInstance();
    private Fragment CONFIG_FRAGMENT = MyRobotFragment.newInstance();
    private Fragment SETTINGS_FRAGMENT = SettingsFragment.newInstance();
    private Fragment SENSOR_MONITOR_FRAGMENT = SensorMonitoringFragment.newInstance();
    private Fragment LOG_FRAGMENT = LoggerFragment.newInstance();

    private final String TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT";
    private final String TAG_CONFIG_FRAGMENT = "TAG_CONFIG_FRAGMENT";
    private final String TAG_SETTINGS_FRAGMENT = "TAG_SETTINGS_FRAGMENT";
    private final String TAG_SENSOR_MONITOR = "TAG_SENSOR_MONITOR";
    private final String TAG_LOG_FRAGMENT = "TAG_LOG_FRAGMENT";

    private static final Logger LOGGER = Logger.getLogger(MainActivity.class.getName());



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initialize(savedInstanceState);

    }

    private void initialize(Bundle savedInstanceState) {
        //If the app is running the display will be set to always on and the device will not go into sleep mode.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SettingsProvider.getInstance().setAndroidId(Secure.getString(this.getContentResolver(), Secure.ANDROID_ID));
        errorHandler = new APIErrorHandler(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        //display Home Fragment
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, HOME_FRAGMENT)
                    .commit();
        }

        //READ IF CONFIG
        /*Runnable run = new Thread(){
            public void run(){
                readIFConfig();
            }
        };
        new Thread(run).start();
        */

        //Activate tethering - (used to activate tethering automatically after app got started after deployment)
        //Only works if phone is connected to to brick by usb
        //ShellService.setTethering(true);
    }

    @Deprecated
    private void readIFConfig(){
        try {
            String dir = Environment.getExternalStorageDirectory()+File.separator+"mindroid";
            System.out.println("### IFCONFIG OUTPUT: dirpath: "+dir);
            //create folder
            File folder = new File(dir); //folder name
            folder.mkdirs();

            //create file
            File outputFile = new File(dir, "output_ifconfig.txt");
            outputFile.createNewFile();

            String filePath = outputFile.getPath();

            System.out.println("### IFCONFIG OUTPUT: outputfile: "+filePath);
            System.out.println("### IFCONFIG OUTPUT: exec shell cmd ");
            LOGGER.log(Level.INFO,"Start to execute IFCOnfig");
            ShellService.execIfConfig(filePath);
            LOGGER.log(Level.INFO,"IFConfig command executed");
            LOGGER.log(Level.INFO,"write into file: "+filePath);

            BufferedReader br = new BufferedReader(new FileReader(outputFile));
            StringBuffer output = new StringBuffer();
            System.out.println("### IFCONFIG OUTPUT: start reading file ");
            LOGGER.log(Level.INFO,"IFCONIFG: Start reading output from file");
            while(br.read() != -1){
                output.append(br.readLine());
            }

            LOGGER.log(Level.INFO,"IFCONIFG: End reading output from file");

            LOGGER.log(Level.INFO,output.toString());
            System.out.println("### IFCONFIG OUTPUT: "+output.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switchFragment(position);
    }

    /**
     * Changes the main fragment dependent on the given position parameter
     * @param position - id of the fragment
     */
    private void switchFragment(int position) {
        switch(position){
            case 0:
                replaceFragment(HOME_FRAGMENT,TAG_HOME_FRAGMENT);
                setTitle(getResources().getString(R.string.title_home));
                break;//Home
            case 1:
                replaceFragment(SENSOR_MONITOR_FRAGMENT,TAG_SENSOR_MONITOR);
                setTitle(getResources().getString(R.string.title_sensor_monitoring));
                break;
            case 2:
                replaceFragment(CONFIG_FRAGMENT,TAG_CONFIG_FRAGMENT);
                setTitle(getResources().getString(R.string.title_myrobot));
                break;//Configuration
            case 3:
                replaceFragment(SETTINGS_FRAGMENT,TAG_SETTINGS_FRAGMENT);
                setTitle(getResources().getString(R.string.title_settings));
                break;//SettingsProvider
            case 4:
                replaceFragment(LOG_FRAGMENT,TAG_LOG_FRAGMENT);
                setTitle(getResources().getString(R.string.title_log));
                break;//SettingsProvider
            default:
                replaceFragment(HOME_FRAGMENT,TAG_HOME_FRAGMENT);
        }
    }

    /**
     * Replaces the Fragments of the main container.
     * @param newFrag - fragment to add, if its not found
     * @param fragmentTag - the unique fragment tag, to identify the fragment
     */
    private void replaceFragment(Fragment newFrag, String fragmentTag){
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment fragment = fManager.findFragmentByTag(fragmentTag);
        if(fragment == null) {
            fTransaction.replace(R.id.container, newFrag, fragmentTag);
        }
        fTransaction.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_home);
                break;
            case 1:
                mTitle = getString(R.string.title_sensor_monitoring);
                break;
            case 2:
                mTitle = getString(R.string.title_myrobot);
                break;
            case 3:
                mTitle = getString(R.string.title_settings);
                break;
            case 4:
                mTitle = getString(R.string.title_log);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switchFragment(0);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSettingsChanged(boolean settingsChanged){
        ((HomeFragment)HOME_FRAGMENT).onSettingsChanged(settingsChanged);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, HOME_FRAGMENT)
                .commit();
    }

    @Override
    public void showErrorDialog(final String title, final String message){
        LOGGER.log(Level.WARNING,"ErrorDialog Shown: Title: "+title+" Msg: "+message);
        DialogFragment errorDialogFragment = ErrorDialog.newInstance(title,message);
        errorDialogFragment.show(getFragmentManager(), "errorDialog");
    }


    public void showInfoDialog(final String title, final String message){
        DialogFragment infoDialogFragment = InfoDialog.newInstance(title,message);
        infoDialogFragment.show(getFragmentManager(), "infoDialog");
    }

    public ListView getMenuItemListView(){
        return mNavigationDrawerFragment.getmDrawerListView();
    }

    @Override
    public AbstractErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public void onDetachedFromWindow() {
        LOGGER.log(Level.INFO,"App got detached from Window");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
