package org.mindroid.android.app.fragments.myrobot;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.mindroid.android.app.R;
import org.mindroid.android.app.robodancer.RobotConfigurationChangedListener;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.Sensors;

import java.util.HashMap;

import static org.mindroid.android.app.fragments.home.HomeFragment.robot;
import static org.mindroid.common.messages.Sensors.EV3ColorSensor;
import static org.mindroid.common.messages.Sensors.EV3GyroSensor;
import static org.mindroid.common.messages.Sensors.EV3IRSensor;
import static org.mindroid.common.messages.Sensors.EV3TouchSensor;
import static org.mindroid.common.messages.Sensors.EV3UltrasonicSensor;

/**
 *
 */
public class HardwareSelectionFragment extends Fragment implements MyRobotFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_HARDWARE_SELECTION_MODE = "org.mindroid.android.app.fragments.myrobot.HardwareSelectionFragment.hardwaretype";
    private static final String KEY_HARDWARE_PORT = "org.mindroid.android.app.fragments.myrobot.HardwareSelectionFragment.hardwareport";

    private RobotConfigurationChangedListener robotConfigurationChangedListener = SettingsProvider.getInstance();

    // TODO: Rename and change types of parameters
    private int hardware_selection_mode;
    private String hardwarePort;

    protected static final int HARDWARE_SELECTION_MODE_SENSOR = 0;
    protected static final int HARDWARE_SELECTION_MODE_MOTOR = 1;


    private String[] sensortypes = {HardwareMapping.notDefined,
            EV3ColorSensor.getName(),
            EV3UltrasonicSensor.getName(),
            EV3TouchSensor.getName(),
            EV3GyroSensor.getName(),
            EV3IRSensor.getName(),
    };

    private String[] motortypes = {HardwareMapping.notDefined,
            Motors.UnregulatedMotor.getName()
    };


    private String selectedSensorType = HardwareMapping.notDefined;
    private String selectedSensorMode = HardwareMapping.notDefined;
    private String selectedMotorType =  HardwareMapping.notDefined;

    private HashMap<String,String[]> senTypeToMode = null;

    //-- UI
    private Spinner spinner_select_type;
    private Spinner spinner_select_mode;
    private TextView txtView_port;
    private TextView txtView_type;
    private TextView txtView_mode;



    public HardwareSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hardwareType Parameter 1.
     * @param hardwarePort Parameter 2.
     * @return A new instance of fragment HardwareSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HardwareSelectionFragment newInstance(int hardwareType, String hardwarePort) {
        HardwareSelectionFragment fragment = new HardwareSelectionFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_HARDWARE_SELECTION_MODE,hardwareType);
        args.putString(KEY_HARDWARE_PORT,hardwarePort);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hardware_selection_mode = getArguments().getInt(KEY_HARDWARE_SELECTION_MODE);
            hardwarePort = getArguments().getString(KEY_HARDWARE_PORT);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hardware_selection, container, false);

        spinner_select_type = (Spinner) view.findViewById(R.id.spinner_type);
        spinner_select_mode = (Spinner) view.findViewById(R.id.spinner_mode);
        txtView_port = (TextView) view.findViewById(R.id.txtView_port);
        txtView_type = (TextView) view.findViewById(R.id.txtView_hardwareSelect_type);
        txtView_mode = (TextView) view.findViewById(R.id.txtView_hardwareSelect_mode);

        //Init Spinner Selection listener
        spinner_select_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            int count = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSensorType = (String) parent.getSelectedItem();
                selectedMotorType = (String) parent.getSelectedItem();
                if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR && count >= 1) { // count >= 1: So the (loaded)Mode wont be reset on start
                    System.out.println("** sensormode adapter set");
                    spinner_select_mode.setAdapter(getSensorModeAdapter(selectedSensorType));
                }else {
                    count++;
                }
                if(isValidSelection()){
                    //Toast.makeText(getActivity(),"Valid configuration",Toast.LENGTH_SHORT).show();
                    savePortConfig(hardware_selection_mode, hardwarePort);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSensorType = HardwareMapping.notDefined;
            }
        });

        spinner_select_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSensorMode = (String) parent.getSelectedItem();
                if(isValidSelection()){
                    //Toast.makeText(getActivity(),"Valid configuration",Toast.LENGTH_SHORT).show();
                    savePortConfig(hardware_selection_mode, hardwarePort);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSensorType = HardwareMapping.notDefined;
            }
        });

        switch(hardware_selection_mode){
            case HARDWARE_SELECTION_MODE_MOTOR: initMotorView(); break;
            case HARDWARE_SELECTION_MODE_SENSOR: initSensorView(); break;
        }
        loadPortConfig(hardware_selection_mode,hardwarePort);

        return view;
    }


    private void initSensorView() {
        initSensorTypeToModeMap();

        if(hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S1))){
            txtView_port.setText("1");
        }else if(hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S2))){
            txtView_port.setText("2");
        }else if(hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S3))){
            txtView_port.setText("3");
        }else if(hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S4))){
            txtView_port.setText("4");
        }

        txtView_type.setText(getResources().getString(R.string.view_sensortype));
        txtView_mode.setText(getResources().getString(R.string.view_sensormode));

        spinner_select_type.setAdapter(getSensorTypeAdapter());
        spinner_select_mode.setAdapter(getSensorModeAdapter(HardwareMapping.notDefined));


    }

    private boolean isValidSelection() {
        if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
            //VALIDATE SENSOR SELECTION
            if (!selectedSensorType.equals(HardwareMapping.notDefined) && !selectedSensorMode.equals(HardwareMapping.notDefined)) {
                String[] modes;
                if ((modes = senTypeToMode.get(selectedSensorType)) != null) {
                    for (String mode : modes) {
                        if (mode.equals(selectedSensorMode)) {
                            return true;
                        }
                    }
                }
                return false;
            }else if(selectedSensorType.equals(HardwareMapping.notDefined) && selectedSensorMode.equals(HardwareMapping.notDefined)){
                //No Sensor-/Mode selected
                return true;
            }
        }else{
            //Motor Hardware Selection mode --> always true
            return true;
        }
        return false;
    }

    /**
     * Maps sensors to their available Modes
     */
    private void initSensorTypeToModeMap() {
        senTypeToMode = new HashMap<String,String[]>();
        senTypeToMode.put(HardwareMapping.notDefined,new String[]{HardwareMapping.notDefined});

        for (Sensors sensors : Sensors.getAllSensorTypes()) {
            String[] modes = new String[sensors.getModes().length];
            for (int i = 0; i < modes.length; i++) {
                modes[i] = sensors.getModes()[i].getValue();
            }
            senTypeToMode.put(sensors.getName(),modes);
        }


    }

    private SpinnerAdapter getSensorTypeAdapter() {
        return new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getSensorTypes());
    }

    private SpinnerAdapter getSensorModeAdapter(String sensortype){
        return new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, senTypeToMode.get(sensortype));
    }

    private SpinnerAdapter getMotorAdapter(){
        return new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getMotorTypes());
    }

    private String[] getMotorTypes(){
        return motortypes;
    }

    private String[] getSensorTypes() {
        return sensortypes;
    }

    private void initMotorView() {
        spinner_select_mode.setVisibility(View.GONE);
        spinner_select_type.setAdapter(getMotorAdapter());

        if(hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_A))){
            txtView_port.setText("A");
        }else if(hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_B))){
            txtView_port.setText("B");
        }else if(hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_C))){
            txtView_port.setText("C");
        }else if(hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_D))){
            txtView_port.setText("D");
        }

        txtView_type.setText(getResources().getString(R.string.view_motortype));
        txtView_mode.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadPortConfig(int hardware_selection_mode, String hardwarePort){
        if(SettingsProvider.getInstance().isInitialized()) {
            if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
                String type = HardwareMapping.notDefined;
                String sensormode = HardwareMapping.notDefined;
                //SENSOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S1))) {
                    type = SettingsProvider.getInstance().getSensorS1() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS1().getName();
                    sensormode = SettingsProvider.getInstance().getSensorModeS1() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS1().getValue();
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S2))) {
                    type = SettingsProvider.getInstance().getSensorS2() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS2().getName();
                    sensormode = SettingsProvider.getInstance().getSensorModeS2() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS2().getValue();
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S3))) {
                    type = SettingsProvider.getInstance().getSensorS3() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS3().getName();
                    sensormode = SettingsProvider.getInstance().getSensorModeS3() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS3().getValue();
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S4))) {
                    type = SettingsProvider.getInstance().getSensorS4() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS4().getName();
                    sensormode = SettingsProvider.getInstance().getSensorModeS4() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS4().getValue();
                }

                selectedSensorType = type;
                selectedSensorMode = sensormode;

                selectSpinnerItem(spinner_select_type,type);
                if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
                    System.out.println("** Select mode item");
                    spinner_select_mode.setAdapter(getSensorModeAdapter(selectedSensorType));
                }
                selectSpinnerItem(spinner_select_mode,sensormode);

            }else{
                String type = HardwareMapping.notDefined;
                //MOTOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_A))) {
                    type = SettingsProvider.getInstance().getMotorA() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorA().getName();

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_B))) {
                    type = SettingsProvider.getInstance().getMotorB() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorB().getName();

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_C))) {
                    type = SettingsProvider.getInstance().getMotorC() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorC().getName();

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_D))) {
                    type = SettingsProvider.getInstance().getMotorD() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorD().getName();
                }
                selectedMotorType = type;
                selectSpinnerItem(spinner_select_type,type);
            }
        }
    }

    private void selectSpinnerItem(Spinner spinner, String item){
        for(int i = 0; i < spinner.getAdapter().getCount();i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                spinner.setSelection(i);
                System.out.println("** Selected Item "+item+" - "+i);
                break;
            }
        }
    }

    /**
     * Save selected Port Configuration to SharedPreferences
     * @param hardware_selection_mode
     * @param hardwarePort
     */
    private void savePortConfig(int hardware_selection_mode, String hardwarePort){
        SharedPreferences.Editor e = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_portConfiguration),Context.MODE_PRIVATE).edit();
        //e.clear();
        if(e != null) {
            if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
                //SENSOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S1))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S1),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S1),spinner_select_mode.getSelectedItem().toString());

                    robot.getRobotPortConfig().setSensorS1(HardwareMapping.getSensorType(spinner_select_type.getSelectedItem().toString()));
                    robot.getRobotPortConfig().setSensormodeS1(HardwareMapping.getSensorMode(spinner_select_mode.getSelectedItem().toString()));
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S2))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S2),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S2),spinner_select_mode.getSelectedItem().toString());

                    robot.getRobotPortConfig().setSensorS2(HardwareMapping.getSensorType(spinner_select_type.getSelectedItem().toString()));
                    robot.getRobotPortConfig().setSensormodeS2(HardwareMapping.getSensorMode(spinner_select_mode.getSelectedItem().toString()));
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S3))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S3),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S3),spinner_select_mode.getSelectedItem().toString());

                    robot.getRobotPortConfig().setSensorS3(HardwareMapping.getSensorType(spinner_select_type.getSelectedItem().toString()));
                    robot.getRobotPortConfig().setSensormodeS3(HardwareMapping.getSensorMode(spinner_select_mode.getSelectedItem().toString()));
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S4))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S4),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S4),spinner_select_mode.getSelectedItem().toString());

                    robot.getRobotPortConfig().setSensorS4(HardwareMapping.getSensorType(spinner_select_type.getSelectedItem().toString()));
                    robot.getRobotPortConfig().setSensormodeS4(HardwareMapping.getSensorMode(spinner_select_mode.getSelectedItem().toString()));
                }

               e.commit();
            }else{
                //MOTOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_A))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_A),spinner_select_type.getSelectedItem().toString());
                    robot.getRobotPortConfig().setMotorA(HardwareMapping.getMotorType(spinner_select_type.getSelectedItem().toString()));
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_B))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_B),spinner_select_type.getSelectedItem().toString());
                    robot.getRobotPortConfig().setMotorB(HardwareMapping.getMotorType(spinner_select_type.getSelectedItem().toString()));
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_C))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_C),spinner_select_type.getSelectedItem().toString());
                    robot.getRobotPortConfig().setMotorC(HardwareMapping.getMotorType(spinner_select_type.getSelectedItem().toString()));
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_D))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_D),spinner_select_type.getSelectedItem().toString());
                    robot.getRobotPortConfig().setMotorD(HardwareMapping.getMotorType(spinner_select_type.getSelectedItem().toString()));
                }
                e.commit();
            }
            //Inform listener that changes occured
            robotConfigurationChangedListener.onRobotConfigurationChangedListener();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
