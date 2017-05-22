package org.mindroid.android.app.acitivites;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import org.mindroid.android.app.R;
import org.mindroid.android.app.dialog.ErrorDialog;
import org.mindroid.android.app.fragments.myrobot.HardwareSelectionFragment;
import org.mindroid.android.app.fragments.myrobot.MyRobotFragment;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.fragments.NavigationDrawerFragment;
import org.mindroid.android.app.fragments.home.RobotSetupInfoFragment;
import org.mindroid.android.app.fragments.settings.SettingsFragment;
import org.mindroid.android.app.robodancer.Settings;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, SettingsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,SettingsFragment.OnSettingsChanged, MyRobotFragment.OnFragmentInteractionListener, RobotSetupInfoFragment.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static SharedPreferences connectionProperties;

    /** Used Fragments **/
    private final Fragment HOME_FRAGMENT = HomeFragment.newInstance("","");
    private final Fragment CONFIG_FRAGMENT = MyRobotFragment.newInstance("","");
    private final Fragment SETTINGS_FRAGMENT = SettingsFragment.newInstance("","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Load Connection Properties from sharedPreferences
        loadConnectionProperties();

        //show Home Fragment
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, HOME_FRAGMENT)
                    .commit();
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        String tag;
        switch(position){
            case 0: fragment = HOME_FRAGMENT;
                    setTitle(getResources().getString(R.string.title_section1));
                break;//Home
            case 1: fragment = CONFIG_FRAGMENT;
                    setTitle(getResources().getString(R.string.title_section2));
                break;//Configuration
            case 2: fragment = SETTINGS_FRAGMENT;
                    setTitle(getResources().getString(R.string.title_section3));
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
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
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

    private void loadConnectionProperties(){
        connectionProperties = getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_connection_Data), Context.MODE_PRIVATE);
        //TODO Refactor -> get settings from Settings.class
        if (connectionProperties != null) {
            String savedVal;
            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_ROBOT_ID),getResources().getString(R.string.DEFAULT_ROBOT_ID));
            Settings.getInstance().robotID = ( (savedVal.isEmpty()) ? getResources().getString(R.string.KEY_ROBOT_ID) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_GROUP_ID),getResources().getString(R.string.DEFAULT_GROUP_ID));
            Settings.getInstance().groupID = ( (savedVal.isEmpty()) ? getResources().getString(R.string.KEY_GROUP_ID) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_EV3_IP), getResources().getString(R.string.DEFAULT_EV3_BRICK_IP));
            Settings.getInstance().ev3IP = ( (savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_EV3_BRICK_IP) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_EV3_TCP_PORT),getResources().getString(R.string.DEFAULT_EV3_BRICK_PORT));
            Settings.getInstance().ev3TCPPort = (Integer.parseInt((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_EV3_BRICK_PORT) : savedVal));

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_SERVER_IP), getResources().getString(R.string.DEFAULT_MSG_SERVER_IP));
            Settings.getInstance().serverIP = ( (savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_MSG_SERVER_IP) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_SERVER_TCP_PORT),getResources().getString(R.string.DEFAULT_MSG_SERVER_PORT));
            Settings.getInstance().serverTCPPort = (Integer.parseInt((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_MSG_SERVER_PORT) : savedVal));

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_ROBOT_SERVER_TCP_PORT),getResources().getString(R.string.DEFAULT_BRICK_MSG_SERVER_PORT));
            Settings.getInstance().robotServerPort = (Integer.parseInt((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_BRICK_MSG_SERVER_PORT) : savedVal));

        }else{
            //TODO
            //System.out.println("Error: Connection Properties","Couldn't Load connection properties. Check the Settings and may restart the application!");
            //showAlertDialog("Error: Connection Properties","Couldn't Load connection properties. Check the Settings and may restart the application!");
        }

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

}
