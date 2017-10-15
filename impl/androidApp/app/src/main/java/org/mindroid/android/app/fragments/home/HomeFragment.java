package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //--- Buttons etc --//
    private Button btn_initConfiguration;
    private Button btn_connect;
    private Button btn_disconnect;
    private Button btn_startRobot;
    private Button btn_stopRobot;


    private Spinner spinner_selectedImplementation;

    /** Information Box **/
    private FrameLayout layout_info;
    private TextView txt_info;
    private Button btn_activateTethering;

    /** StartStop-Robot-Task cmd **/
    private final String START_ROBOT = "start";
    private final String STOP_ROBOT ="stop";

    public static Robot robot = new Robot();;

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

    private HashMap<Integer,Boolean> menuItemAlwaysEnabled = new HashMap<>();

    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        //Get Resources
        msgConnectToRobot = getResources().getString(R.string.dialog_msg_connect_to_robot);
        msgConnectToRobotError = getResources().getString(R.string.dialog_msg_connect_to_robot_error);
        msgDisconnectFromRobot = getResources().getString(R.string.dialog_msg_disconnect_from_robot);
        msgInitConfiguration = getResources().getString(R.string.dialog_msg_init_config);
        msgStartRobot = getResources().getString(R.string.dialog_msg_start_robot);
        msgStopRobot = getResources().getString(R.string.dialog_msg_stop_robot);
        infoTetheringNotActiavted = getResources().getString(R.string.info_txt_msg_activate_tetherting);
        infoUsbNotFound = getResources().getString(R.string.info_txt_msg_activate_usb_not_found);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        /** Instantiate buttons and textviews **/

        btn_initConfiguration = (Button) view.findViewById(R.id.btn_initConfig);
        btn_connect = (Button) view.findViewById(R.id.btn_connect);
        btn_disconnect = (Button) view.findViewById(R.id.btn_disconnect);
        spinner_selectedImplementation = (Spinner) view.findViewById(R.id.spinner_selectedStatemachine);
        btn_startRobot = (Button) view.findViewById(R.id.btn_startRobot);
        btn_stopRobot = (Button) view.findViewById(R.id.btn_stopRobot);

        /** ActivateTethering-Information Box **/
        layout_info = (FrameLayout) view.findViewById(R.id.layout_infobox);
        txt_info = (TextView) view.findViewById(R.id.txt_info);
        btn_activateTethering = (Button) view.findViewById(R.id.btn_activateTethering);

        btn_activateTethering.setText(getResources().getString(R.string.btn_text_activate_tethering));
        btn_connect.setText(getResources().getString(R.string.btn_text_connect));
        btn_disconnect.setText(getResources().getString(R.string.btn_text_disconnect));
        btn_disconnect.setVisibility(View.GONE);
        btn_initConfiguration.setText(getResources().getString(R.string.btn_text_init_config));
        btn_startRobot.setText(getResources().getString(R.string.btn_text_start_robot));
        btn_stopRobot.setText(getResources().getString(R.string.btn_text_stop_robot));


        //Add RobotSetupInfo Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_robotSetupInfo, RobotSetupInfoFragment.newInstance("",""));
        //transaction.addToBackStack(null);
        transaction.commit();

        spinner_selectedImplementation.setAdapter(getImplementationIDAdapter());

        //mListener.showErrorDialog("Error on create",stateAlreadyExists.getMessage());

        setButtonListeners();

        checkCurrentState();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        // TODO: Update argument type and name
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

                btn_connect.setEnabled(!robot.isConnected() && positiveUSBState);

                btn_connect.setVisibility((!robot.isConnected() && positiveUSBState) ? View.VISIBLE : View.GONE);

                btn_disconnect.setVisibility(robot.isConnected() ? View.VISIBLE : View.GONE);

                btn_initConfiguration.setEnabled(robot.isConnected() && !robot.isConfigurated() && positiveUSBState);

                btn_startRobot.setEnabled(robot.isConnected() && robot.isConfigurated() && !robot.isRunning && positiveUSBState && spinner_selectedImplementation.getSelectedItem() != null && !spinner_selectedImplementation.getSelectedItem().toString().isEmpty());

                btn_stopRobot.setEnabled(robot.isRunning);

                setEnableMenuItems(!robot.isConnected());

                spinner_selectedImplementation.setEnabled(!robot.isRunning);
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

                if(isUSBConnected && !(isTetheringActive = isTetheringActivated(parentActivity))){
                    sb.append(infoTetheringNotActiavted);
                    btn_activateTethering.setVisibility(Button.VISIBLE);
                }else{
                    btn_activateTethering.setVisibility(Button.GONE);
                }

                if(!isUSBConnected || !isTetheringActive){
                    txt_info.setText(sb.toString());
                    layout_info.setVisibility(FrameLayout.VISIBLE);

                    //Stop robot
                    if(robot.isRunning){
                        StartStopRobotTask task_stopRobot = new StartStopRobotTask(parentActivity,msgStartRobot);
                        task_stopRobot.execute(STOP_ROBOT);

                        DisconnectFromBrickTask task_disconnect = new DisconnectFromBrickTask(parentActivity,msgDisconnectFromRobot);
                        task_disconnect.execute();
                    }
                }else{
                    layout_info.setVisibility(FrameLayout.GONE);
                }
            }
        };



        //final Context cntxt_mainActivity = this;
        Runnable check = new Runnable() {
            @Override
            public void run() {
                while(true) {//TODO remove ?

                    //Enable/disable control buttons (Connect to brick, init configuration, start robot,stop robot)
                    parentActivity.runOnUiThread(taskUpdateButtonEnableState);

                    //Update View of the current State
                    parentActivity.runOnUiThread(taskCheckUSBState);

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

    private void setButtonListeners() {

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates the Robot
                robot.create();
                //Creates and executes the Task to connect to the Brick
                ConnectToBrickTask task = new ConnectToBrickTask(parentActivity,msgConnectToRobot);
                task.execute(); //String is not important
            }
        });

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(robot.isRunning){
                    StartStopRobotTask task = new StartStopRobotTask(parentActivity,msgStopRobot);
                    task.execute(STOP_ROBOT);
                }

                DisconnectFromBrickTask task = new DisconnectFromBrickTask(parentActivity,msgDisconnectFromRobot);
                task.execute();
            }
        });

        btn_initConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitConfiguration task = new InitConfiguration(parentActivity,msgInitConfiguration);
                task.execute();
            }
        });

        btn_startRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartStopRobotTask task = new StartStopRobotTask(parentActivity,msgStartRobot);
                task.execute(START_ROBOT);
            }
        });

        btn_stopRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartStopRobotTask task = new StartStopRobotTask(parentActivity,msgStopRobot);
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

    //TODO Does not work as intended
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

    //--- Async Tasks

    private class ConnectToBrickTask extends ProgressTask{

        public ConnectToBrickTask(Context context,String progressMsg) {
            super(context,progressMsg);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                robot.connectToBrick();
                result = robot.isConnected();
            } catch (IOException e){
                e.printStackTrace();
                mListener.showErrorDialog("Exception",msgConnectToRobotError);
            }
            return result;
        }
    }

    private class DisconnectFromBrickTask extends ProgressTask{

        public DisconnectFromBrickTask(Context context,String progressMsg) {
            super(context,progressMsg);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                robot.disconnect();
                result = robot.isConnected();
            } catch (Exception e){
                e.printStackTrace();
                mListener.showErrorDialog("Exception",e.getMessage());
            }
            return result;
        }
    }

    private class InitConfiguration extends ProgressTask{

        public InitConfiguration(Context context,String progressMsg) {
            super(context,progressMsg);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;

            try{
                result = robot.initializeConfiguration();;
            }catch(Exception e){
                System.out.println("## AsyncTask initRobotConfig. Exception: "+e);
                mListener.showErrorDialog("Exception",e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
    }


    private class StartStopRobotTask extends ProgressTask{

        public StartStopRobotTask(Context context,String progressMsg) {
            super(context,progressMsg);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length>0) {
                if (params[0].equals(START_ROBOT)) { //True => Start robot, else it should stop the Robot
                    try {
                        robot.startExecuteImplementation(SettingsProvider.getInstance().selectedImplementationID);
                        return true;
                    }catch(Exception e){
                        System.out.println("## AsyncTask StartStopRobot. Exception: "+e);
                        mListener.showErrorDialog("Exception",e.getMessage());
                        e.printStackTrace();
                    }
                } else if(params[0].equals(STOP_ROBOT)) {
                    try {
                        robot.stopRunningImplmentation();
                        return false;
                    }catch(Exception e){
                        System.out.println("## AsyncTask StartStopRobot. Exception: "+e);
                        mListener.showErrorDialog("Exception",e.getMessage());
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

}
