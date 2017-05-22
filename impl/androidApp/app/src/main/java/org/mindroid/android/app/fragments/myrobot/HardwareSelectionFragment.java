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
import android.widget.Toast;

import org.mindroid.android.app.R;
import org.mindroid.common.messages.Motors;
import org.mindroid.common.messages.Sensors;

import java.util.HashMap;

import static org.mindroid.common.messages.Sensors.EV3ColorSensor;
import static org.mindroid.common.messages.Sensors.EV3GyroSensor;
import static org.mindroid.common.messages.Sensors.EV3IRSensor;
import static org.mindroid.common.messages.Sensors.EV3TouchSensor;
import static org.mindroid.common.messages.Sensors.EV3UltrasonicSensor;
import static org.mindroid.common.messages.Sensors.getAllSensorTypes;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HardwareSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HardwareSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HardwareSelectionFragment extends Fragment implements MyRobotFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_HARDWARE_SELECTION_MODE = "org.mindroid.android.app.fragments.myrobot.HardwareSelectionFragment.hardwaretype";
    private static final String KEY_HARDWARE_PORT = "org.mindroid.android.app.fragments.myrobot.HardwareSelectionFragment.hardwareport";

    // TODO: Rename and change types of parameters
    private int hardware_selection_mode;
    private String hardwarePort;

    private OnFragmentInteractionListener mListener;



    public static final int HARDWARE_SELECTION_MODE_SENSOR = 0;
    public static final int HARDWARE_SELECTION_MODE_MOTOR = 1;

    String notDefined = "-";
    private String[] sensortypes = {notDefined,
            EV3ColorSensor.getName(),
            EV3UltrasonicSensor.getName(),
            EV3TouchSensor.getName(),
            EV3GyroSensor.getName(),
            EV3IRSensor.getName(),
    };

    private String[] motortypes = {notDefined,
            Motors.UnregulatedMotor.getName()
    };


    String selectedSensorType =notDefined;
    String selectedSensorMode =notDefined;
    String selectedMotorType = notDefined;

    HashMap<String,String[]> senTypeToMode = null;

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

        switch(hardware_selection_mode){
            case HARDWARE_SELECTION_MODE_MOTOR: initMotorView(); break;
            case HARDWARE_SELECTION_MODE_SENSOR: initSensorView(); break;
        };


        spinner_select_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSensorType = (String) parent.getSelectedItem();
                selectedMotorType = (String) parent.getSelectedItem();
                if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
                    spinner_select_mode.setAdapter(getSensorModeAdapter(selectedSensorType));
                }
                if(isValidSelection()){
                    //Toast.makeText(getActivity(),"Valid configuration",Toast.LENGTH_SHORT).show();
                    savePortConfig(hardware_selection_mode, hardwarePort);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSensorType = notDefined;
            }
        });

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
        spinner_select_mode.setAdapter(getSensorModeAdapter(notDefined));

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
                selectedSensorType = notDefined;
            }
        });
    }

    private boolean isValidSelection() {
        if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
            //VALIDATE SENSOR SELECTION
            if (!selectedSensorType.equals(notDefined) && !selectedSensorMode.equals(notDefined)) {
                String[] modes;
                if ((modes = senTypeToMode.get(selectedSensorType)) != null) {
                    for (String mode : modes) {
                        if (mode.equals(selectedSensorMode)) {
                            return true;
                        }
                    }
                }
                return false;
            }else if(selectedSensorType.equals(notDefined) && selectedSensorMode.equals(notDefined)){
                //No Sensor-/Mode selected
                return true;
            }
        }else{
            //Motor Hardware Selection mode --> always true
            return true;
        }
        return false;
    }

    private void initSensorTypeToModeMap() {
        senTypeToMode = new HashMap<String,String[]>();
        senTypeToMode.put(notDefined,new String[]{notDefined});

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
    };

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
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void loadPortConfig(int hardware_selection_mode, String hardwarePort){
        SharedPreferences portConfigProperties = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_portConfiguration),Context.MODE_PRIVATE);
        if(portConfigProperties != null) {
            // ---- load sensortypes ---- //
            String type ="";
            String sensormode="";

            if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
                //SENSOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S1))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S1), notDefined);
                    sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S1), notDefined);
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S2))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S2), notDefined);
                    sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S2), notDefined);
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S3))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S3), notDefined);
                    sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S3), notDefined);
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S4))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S4), notDefined);
                    sensormode = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S4), notDefined);
                }

                selectSpinnerItem(spinner_select_type,type);
                selectSpinnerItem(spinner_select_mode,sensormode);

            }else{
                //MOTOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_A))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_A), notDefined);

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_B))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_B), notDefined);

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_C))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_C), notDefined);

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_D))) {
                    type = portConfigProperties.getString(getResources().getString(R.string.KEY_MOTOR_D), notDefined);
                }

                selectSpinnerItem(spinner_select_type,type);

            }
        }
    }

    private void selectSpinnerItem(Spinner spinner, String item){
        for(int i = 0; i < spinner.getAdapter().getCount();i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void savePortConfig(int hardware_selection_mode, String hardwarePort){
        SharedPreferences.Editor e = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_portConfiguration),Context.MODE_PRIVATE).edit();
        //e.clear();
        if(e != null) {
            if(hardware_selection_mode == HARDWARE_SELECTION_MODE_SENSOR) {
                //SENSOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S1))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S1),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S1),spinner_select_mode.getSelectedItem().toString());
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S2))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S2),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S2),spinner_select_mode.getSelectedItem().toString());
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S3))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S3),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S3),spinner_select_mode.getSelectedItem().toString());
                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_SENSOR_S4))) {
                    e.putString(getResources().getString(R.string.KEY_SENSOR_S4),spinner_select_type.getSelectedItem().toString());
                    e.putString(getResources().getString(R.string.KEY_SENSORMODE_S4),spinner_select_mode.getSelectedItem().toString());
                }

               e.commit();
            }else{
                //MOTOR
                if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_A))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_A),spinner_select_type.getSelectedItem().toString());

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_B))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_B),spinner_select_type.getSelectedItem().toString());

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_C))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_C),spinner_select_type.getSelectedItem().toString());

                } else if (hardwarePort.equals(getResources().getString(R.string.KEY_MOTOR_D))) {
                    e.putString(getResources().getString(R.string.KEY_MOTOR_D),spinner_select_type.getSelectedItem().toString());

                }
                e.commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
