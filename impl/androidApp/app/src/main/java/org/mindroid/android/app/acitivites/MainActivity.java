package org.mindroid.android.app.acitivites;

import android.app.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.widget.DrawerLayout;

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
import org.mindroid.android.app.serviceloader.StatemachineService;
import org.mindroid.api.errorhandling.AbstractErrorHandler;

import java.util.ArrayList;
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

    public static Robot robot;

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

    private ArrayList<AlertDialog> shownDialogs = new ArrayList<AlertDialog>();

    private static final Logger LOGGER = Logger.getLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup Logger
        GlobalLogger.setup();

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(robot == null) {
            robot = new Robot();
        }

        SettingsProvider.getInstance().setAndroidId(Secure.getString(this.getContentResolver(), Secure.ANDROID_ID));
        errorHandler = new APIErrorHandler(this);

        /** init Statemachine Service**/
        initStatemachineService();

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

        initialiseSettings();

    }

    /**
     * Initializes the SettingsProvider Instance
     */
    private void initialiseSettings() {
        SharedPreferences connectionProperties = this.getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_connection_Data), Context.MODE_PRIVATE);
        SharedPreferences portConfigProperties = this.getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_portConfiguration),Context.MODE_PRIVATE);

        SettingsProvider.getInstance().initialize(getResources(),connectionProperties,portConfigProperties);
    }

    private void initStatemachineService(){
        StatemachineService.getInstance();
        StatemachineService.packageManager = getPackageManager();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

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
        for (AlertDialog shownDialog : shownDialogs) {
            if(shownDialog.isShowing()){
                shownDialog.dismiss();
            }
            shownDialogs.clear();
        }
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
}
