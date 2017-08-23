package org.mindroid.android.app.acitivites;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;

import android.widget.ListView;
import org.mindroid.android.app.R;
import org.mindroid.android.app.dialog.ErrorDialog;
import org.mindroid.android.app.dialog.InfoDialog;
import org.mindroid.android.app.errorhandling.APIErrorHandler;
import org.mindroid.android.app.fragments.myrobot.MyRobotFragment;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.fragments.NavigationDrawerFragment;
import org.mindroid.android.app.fragments.home.RobotSetupInfoFragment;
import org.mindroid.android.app.fragments.sensormonitoring.SensorMonitoringFragment;
import org.mindroid.android.app.fragments.sensormonitoring.SensorObservationFragment;
import org.mindroid.android.app.fragments.settings.SettingsFragment;
import org.mindroid.android.app.serviceloader.StatemachineService;
import org.mindroid.api.errorhandling.AbstractErrorHandler;

import java.util.Locale;

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

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private APIErrorHandler errorHandler = new APIErrorHandler(this);

    /** Used Fragments **/
    private final Fragment HOME_FRAGMENT = HomeFragment.newInstance("","");
    private final Fragment CONFIG_FRAGMENT = MyRobotFragment.newInstance("","");
    private final Fragment SETTINGS_FRAGMENT = SettingsFragment.newInstance("","");
    private final Fragment SENSOR_MONITOR_FRAGMENT = SensorMonitoringFragment.newInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /** init Statemachine Service**/
        initStatemachineService();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        //show Home Fragment
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, HOME_FRAGMENT)
                    .commit();
        }

    }

    private void initStatemachineService(){
        StatemachineService.getInstance();
        StatemachineService.packageManager = getPackageManager();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        String tag;
        switch(position){
            case 0: fragment = HOME_FRAGMENT;
                    setTitle(getResources().getString(R.string.title_home));
                break;//Home
            case 1: fragment = SENSOR_MONITOR_FRAGMENT;
                    setTitle(getResources().getString(R.string.title_sensor_monitoring));
                    break;
            case 2: fragment = CONFIG_FRAGMENT;
                    setTitle(getResources().getString(R.string.title_myrobot));
                break;//Configuration
            case 3: fragment = SETTINGS_FRAGMENT;
                    setTitle(getResources().getString(R.string.title_settings));
                break;//Settings
            default:
                System.out.println("## MainActivity.onNavigationDrawerItemSelected(): No fragment defined for this position");
                fragment = HOME_FRAGMENT;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
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
        final Context context = this;
        Runnable showErrorDialog = new Runnable(){
            @Override
            public void run() {
                ErrorDialog.newInstance(context,title,message).show();
            }
        };
        runOnUiThread(showErrorDialog);
    }

    public void showInfoDialog(final String title, final String message){
        final Context context = this;
        Runnable showErrorDialog = new Runnable(){
            @Override
            public void run() {
                InfoDialog.newInstance(context,title,message).show();
            }
        };
        runOnUiThread(showErrorDialog);
    }

    /**
     * Changes the app language
     * @param localLang
     */
    public void setLocale(Locale localLang) {
        //TODO Does not work properly
        Locale myLocale = localLang;
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);

    }

    public ListView getMenuItemListView(){
        return mNavigationDrawerFragment.getmDrawerListView();
    }

    @Override
    public AbstractErrorHandler getErrorHandler() {
        return errorHandler;
    }

}
