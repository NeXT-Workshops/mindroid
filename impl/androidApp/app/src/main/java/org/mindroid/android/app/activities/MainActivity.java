package org.mindroid.android.app.activities;

import android.app.*;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import org.mindroid.android.app.R;
import org.mindroid.android.app.dialog.ErrorDialog;
import org.mindroid.android.app.dialog.InfoDialog;
import org.mindroid.android.app.errorhandling.APIErrorHandler;
import org.mindroid.android.app.fragments.admin.AdminFragment;
import org.mindroid.android.app.fragments.log.LoggerFragment;
import org.mindroid.android.app.fragments.myrobot.MyRobotFragment;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.fragments.home.RobotSetupInfoFragment;
import org.mindroid.android.app.fragments.sensormonitoring.SensorMonitoringFragment;
import org.mindroid.android.app.fragments.sensormonitoring.SensorObservationFragment;
import org.mindroid.android.app.fragments.settings.SettingsFragment;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.api.errorhandling.AbstractErrorHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener,
        SensorMonitoringFragment.OnFragmentInteractionListener,
        SensorObservationFragment.OnFragmentInteractionListener,
        SettingsFragment.OnSettingsChanged,
        AdminFragment.OnAdminChanged,
        MyRobotFragment.OnFragmentInteractionListener,
        RobotSetupInfoFragment.OnFragmentInteractionListener,
        IErrorHandler{

    public static Robot robot = new Robot();

    private APIErrorHandler errorHandler;

    /** Used Fragments **/ //TODO remove parameters, which are not used
    private Fragment HOME_FRAGMENT = HomeFragment.newInstance();
    private Fragment CONFIG_FRAGMENT = MyRobotFragment.newInstance();
    private Fragment SETTINGS_FRAGMENT = SettingsFragment.newInstance();
    private Fragment SENSOR_MONITOR_FRAGMENT = SensorMonitoringFragment.newInstance();
    private Fragment LOG_FRAGMENT = LoggerFragment.newInstance();
    private Fragment ADMIN_FRAGMENT = AdminFragment.newInstance();

    private final String TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT";
    private final String TAG_MYROBOT_FRAGMENT = "TAG_MYROBOT_FRAGMENT";
    private final String TAG_SETTINGS_FRAGMENT = "TAG_SETTINGS_FRAGMENT";
    private final String TAG_SENSOR_MONITOR = "TAG_SENSOR_MONITOR";
    private final String TAG_LOG_FRAGMENT = "TAG_LOG_FRAGMENT";
    private final String TAG_ADMIN_FRAGMENT = "TAG_ADMIN_FRAGMENT";

    private static final Logger LOGGER = Logger.getLogger(MainActivity.class.getName());

    private DrawerLayout mDrawerLayout;

    private NavigationView navigationView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LOGGER.info("Main Activity on Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        // close drawer when item is tapped
                        //mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        // Done using onClick in xml resource calling methods open..Fragment(..)

                        return true;
                    }
                });

        initialize(savedInstanceState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize(Bundle savedInstanceState) {
        //If the app is running the display will be set to always on and the device will not go into sleep mode.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        errorHandler = new APIErrorHandler(this);

        //display Home Fragment
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, HOME_FRAGMENT)
                    .commit();
        }
    }

    /**
     * Switches the main fragment and closes the drawers and the possible open keyboard
     *
     * @param fragTag - tag/id of the fragment to switch to
     */
    private void switchFragAndCloseDrawerAndKeyboard(String fragTag){
        switchFragment(fragTag);

        //Close Menu Drawer
        mDrawerLayout.closeDrawers();

        //Close keyboard
        hideKeyboard(this);
    }

    /**
     * Hides the keyboard. Always needs to be called from an activity!!!
     *
     * Thanks to: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
     *
     * @param activity
     */
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        //Clear focus to avoid showing keyboard again if you open the app from the background
        view.clearFocus();
    }

    public void openHomeFragment(MenuItem item) {
        switchFragAndCloseDrawerAndKeyboard(TAG_HOME_FRAGMENT);
    }

    public void openSensorMonitoringFragment(MenuItem item){
        switchFragAndCloseDrawerAndKeyboard(TAG_SENSOR_MONITOR);
    }

    public void openMyRobotFragment(MenuItem item) {
        switchFragAndCloseDrawerAndKeyboard(TAG_MYROBOT_FRAGMENT);
    }

    public void openSettingsFragment(MenuItem item) {
        switchFragAndCloseDrawerAndKeyboard(TAG_SETTINGS_FRAGMENT);
    }

    public void openLogFragment(MenuItem item) {
        switchFragAndCloseDrawerAndKeyboard(TAG_LOG_FRAGMENT);
    }

    public void openAdminFragment(MenuItem item){
        switchFragAndCloseDrawerAndKeyboard(TAG_ADMIN_FRAGMENT);
    }

    public void createShortcut(MenuItem item){
        createShortcut();
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.txt_shortcut_success),Toast.LENGTH_LONG).show();
        mDrawerLayout.closeDrawers();
    }


    /**
     * Changes the main fragment dependent on the given position parameter
     * @param position - id of the fragment
     */
    private void switchFragment(String position) {
        switch(position){
            case TAG_HOME_FRAGMENT:
                replaceFragment(HOME_FRAGMENT,TAG_HOME_FRAGMENT);
                setTitle(getResources().getString(R.string.title_home));
                break;//Home
            case TAG_SENSOR_MONITOR:
                replaceFragment(SENSOR_MONITOR_FRAGMENT, TAG_SENSOR_MONITOR);
                setTitle(getResources().getString(R.string.title_sensor_monitoring));
                break;
            case TAG_MYROBOT_FRAGMENT:
                replaceFragment(CONFIG_FRAGMENT, TAG_MYROBOT_FRAGMENT);
                setTitle(getResources().getString(R.string.title_myrobot));
                break;//Configuration
            case TAG_SETTINGS_FRAGMENT:
                replaceFragment(SETTINGS_FRAGMENT, TAG_SETTINGS_FRAGMENT);
                setTitle(getResources().getString(R.string.title_settings));
                break;//SettingsProvider
            case TAG_LOG_FRAGMENT:
                replaceFragment(LOG_FRAGMENT, TAG_LOG_FRAGMENT);
                setTitle(getResources().getString(R.string.title_log));
                break;//SettingsProvider
            case TAG_ADMIN_FRAGMENT:
                replaceFragment(ADMIN_FRAGMENT, TAG_ADMIN_FRAGMENT);
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

    @Override
    public void onBackPressed() {
        switchFragAndCloseDrawerAndKeyboard(TAG_HOME_FRAGMENT);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSettingsChanged(boolean settingsChanged){
        ((HomeFragment)HOME_FRAGMENT).onSettingsChanged(settingsChanged);

        // makes sure Home is selected in Drawer
        switchFragAndCloseDrawerAndKeyboard(TAG_HOME_FRAGMENT);

        InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imgr.showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);
        imgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }



    @Override
    public void showErrorDialog(final String title, final String message){
        LOGGER.log(Level.WARNING,"ErrorDialog Shown: Title: "+title+" Msg: "+message);
        DialogFragment errorDialogFragment = ErrorDialog.newInstance(title,message);
        errorDialogFragment.show(getFragmentManager(), "errorDialog");
    }

    @Override
    public void enableMenuItems() {
        Runnable changeItemState = new Runnable() {
            @Override
            public void run() {
                navigationView.getMenu().setGroupEnabled(R.id.menu_group_settigns,true);
            }
        };
        runOnUiThread(changeItemState);
    }

    @Override
    public void disableMenuItems() {
        Runnable changeItemState = new Runnable() {
            @Override
            public void run() {
                navigationView.getMenu().setGroupEnabled(R.id.menu_group_settigns,false);
            }
        };
        runOnUiThread(changeItemState);
    }


    public void showInfoDialog(final String title, final String message){
        DialogFragment infoDialogFragment = InfoDialog.newInstance(title,message);
        infoDialogFragment.show(getFragmentManager(), "infoDialog");
    }

    private Intent createShortcutIntent(){
        Intent shortcutIntent = new Intent(getApplicationContext(),
                SplashActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.mindroid_with_tango));

        return addIntent;
    }

    private void removeShortcut(){
        Intent shortcutIntent = createShortcutIntent();
        shortcutIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(shortcutIntent);
    }

    /**
     * Thanks to: http://viralpatel.net/blogs/android-install-uninstall-shortcut-example/
     */
    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = createShortcutIntent();
        shortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(shortcutIntent);
    }

    private void createShortcut(){
        //Remove shortcut to not add shortcut twice on screen
        removeShortcut();
        addShortcut();
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
        if(AdminChanged) {
            switchFragAndCloseDrawerAndKeyboard(TAG_HOME_FRAGMENT);
        }

    }

}
