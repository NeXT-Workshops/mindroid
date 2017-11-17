package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.view.*;
import android.widget.*;

import org.mindroid.android.app.R;
import org.mindroid.android.app.acitivites.IErrorHandler;
import org.mindroid.android.app.acitivites.MainActivity;
import org.mindroid.android.app.asynctasks.ProgressTask;
import org.mindroid.android.app.fragments.settings.SettingsFragment;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.android.app.serviceloader.ImperativeImplService;
import org.mindroid.android.app.serviceloader.StatemachineService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private final String MSG_TASK_PARAM_0_CONNECT = "connect";
    private final String MSG_TASK_PARAM_0_DISCONNECT = "disconnectFromBrick";

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


    private Spinner spinner_selectedImplementation;

    /** StartStop-Robot-Task cmd **/
    private final String START_ROBOT = "start";
    private final String STOP_ROBOT ="stop";

    public static Robot robot;

    MainActivity parentActivity;

    // Resources
    private String msgConnectToRobot;
    private String msgConnectToRobotError;
    private String msgDisconnectFromRobot;
    private String msgInitConfiguration;
    private String msgStartRobot;
    private String msgStopRobot;
    private String infoUsbNotFound;
    private String infoTetheringNotActiavted;
    private String txt_btn_DisConnect_connect;
    private String txt_btn_DisConnect_disconnect;

    private HashMap<Integer,Boolean> menuItemAlwaysEnabled = new HashMap<>();

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        robot = MainActivity.robot;
        parentActivity = (MainActivity) getActivity();


        if(parentActivity instanceof IErrorHandler){
            robot.registerErrorHandler(((IErrorHandler) parentActivity).getErrorHandler());
        }

        getMenuEnabledSettings();
    }

    /**
     *
     */
    private void getMenuEnabledSettings() {
        //SettingsProvider menu enable
        menuItemAlwaysEnabled.put(0,true);
        menuItemAlwaysEnabled.put(1,true);
        menuItemAlwaysEnabled.put(2,false);
        menuItemAlwaysEnabled.put(3,false);
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
        txt_btn_DisConnect_disconnect = getResources().getString(R.string.txt_btn_DisConnect_disconnect);;
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
        spinner_selectedImplementation = (Spinner) view.findViewById(R.id.spinner_selectedStatemachine);
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
        transaction.replace(R.id.container_robotSetupInfo, RobotSetupInfoFragment.newInstance("",""));
        //transaction.addToBackStack(null);
        transaction.commit();

        ArrayAdapter<String> adapter = getImplementationIDAdapter();
        spinner_selectedImplementation.setAdapter(adapter);


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
            spinner_selectedImplementation.setSelection(spinnerPosition);
            if(spinner_selectedImplementation.getSelectedItem() != null){
                SettingsProvider.getInstance().selectedImplementationID = (String) spinner_selectedImplementation.getSelectedItem();
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
                boolean positiveUSBState = isUSBConnected(parentActivity) && isTetheringActivated(parentActivity);

                btn_connect.setEnabled(!robot.isConnectedToBrick() && (positiveUSBState || SettingsProvider.getInstance().isSimulationEnabled()));

                btn_connect.setVisibility((!robot.isConnectedToBrick() && (positiveUSBState || SettingsProvider.getInstance().isSimulationEnabled())) ? View.VISIBLE : View.GONE);

                btn_disconnect.setVisibility(robot.isConnectedToBrick() ? View.VISIBLE : View.GONE);

                btn_startRobot.setEnabled(robot.isConnectedToBrick() && robot.isConfigurated() && !robot.isRunning && (positiveUSBState || SettingsProvider.getInstance().isSimulationEnabled()) && spinner_selectedImplementation.getSelectedItem() != null && !spinner_selectedImplementation.getSelectedItem().toString().isEmpty());

                btn_stopRobot.setEnabled(robot.isRunning);

                setEnableMenuItems(!robot.isConnectedToBrick());

                spinner_selectedImplementation.setEnabled(!robot.isRunning);

                btn_messengerConnDisconn.setEnabled(!robot.isRunning);

                switch_enableSimulation.setEnabled(!robot.isConnectedToBrick());

                if(!robot.isMessengerConnected()) {
                    btn_messengerConnDisconn.setText(txt_btn_DisConnect_connect);
                }else{
                    btn_messengerConnDisconn.setText(txt_btn_DisConnect_disconnect);
                }
            }
        };

        final Runnable taskCheckUSBState = new Runnable() {
            @Override
            public void run() {
            StringBuffer sb = new StringBuffer();

            boolean isUSBConnected = false;
            boolean isTetheringActive = false;


            if(!(isUSBConnected = isUSBConnected(parentActivity))){
                sb.append(infoUsbNotFound);
            }

            if( (isUSBConnected && !(isTetheringActive = isTetheringActivated(parentActivity)))){
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
                    StartStopRobotTask task_stopRobot = new StartStopRobotTask("Robot",msgStartRobot);
                    task_stopRobot.execute(STOP_ROBOT);

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
                while(true) {

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

    /**
     * Blocking Method. Waits until the given task is finished.
     * Set the status check delay by setting the millis.
     * @param task - task to wait for completion
     * @param millis - sleeptime should be >0
     */
    private void waitForTaskCompletion(AsyncTask task, long millis){
        while(task.getStatus() == AsyncTask.Status.FINISHED){
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
                    msgClientTask.execute(MSG_TASK_PARAM_0_DISCONNECT);
                }else{
                    DisConnectMessengerTask msgClientTask = new DisConnectMessengerTask("Messenger","Messenger Client tries to connect");
                    msgClientTask.execute(MSG_TASK_PARAM_0_CONNECT);
                }

            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates the Robot
                /*CreateRobotTask createRobotTask = new CreateRobotTask("Creating Robot","Creating your Robot");
                createRobotTask.execute();

                waitForTaskCompletion(createRobotTask,20);

                //Messenger Client Task - messenger connects to messageserver
                DisConnectMessengerTask msgClientTask = new DisConnectMessengerTask("Disconnect from Brick","Messenger Client tries to connect");
                msgClientTask.execute(MSG_TASK_PARAM_0_CONNECT);

                waitForTaskCompletion(msgClientTask,20);

                //Creates and executes the Task to connect to the Brick
                ConnectToBrickTask task = new ConnectToBrickTask("Connecting to Brick",msgConnectToRobot);
                task.execute(); //String is not important
                */
                ConnectInitBrickTask task = new ConnectInitBrickTask("Initializing",SettingsProvider.getInstance().getRobotConfigBundle(),getFragmentManager());
                task.execute();
            }
        });

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(robot.isRunning){
                    StartStopRobotTask task = new StartStopRobotTask("Start/Stop robot",msgStopRobot);
                    task.execute(STOP_ROBOT);
                }

                DisconnectFromBrickTask task = new DisconnectFromBrickTask("Disconnecting robot",msgDisconnectFromRobot);
                task.execute();
            }
        });

        btn_startRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartStopRobotTask task = new StartStopRobotTask("Starting robot",msgStartRobot);
                task.execute(START_ROBOT);
            }
        });

        btn_stopRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartStopRobotTask task = new StartStopRobotTask("Stopping robot",msgStopRobot);
                task.execute(STOP_ROBOT);
            }
        });

        btn_activateTethering.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
            }
        });

        spinner_selectedImplementation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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
    }

    public ArrayAdapter<String> getImplementationIDAdapter() {
        //Add Statemachine ids to Dropdown-ui
        ArrayList<String> imperativeImplIDs = ImperativeImplService.getInstance().getImperativeImplIDs();
        ArrayList<String>  statemachineIDs = StatemachineService.getInstance().getStatemachineCollectionIDs();

        ArrayList<String> collectedIDs = new ArrayList<String>(imperativeImplIDs.size()+statemachineIDs.size());

        for (String imperativeImplID : imperativeImplIDs) {
            if(imperativeImplID!= null){
                collectedIDs.add(imperativeImplID);
            }
        }

        for (String statemachineID : statemachineIDs) {
            if(statemachineID != null){
                collectedIDs.add(statemachineID);
            }
        }

        String[] allIDs = collectedIDs.toArray(new String[collectedIDs.size()]);

        return new ArrayAdapter<String>(parentActivity, android.R.layout.simple_spinner_dropdown_item, allIDs);

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

    private class StartStopRobotTask extends ProgressTask{

        public StartStopRobotTask(String title,String progressMsg) {
            super(getFragmentManager(),title,progressMsg);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length>0) {
                if (params[0].equals(START_ROBOT)) { //True => Start robot, else it should stop the Robot
                    try {
                        robot.startExecuteImplementation(SettingsProvider.getInstance().selectedImplementationID);

                        return true;
                    }catch(Exception e){
                        parentActivity.showErrorDialog("Exception",e.getMessage());
                        e.printStackTrace();
                    }
                } else if(params[0].equals(STOP_ROBOT)) {
                    try {
                        robot.stopRunningImplmentation();
                        return false;
                    }catch(Exception e){
                        parentActivity.showErrorDialog("Exception",e.getMessage());
                        e.printStackTrace();
                    }
                }
            }else{
                return false;
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            robot.isRunning = result;
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
            if(params.length > 0){
                if(params[0].equals(MSG_TASK_PARAM_0_CONNECT)){
                    if(!robot.isMessengerConnected()) {
                        robot.connectMessenger(SettingsProvider.getInstance().getMsgServerIP(),SettingsProvider.getInstance().getMsgServerPort());
                    }
                }else if(params[0].equals(MSG_TASK_PARAM_0_DISCONNECT)){
                    if(robot.isMessengerConnected()) {
                        robot.disconnectMessenger();
                    }
                }
            }

            return robot.isMessengerConnected();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }


    private class ConnectInitBrickTask extends ConnectionProgressTask{

        public ConnectInitBrickTask(String title, Bundle args, FragmentManager fManager) {
            super(title, args, fManager);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //Creates the Robot
            CreateRobotTask createRobotTask = new CreateRobotTask(); //TODO Todo set text somewhere else: "Creating Robot","Creating your Robot"
            createRobotTask.execute();
            waitForTaskCompletion(createRobotTask,20);

            //Messenger Client Task - messenger connects to messageserver
            DisConnectMessengerTask msgClientTask = new DisConnectMessengerTask("Disconnect from Brick","Messenger Client tries to connect");
            msgClientTask.execute(MSG_TASK_PARAM_0_CONNECT);
            waitForTaskCompletion(msgClientTask,20);
            setProgressState(ConnectionProgressFragment.KEY_PARAM_MESSENGER,robot.isMessengerConnected());


            //Creates and executes the Task to connect to the Brick
            ConnectToBrickTask connectToBrickTask = new ConnectToBrickTask(); //Todo set text somewhere else: "Connecting to Brick",msgConnectToRobot
            connectToBrickTask.execute(); //String is not important
            waitForTaskCompletion(connectToBrickTask,20);
            setProgressState(ConnectionProgressFragment.KEY_PARAM_BRICK,robot.isConnectedToBrick());

            if(robot.isConnectedToBrick()) {
                InitConfiguration initConfigTask = new InitConfiguration(); //Todo set text somewhere else: "Init Configuration", msgInitConfiguration
                initConfigTask.execute();
                waitForTaskCompletion(initConfigTask, 20);
                //TODO set sensor/motor progress states
            }else{
                setProgressState(ConnectionProgressFragment.KEY_PARAM_SEN_P1, false);
                setProgressState(ConnectionProgressFragment.KEY_PARAM_SEN_P2, false);
                setProgressState(ConnectionProgressFragment.KEY_PARAM_SEN_P3, false);
                setProgressState(ConnectionProgressFragment.KEY_PARAM_SEN_P4, false);
                setProgressState(ConnectionProgressFragment.KEY_PARAM_MOT_A, false);
                setProgressState(ConnectionProgressFragment.KEY_PARAM_MOT_B, false);
                setProgressState(ConnectionProgressFragment.KEY_PARAM_MOT_C, false);
                setProgressState(ConnectionProgressFragment.KEY_PARAM_MOT_D, false);
            }

            return true;
        }

        private class CreateRobotTask extends AsyncTask<String,Integer,Boolean>{
            public CreateRobotTask() {
                super();
            }
            @Override
            protected Boolean doInBackground(String... params) {
                //Create Robot
                robot.create();
                return true;
            }
        }

        private class ConnectToBrickTask extends AsyncTask<String,Integer,Boolean>{
            public ConnectToBrickTask() {
                super();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                boolean result = false;
                try {
                    robot.connectToBrick();
                    result = robot.isConnectedToBrick();
                } catch (IOException e){
                    e.printStackTrace();
                    parentActivity.showErrorDialog("Exception",msgConnectToRobotError);
                }
                return result;
            }
        }

        private class InitConfiguration extends AsyncTask<String,Integer,Boolean>{
            public InitConfiguration() {
                super();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                boolean result = false;

                try{
                    result = robot.initializeConfiguration();;
                }catch(Exception e){
                    System.out.println("## AsyncTask initRobotConfig. Exception: "+e);
                    parentActivity.showErrorDialog("Exception",e.getMessage());
                    e.printStackTrace();
                }
                return result;
            }
        }
    }
}
