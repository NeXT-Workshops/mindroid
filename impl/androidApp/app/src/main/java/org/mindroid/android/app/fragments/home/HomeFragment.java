package org.mindroid.android.app.fragments.home;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.mindroid.android.app.R;
import org.mindroid.android.app.asynctasks.ProgressTask;
import org.mindroid.android.app.fragments.myrobot.HardwareSelectionFragment;
import org.mindroid.android.app.fragments.settings.SettingsFragment;
import org.mindroid.android.app.robodancer.Robot;
import org.mindroid.android.app.robodancer.Settings;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;

import java.io.IOException;

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
    private Button btn_startRobot;
    private Button btn_stopRobot;

    private Spinner spinner_selectedStatemachine;

    /** Information Box **/
    private FrameLayout layout_info;
    private TextView txt_info;
    private Button btn_activateTethering;

    /** StartStop-Robot-Task cmd **/
    private final String START_ROBOT = "start";
    private final String STOP_ROBOT ="stop";

    public static Robot robot = new Robot();;

    Activity parentActivity;

    // Resources
    String msgConnectToRobot;
    String msgInitConfiguration;
    String msgStartRobot;
    String msgStopRobot;

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

        parentActivity = getActivity();

        loadRobotPortConfiguration();
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
        msgConnectToRobot = getResources().getString(R.string.msg_connect_to_robot);
        msgInitConfiguration = getResources().getString(R.string.msg_init_config);
        msgStartRobot = getResources().getString(R.string.msg_start_robot);
        msgStopRobot = getResources().getString(R.string.msg_stop_robot);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        /** Instantiate buttons and textviews **/

            btn_initConfiguration = (Button) view.findViewById(R.id.btn_initConfig);
            btn_connect = (Button) view.findViewById(R.id.btn_connect);
            spinner_selectedStatemachine = (Spinner) view.findViewById(R.id.spinner_selectedStatemachine);
            btn_startRobot = (Button) view.findViewById(R.id.btn_startRobot);
            btn_stopRobot = (Button) view.findViewById(R.id.btn_stopRobot);

            /** Information Box **/
            layout_info = (FrameLayout) view.findViewById(R.id.layout_infobox);
            txt_info = (TextView) view.findViewById(R.id.txt_info);
            btn_activateTethering = (Button) view.findViewById(R.id.btn_activateTethering);

            //Add RobotSetupInfo Fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_robotSetupInfo, RobotSetupInfoFragment.newInstance("",""));
            //transaction.addToBackStack(null);
            transaction.commit();

        try {
            robot.makeRobot();
            spinner_selectedStatemachine.setAdapter(getStatemachineIDAdapter());
        } catch (StateAlreadyExists stateAlreadyExists) {
            stateAlreadyExists.printStackTrace();
            //showAlertDialog("Error on create",stateAlreadyExists.getMessage());
        }


        /** Load Connection Properties **/
        //loadConnectionProperties();


        setButtonListeners();

        checkCurrentState();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSettingsChanged(boolean settingsChanged) {
        //nothind todo
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
        void onFragmentInteraction(Uri uri);
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

                btn_connect.setEnabled(!robot.isConnectedToBrick && positiveUSBState);

                btn_initConfiguration.setEnabled(robot.isConnectedToBrick && !robot.isConfigurationBuilt && positiveUSBState);

                btn_startRobot.setEnabled(robot.isConnectedToBrick && robot.isConfigurationBuilt && !robot.isRunning && positiveUSBState && !spinner_selectedStatemachine.getSelectedItem().toString().isEmpty());

                btn_stopRobot.setEnabled(robot.isRunning);

                spinner_selectedStatemachine.setEnabled(!robot.isRunning);



            }
        };

        final Runnable taskCheckUSBState = new Runnable() {
            @Override
            public void run() {
                StringBuffer sb = new StringBuffer();

                boolean isUSBConnected = false;
                boolean isTetheringActive = false;

                if(!(isUSBConnected = isUSBConnected(parentActivity))){
                    sb.append("USB Kabel ist nicht eingesteckt. Prüfe die USB-Verbindung!");
                }

                if(isUSBConnected && !(isTetheringActive = isTetheringActivated(parentActivity))){
                    sb.append("Tethering ist nicht aktiviert. Aktiviere Tethering, um eine Verbindung zum Brick herzustellen!");
                    btn_activateTethering.setVisibility(Button.VISIBLE);
                }else{
                    btn_activateTethering.setVisibility(Button.GONE);
                }

                if(!isUSBConnected || !isTetheringActive){
                    txt_info.setText(sb.toString());
                    layout_info.setVisibility(FrameLayout.VISIBLE);

                    //Stop robot
                    if(robot.isRunning){
                        robot.stop();
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

                    //TODO remove those lines
                    //System.out.println("Usb device is rndis -> " + getUSBInfo(cntxt_mainActivity));
                    //txt_ipPhone.setText(getUSBInfo(cntxt_mainActivity));

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
                try {
                    robot.makeRobot();
                } catch (StateAlreadyExists stateAlreadyExists) {
                    stateAlreadyExists.printStackTrace();
                }
                ConnectToBrickTask task = new ConnectToBrickTask(parentActivity,msgConnectToRobot);
                task.execute(); //String is not important
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

        spinner_selectedStatemachine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.getInstance().selectedStatemachineID = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Settings.getInstance().selectedStatemachineID = "";
            }
        });
    }

    public ArrayAdapter<String> getStatemachineIDAdapter() {
        //Add Statemachine ids to Dropdown-ui
        return new ArrayAdapter<String>(parentActivity, android.R.layout.simple_spinner_dropdown_item, robot.getStatemachineIDs());

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
            }
            robot.isConnectedToBrick = result;
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
                //TODO main_activity.dismissCurrentProgressDialog();
                //TODO main_activity.showAlertDialog("Error",""+e);
                e.printStackTrace();
            }
            robot.isConfigurationBuilt = result;
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
                        robot.startStatemachine(Settings.getInstance().selectedStatemachineID);
                        return true;
                    }catch(Exception e){
                        System.out.println("## AsyncTask StartStopRobot. Exception: "+e);
                        //TODO main_activity.dismissCurrentProgressDialog();
                        //TODO  main_activity.showAlertDialog("Error",""+e);
                        e.printStackTrace();
                    }
                } else if(params[0].equals(STOP_ROBOT)) {
                    try {
                        robot.stopStatemachine(Settings.getInstance().selectedStatemachineID);
                        return false;
                    }catch(Exception e){
                        System.out.println("## AsyncTask StartStopRobot. Exception: "+e);
                        //TODO main_activity.dismissCurrentProgressDialog();
                        //TODO main_activity.showAlertDialog("Error",""+e);
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

    private void loadRobotPortConfiguration() {
        SharedPreferences portConfigProperties = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_portConfiguration),Context.MODE_PRIVATE);
        if(portConfigProperties != null) {
            // ---- load sensortypes ---- //
            String type ="";
            String sensormode="";


            //--SENSORS--
            //Sensor - S1
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S1), HardwareSelectionFragment.notDefined);
            sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S1), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setSensorS1(HardwareSelectionFragment.getSensorType(type));
            robot.getRobotPortConfig().setSensormodeS1(HardwareSelectionFragment.getSensorMode(sensormode));

            //Sensor - S2
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S2), HardwareSelectionFragment.notDefined);
            sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S2), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setSensorS2(HardwareSelectionFragment.getSensorType(type));
            robot.getRobotPortConfig().setSensormodeS2(HardwareSelectionFragment.getSensorMode(sensormode));

            //Sensor - S3
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S3), HardwareSelectionFragment.notDefined);
            sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S3), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setSensorS3(HardwareSelectionFragment.getSensorType(type));
            robot.getRobotPortConfig().setSensormodeS3(HardwareSelectionFragment.getSensorMode(sensormode));

            //Sensor - S4
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S4), HardwareSelectionFragment.notDefined);
            sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S4), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setSensorS4(HardwareSelectionFragment.getSensorType(type));
            robot.getRobotPortConfig().setSensormodeS4(HardwareSelectionFragment.getSensorMode(sensormode));

            //--MOTORS--
            //Motor A
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_A), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setMotorA(HardwareSelectionFragment.getMotorType(type));

            //Motor B
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_B), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setMotorB(HardwareSelectionFragment.getMotorType(type));

            //Motor C
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_C), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setMotorC(HardwareSelectionFragment.getMotorType(type));

            //Motor D
            type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_D), HardwareSelectionFragment.notDefined);
            robot.getRobotPortConfig().setMotorD(HardwareSelectionFragment.getMotorType(type));
            }
        }
}
