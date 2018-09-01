package org.mindroid.android.app.activities;

import android.app.*;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.widget.DrawerLayout;

import android.view.WindowManager;
import android.widget.ListView;
import org.mindroid.android.app.R;
import org.mindroid.android.app.dialog.ErrorDialog;
import org.mindroid.android.app.dialog.InfoDialog;
import org.mindroid.android.app.errorhandling.APIErrorHandler;
import org.mindroid.android.app.fragments.admin.AdminFragment;
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
import org.mindroid.api.errorhandling.AbstractErrorHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        HomeFragment.OnFragmentInteractionListener,
        SensorMonitoringFragment.OnFragmentInteractionListener,
        SensorObservationFragment.OnFragmentInteractionListener,
        SettingsFragment.OnSettingsChanged,
        AdminFragment.OnAdminChanged,
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
    private Fragment ADMIN_FRAGEMENT = AdminFragment.newInstance();

    private final String TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT";
    private final String TAG_CONFIG_FRAGMENT = "TAG_CONFIG_FRAGMENT";
    private final String TAG_SETTINGS_FRAGMENT = "TAG_SETTINGS_FRAGMENT";
    private final String TAG_SENSOR_MONITOR = "TAG_SENSOR_MONITOR";
    private final String TAG_LOG_FRAGMENT = "TAG_LOG_FRAGMENT";
    private final String TAG_ADMIN_FRAGMENT = "TAG_ADMIN_FRAGMENT";

    private static final Logger LOGGER = Logger.getLogger(MainActivity.class.getName());



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initialize(savedInstanceState);

        addShortcut();
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
                replaceFragment(SENSOR_MONITOR_FRAGMENT, TAG_SENSOR_MONITOR);
                setTitle(getResources().getString(R.string.title_sensor_monitoring));
                break;
            case 2:
                replaceFragment(CONFIG_FRAGMENT, TAG_CONFIG_FRAGMENT);
                setTitle(getResources().getString(R.string.title_myrobot));
                break;//Configuration
            case 3:
                replaceFragment(SETTINGS_FRAGMENT, TAG_SETTINGS_FRAGMENT);
                setTitle(getResources().getString(R.string.title_settings));
                break;//SettingsProvider
            case 4:
                replaceFragment(LOG_FRAGMENT, TAG_LOG_FRAGMENT);
                setTitle(getResources().getString(R.string.title_log));
                break;//SettingsProvider
            case 5:
                replaceFragment(ADMIN_FRAGEMENT, TAG_ADMIN_FRAGMENT);
                setTitle(getResources().getString(R.string.title_admin));
                break;//Admin
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
            case 5:
                mTitle = getResources().getText(R.string.title_admin);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mNavigationDrawerFragment.selectItem(0);
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

        // makes sure Home is selected in Drawer
        mNavigationDrawerFragment.selectItem(0);
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

    /**
     * Thanks to: http://viralpatel.net/blogs/android-install-uninstall-shortcut-example/
     */
    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                MainActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.mindroid_with_tango));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
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

    @Override
    public void onAdminChanged(boolean AdminChanged) {
        if(AdminChanged)
            mNavigationDrawerFragment.selectItem(0);

    }
}
