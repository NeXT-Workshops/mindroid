package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.*;

import org.mindroid.android.app.R;
import org.mindroid.android.app.activities.IErrorHandler;
import org.mindroid.android.app.activities.MainActivity;
import org.mindroid.android.app.asynctasks.ProgressTask;
import org.mindroid.android.app.dialog.ProgressDialog;
import org.mindroid.android.app.fragments.log.GlobalLogger;
import org.mindroid.android.app.fragments.settings.SettingsFragment;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.android.app.serviceloader.ImplementationService;
import org.mindroid.android.app.util.ShellService;
import org.mindroid.android.app.util.USBService;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SettingsFragment.OnSettingsChanged {


    //Task
    private final String MSG_TASK_CONNECT = "connect";
    private final String MSG_TASK_DISCONNECT = "disconnectFromBrick";

    //--- Buttons etc --//

    private FrameLayout layout_info;
    private Button btn_messengerConnDisconn;
    private Switch switch_enableSimulation;
    private Button btn_activateTethering;
    private Button btn_connect;
    private Button btn_disconnect;
    private Button btn_startRobot;
    private Button btn_stopRobot;
    private TextView txt_info;

    //Message client Connection state
    private TextView txtView_msgServerConnectionState;

    private TextView txt_program_set;
    private Spinner spinner_program_sets;
    private Spinner spinner_implementations;

    /** StartStop-Robot-Task cmd **/
    private final String START_ROBOT = "start";
    private final String STOP_ROBOT ="stop";

    public static Robot robot;

    MainActivity parentActivity;

    // Resources
    private String msgConnectToRobotError;
    private String msgDisconnectFromRobot;
    private String msgConnectToRobot;
    private String msgInitConfiguration;
    private String msgStartRobot;
    private String msgStopRobot;
    private String infoUsbNotFound;
    private String infoTetheringNotActiavted;
    private String txt_btn_DisConnect_connect;
    private String txt_btn_DisConnect_disconnect;

    private final int ACTIVATE_TETHERING_TIMEOUT = 4000;

    private SparseBooleanArray menuItemAlwaysEnabled = new SparseBooleanArray();

    private final static Logger LOGGER = Logger.getLogger(HomeFragment.class.getName());

    private boolean stopThreads;

    public HomeFragment() { //Called by newInstance(..)
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        LogManager.getLogManager().addLogger(LOGGER);
        GlobalLogger.getInstance().registerLogger(LOGGER);
        LOGGER.setLevel(Level.INFO);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        robot = MainActivity.robot;
        parentActivity = (MainActivity) getActivity();

        stopThreads = false;

        if(parentActivity instanceof IErrorHandler){
            robot.registerErrorHandler(((IErrorHandler) parentActivity).getErrorHandler());
        }

        getMenuEnabledSettings();
    }

    /**
     *
     */
    private void getMenuEnabledSettings() {
        //TODO refactor to a another class to configure menu
        //SettingsProvider menu enable
        menuItemAlwaysEnabled.put(0,true);
        menuItemAlwaysEnabled.put(1,true);
        menuItemAlwaysEnabled.put(2,false);
        menuItemAlwaysEnabled.put(3,false);
        menuItemAlwaysEnabled.put(4,true);
        menuItemAlwaysEnabled.put(5,false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Get Resources
        msgConnectToRobot = getResources().getString(R.string.dialog_msg_connect_to_robot);
        msgConnectToRobotError = getResources().getString(R.string.dialog_msg_connect_to_robot_error);
        msgDisconnectFromRobot = getResources().getString(R.string.dialog_msg_disconnect_from_robot);
        msgInitConfiguration = getResources().getString(R.string.dialog_msg_init_config);
        msgStartRobot = getResources().getString(R.string.dialog_msg_start_robot);
        msgStopRobot = getResources().getString(R.string.dialog_msg_stop_robot);
        infoTetheringNotActiavted = getResources().getString(R.string.info_txt_msg_activate_tetherting);
        infoUsbNotFound = getResources().getString(R.string.info_txt_msg_activate_usb_not_found);
        txt_btn_DisConnect_connect = getResources().getString(R.string.txt_btn_DisConnect_connect);
        txt_btn_DisConnect_disconnect = getResources().getString(R.string.txt_btn_DisConnect_disconnect);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        /** Instantiate buttons and textviews **/


        btn_messengerConnDisconn = (Button) view.findViewById(R.id.btn_messengerConnDisconn);
        switch_enableSimulation = (Switch) view.findViewById(R.id.switch_enable_simulation);
        btn_connect = (Button) view.findViewById(R.id.btn_connect);
        btn_disconnect = (Button) view.findViewById(R.id.btn_disconnect);
        spinner_implementations = (Spinner) view.findViewById(R.id.spinner_selectedStatemachine);
        btn_startRobot = (Button) view.findViewById(R.id.btn_startRobot);
        btn_stopRobot = (Button) view.findViewById(R.id.btn_stopRobot);

        /** ActivateTethering-Information Box **/
        layout_info = (FrameLayout) view.findViewById(R.id.layout_infobox);
        txt_info = (TextView) view.findViewById(R.id.txt_info);
        btn_activateTethering = (Button) view.findViewById(R.id.btn_activateTethering);

        //Radio Button displaying connection state to the message server
        txtView_msgServerConnectionState = (TextView) view.findViewById(R.id.txtView_msgServerConnectionState);

        btn_activateTethering.setText(getResources().getString(R.string.btn_text_activate_tethering));
        btn_connect.setText(getResources().getString(R.string.btn_text_connect));
        btn_disconnect.setText(getResources().getString(R.string.btn_text_disconnect));
        btn_disconnect.setVisibility(View.GONE);
        btn_startRobot.setText(getResources().getString(R.string.btn_text_start_robot));
        btn_stopRobot.setText(getResources().getString(R.string.btn_text_stop_robot));

        //Add RobotSetupInfo Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_robotSetupInfo, RobotSetupInfoFragment.newInstance());
        //transaction.addToBackStack(null);
        transaction.commit();

        ArrayAdapter<String> adapter = getImplementationIDAdapter();
        spinner_implementations.setAdapter(adapter);

        txt_program_set = (TextView) view.findViewById(R.id.txt_program_set);
        spinner_program_sets = (Spinner) view.findViewById(R.id.spinner_program_set);

        spinner_program_sets.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ImplementationService.getInstance().getImplementationSets()));

        //Set spinner visibility of program sets depending on logged in/out admin mode
        if(SettingsProvider.getInstance().isAdminModeUnlocked()) {
            //LOGGED into admin mode
            txt_program_set.setVisibility(View.VISIBLE);
            spinner_program_sets.setVisibility(View.VISIBLE);
        }else{
            //NOT LOGGED into admin mode
            txt_program_set.setVisibility(View.GONE);
            spinner_program_sets.setVisibility(View.GONE);
        }

        //mListener.showErrorDialog("Error on create",stateAlreadyExists.getMessage());

        setButtonListeners();

        checkCurrentState();

        restoreSelectedImplID(adapter);

        return view;
    }

    /**
     * If the app gets restarted the former Selected implementation ID gets restored and selected to the spinner adapter.
     * @param adapter - spinner adapter of implementation ids
     */
    private void restoreSelectedImplID(ArrayAdapter<String> adapter) {
        //Get former selected implementation ID
        String formerSelectedImplementationID = loadSelectedImplementationID();
        //Set former selected Implementation ID
        if (!formerSelectedImplementationID.equals(null)) {
            int spinnerPosition = adapter.getPosition(formerSelectedImplementationID);
            spinner_implementations.setSelection(spinnerPosition);
            if(spinner_implementations.getSelectedItem() != null){
                SettingsProvider.getInstance().selectedImplementationID = (String) spinner_implementations.getSelectedItem();
            }
        }
    }

    @Override
    public void onSettingsChanged(boolean settingsChanged) {
        //nothing todo here
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void showErrorDialog(String title,String message);
    }

    /**
     * Continuesly checking the current state of:
     *  Robot state (connected ?, configuration ? , running? )
     *  USB State (Connected to usb ?, Tethering actiaveted? );
     *
     *  Updateing the UI in respect of the current state.
     */
    private void checkCurrentState() {
        final Runnable taskUpdateButtonEnableState = new Runnable(){
            @Override
            public void run(){
                boolean positiveUSBState = USBService.isUSBConnected(parentActivity) && USBService.isTetheringActivated(parentActivity);

                btn_connect.setEnabled(!robot.isConnectedToBrick() && (positiveUSBState || SettingsProvider.getInstance().isSimulationEnabled()));

                btn_connect.setVisibility((!robot.isConnectedToBrick() && (positiveUSBState || SettingsProvider.getInstance().isSimulationEnabled())) ? View.VISIBLE : View.GONE);

                btn_disconnect.setVisibility(robot.isConnectedToBrick() ? View.VISIBLE : View.GONE);

                btn_startRobot.setEnabled(robot.isConnectedToBrick() && robot.isConfigurated() && !robot.isRunning && (positiveUSBState || SettingsProvider.getInstance().isSimulationEnabled()) && spinner_implementations.getSelectedItem() != null && !spinner_implementations.getSelectedItem().toString().isEmpty());

                btn_stopRobot.setEnabled(robot.isRunning);

                setEnableMenuItems(!robot.isConnectedToBrick());

                spinner_implementations.setEnabled(!robot.isRunning);

                btn_messengerConnDisconn.setEnabled(!robot.isRunning);

                switch_enableSimulation.setEnabled(!robot.isConnectedToBrick());

                if(!robot.isMessengerConnected()) {
                    btn_messengerConnDisconn.setText(txt_btn_DisConnect_connect);
                }else{
                    btn_messengerConnDisconn.setText(txt_btn_DisConnect_disconnect);
                }
                spinner_program_sets.setEnabled(!robot.isConnectedToBrick());

            }
        };

        final Runnable taskCheckUSBState = new Runnable() {
            @Override
            public void run() {
            StringBuffer sb = new StringBuffer();

            boolean isUSBConnected = false;
            boolean isTetheringActive = false;


            if(!(isUSBConnected = USBService.isUSBConnected(parentActivity))){
                sb.append(infoUsbNotFound);
            }

            if( (isUSBConnected && !(isTetheringActive = USBService.isTetheringActivated(parentActivity)))){
                sb.append(infoTetheringNotActiavted);
                btn_activateTethering.setVisibility(Button.VISIBLE);
            }else{
                btn_activateTethering.setVisibility(Button.GONE);
            }

            if(!isUSBConnected || !isTetheringActive){
                txt_info.setText(sb.toString());
                if(!SettingsProvider.getInstance().isSimulationEnabled()) {
                    layout_info.setVisibility(View.VISIBLE);
                }else{
                    layout_info.setVisibility(View.GONE);
                }

                //Stop robot
                if(robot.isRunning){
                    StopRobotTask task_stopRobot = new StopRobotTask("Robot",msgStopRobot);
                    task_stopRobot.execute();

                    DisconnectFromBrickTask task_disconnect = new DisconnectFromBrickTask("Robot",msgDisconnectFromRobot);
                    task_disconnect.execute();
                }
            }else{
                layout_info.setVisibility(FrameLayout.GONE);
            }

            if(SettingsProvider.getInstance().isSimulationEnabled()){
                btn_activateTethering.setVisibility(View.GONE);
            }
            }
        };

        final Runnable checkMessageClientConnectionState = new Runnable() {
            @Override
            public void run() {

                if(robot.isMessengerConnected()){
                    txtView_msgServerConnectionState.setText("Messenger: connected");
                    txtView_msgServerConnectionState.setBackgroundColor(Color.GREEN);
                }else{
                    txtView_msgServerConnectionState.setText("Messenger: disconnected");
                    txtView_msgServerConnectionState.setBackgroundColor(Color.RED);
                }

            }
        };


        //final Context cntxt_mainActivity = this;
        Runnable check = new Runnable() {
            @Override
            public void run() {
                while(!stopThreads) {

                    //Enable/disable control buttons (Connect to brick, init configuration, start robot,stop robot)
                    parentActivity.runOnUiThread(taskUpdateButtonEnableState);

                    //Update View of the current State
                    parentActivity.runOnUiThread(taskCheckUSBState);

                    //Checks the messenger connection state
                    parentActivity.runOnUiThread(checkMessageClientConnectionState);

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

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        stopThreads = true;
    }


    /**
     * Blocking Method. Waits until the given task is finished.
     * Set the status check delay by setting the millis.
     * @param task - task to wait for completion
     * @param millis - sleeptime should be >0
     */
    private void waitForTaskCompletion(final AsyncTask task, long millis){
        while(task.getStatus() != AsyncTask.Status.FINISHED){
            sleep(millis);
        }
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setButtonListeners() {
        switch_enableSimulation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsProvider.getInstance().setSimulationEnabled(isChecked);
            }
        });


        btn_messengerConnDisconn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(robot.isMessengerConnected()){
                    DisConnectMessengerTask msgClientTask = new DisConnectMessengerTask("Messenger","Messenger Client tries to connect");
                    msgClientTask.execute(MSG_TASK_DISCONNECT);
                }else{
                    DisConnectMessengerTask msgClientTask = new DisConnectMessengerTask("Messenger","Messenger Client tries to connect");
                    msgClientTask.execute(MSG_TASK_CONNECT);
                }

            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add internationalization to title
                ConnectInitBrickTask task = new ConnectInitBrickTask("Connecting",SettingsProvider.getInstance().getRobotConfigBundle(),getFragmentManager());
                task.execute();
            }
        });

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(robot.isRunning){
                    StopRobotTask task = new StopRobotTask("Stop robot",msgStopRobot);
                    task.execute();
                }

                DisconnectFromBrickTask task = new DisconnectFromBrickTask("Disconnecting robot",msgDisconnectFromRobot);
                task.execute();
            }
        });

        btn_startRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartRobotTask task = new StartRobotTask("Starting robot",msgStartRobot);
                task.execute();
            }
        });

        btn_stopRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopRobotTask task = new StopRobotTask("Stopping robot",msgStopRobot);
                task.execute();
            }
        });

        btn_activateTethering.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final ProgressDialog pd = ProgressDialog.newInstance(getResources().getString(R.string.txt_activate_tethering),"");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                }, ACTIVATE_TETHERING_TIMEOUT);
                ShellService.setTethering(true);

                pd.show(getFragmentManager(),"ACTIVATE_TETHERING_DIALOG");
            }
        });

        spinner_implementations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettingsProvider.getInstance().selectedImplementationID = (String) parent.getSelectedItem();
                saveSelectedImplementation((String) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SettingsProvider.getInstance().selectedImplementationID = "";
            }
        });

        spinner_program_sets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettingsProvider.getInstance().setSelectedProgramSet((String) parent.getSelectedItem());
                spinner_implementations.setAdapter(getImplementationIDAdapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SettingsProvider.getInstance().setSelectedProgramSet(ImplementationService.getInstance().getDefaultSet());
                spinner_implementations.setAdapter(getImplementationIDAdapter());
            }
        });
    }

    public ArrayAdapter<String> getImplementationIDAdapter() {
        return new ArrayAdapter<String>(parentActivity, android.R.layout.simple_spinner_dropdown_item, ImplementationService.getInstance().getImplementationIDs(SettingsProvider.getInstance().getSelectedProgramSet()));

    }

    public void setEnableMenuItems(boolean enable){
        ListView mDrawerListView;
        if((mDrawerListView = parentActivity.getMenuItemListView()) != null) {
            for(int i = 0; i < mDrawerListView.getChildCount(); i++){
                if(!menuItemAlwaysEnabled.get(i)) {
                    mDrawerListView.getChildAt(i).setEnabled(enable);
                }
            }
        }
    }

    /**
     * Saves the selected ImplementationID in preferences to reload the selected one, when restarting the app.
     * @param selectedItem - implementationID to save
     */
    public void saveSelectedImplementation(String selectedItem) {
        SharedPreferences.Editor e = parentActivity.getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_app_config_data), Context.MODE_PRIVATE).edit();
        e.clear();

        /** Selected Implementation ID **/
        e.putString(getResources().getString(R.string.KEY_SELECTED_IMPLEMENTATION),selectedItem);

        e.commit();
    }

    public String loadSelectedImplementationID(){
        SharedPreferences connectionProperties = parentActivity.getSharedPreferences(getResources().getString(R.string.shared_pref_app_config_data), Context.MODE_PRIVATE);
        return connectionProperties.getString(getResources().getString(R.string.KEY_SELECTED_IMPLEMENTATION),"-");
    }

    //--- Async Tasks

    private class DisconnectFromBrickTask extends ProgressTask{

        public DisconnectFromBrickTask(String title,String progressMsg) {
            super(getFragmentManager(),title,progressMsg);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                robot.disconnectFromBrick();
                result = robot.isConnectedToBrick();
            } catch (Exception e){
                e.printStackTrace();
                parentActivity.showErrorDialog("Exception",e.getMessage());
            }
            return result;
        }
    }

    private class StartRobotTask extends SessionProgressTask{

        public StartRobotTask(String title, String progressMsg) {
            super(title,new Bundle(),getFragmentManager());
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return startRobot();
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            robot.isRunning = result;
        }

        private boolean startRobot(){
            robot.startExecuteImplementation(SettingsProvider.getInstance().selectedImplementationID);
            do {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (!SessionStateObserver.getInstance().isSessionComplete() || !isInterrupted);
            //Finish execution when the Session is Full and gets started, or the cancel button of the dialog is pressed (isInterrutped)
            //-> with ending this method the dialog will be disposed onPostExecute(..)
            if(isInterrupted){
                //Stop execution of implementation
                robot.stopRunningImplmentation();
                return false;
            }

            return true;
        }
    }

    private class StopRobotTask extends ProgressTask{

        public StopRobotTask(String title, String progressMsg) {
            super(getFragmentManager(), title, progressMsg);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            robot.stopRunningImplmentation();
            return false;
        }
    }

    private class DisConnectMessengerTask extends ProgressTask{

        public DisConnectMessengerTask(String title, String progressMsg) {
            super(getFragmentManager(),title,progressMsg);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length == 0){
                return false;
            }

            switch(params[0]){
                case MSG_TASK_CONNECT:
                    if(!robot.isMessengerConnected()) {
                        LOGGER.log(Level.INFO,"Connecting to Message Server");
                        robot.connectMessenger(SettingsProvider.getInstance().getMsgServerIP(),SettingsProvider.getInstance().getMsgServerPort());
                    } break;
                case MSG_TASK_DISCONNECT:
                    if(robot.isMessengerConnected()) {
                        LOGGER.log(Level.INFO,"Disconnecting to Message Server");
                        robot.disconnectMessenger();
                    } break;
            }
            return robot.isMessengerConnected();
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }


    private class ConnectInitBrickTask extends ConnectionProgressTask{

        public ConnectInitBrickTask(String title, Bundle configBundle, FragmentManager fManager) {
            super(title, configBundle, fManager);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            //Creates the Robot
            //TODO Try to call the createRobot method somewhere else, and only create a Robot if the Configuration/Settings have changed.
            boolean createRobot = createRobot();

            //Messenger Client Task - messenger connects to messageserver
            boolean isMessengerConnected = connectMessenger();
            setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_MESSENGER,isMessengerConnected);

            //Creates and executes the Task to connect to the Brick
            boolean connectedToBrick = connectToBrick();
            setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_BRICK,connectedToBrick);

            if(robot.isConnectedToBrick()) {
                boolean isConfigurationInitialized = initConfiguration();
                return createRobot && isMessengerConnected && connectedToBrick && isConfigurationInitialized;
            }else{
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P1, false);
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P2, false);
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P3, false);
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_SEN_P4, false);
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_MOT_A, false);
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_MOT_B, false);
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_MOT_C, false);
                setProgressState(ConnectionProgressDialogFragment.KEY_PARAM_MOT_D, false);
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //How to interpret the values -> [messenger,
        }

        /**
         * Creates a robot and will always return true;
         * @return true
         */
        private boolean createRobot(){
            //Create Robot
            robot.create();
            return true;
        }

        /**
         * Connects to the messenger, when connection is not established yet
         * @return connection state
         */
        private boolean connectMessenger(){
            if(!robot.isMessengerConnected()) {
                robot.connectMessenger(SettingsProvider.getInstance().getMsgServerIP(),SettingsProvider.getInstance().getMsgServerPort());
            }
            return robot.isMessengerConnected();
        }

        /**
         * Connects to the brick.
         * @return connection state
         */
        private boolean connectToBrick(){
            boolean result = false;
            try {
                LOGGER.log(Level.INFO,"Connecting to EV3-Brick");
                robot.connectToBrick();
                result = robot.isConnectedToBrick();
            } catch (IOException e){
                e.printStackTrace();
                parentActivity.showErrorDialog("Exception",msgConnectToRobotError);
            }
            return result;
        }

        /**
         * Initializes the sensors and motors at the brick.
         * @return true if initialization was successful
         */
        private boolean initConfiguration(){
            boolean result = false;
            try{
                LOGGER.log(Level.INFO,"Initializing Robot sensors/motors");
                result = robot.initializeConfiguration();
            }catch(Exception e){
                parentActivity.showErrorDialog("Exception",e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
    }

}
