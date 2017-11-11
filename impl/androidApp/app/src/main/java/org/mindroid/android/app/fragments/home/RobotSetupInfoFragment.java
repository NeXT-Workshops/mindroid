package org.mindroid.android.app.fragments.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import org.mindroid.android.app.R;
import org.mindroid.android.app.fragments.myrobot.HardwareMapping;
import org.mindroid.android.app.robodancer.SettingsProvider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RobotSetupInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RobotSetupInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotSetupInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /** textviews **/
    private TextView txtView_sensortype_s1;
    private TextView txtView_sensortype_s2;
    private TextView txtView_sensortype_s3;
    private TextView txtView_sensortype_s4;

    private TextView txtView_sensormode_s1;
    private TextView txtView_sensormode_s2;
    private TextView txtView_sensormode_s3;
    private TextView txtView_sensormode_s4;

    private TextView txtView_motor_a;
    private TextView txtView_motor_b;
    private TextView txtView_motor_c;
    private TextView txtView_motor_d;

    private TextView txtView_robot_id;
    private TextView txtView_group_id;
    private TextView txtView_msg_server_ip;
    private TextView txtView_ev3_brick_ip;

    public RobotSetupInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RobotSetupInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotSetupInfoFragment newInstance(String param1, String param2) {
        RobotSetupInfoFragment fragment = new RobotSetupInfoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_robot_infotabs, container, false);

        TabHost host = (TabHost)view.findViewById(R.id.tabHost_info_robotSetup);
        host.setup();

        //Set Textview-Fields
        getTextviews(view);

        //Load Configuration and Display
        setSensorTabTexts();

        //Load Info SettingsProvider and Display
        setInfoTabTexts();


        //Init sensor Configuration Tab
        TabHost.TabSpec tab_info_sensors = host.newTabSpec(getResources().getString(R.string.tab_sensors_tag));//getResources().getString(R.string.tab_sensors_tag)
        tab_info_sensors.setIndicator(getResources().getString(R.string.tab_sensors_tag));
        tab_info_sensors.setContent(R.id.tab_config_sensors);
        host.addTab(tab_info_sensors);

        //Init motor Configuration Tab
        TabHost.TabSpec tab_info_motors = host.newTabSpec(getResources().getString(R.string.tab_motors_tag));//getResources().getString(R.string.tab_sensors_tag)
        tab_info_motors.setIndicator(getResources().getString(R.string.tab_motors_tag));
        tab_info_motors.setContent(R.id.tab_config_motors);
        host.addTab(tab_info_motors);


        //Init Connection configuration Tab
        TabHost.TabSpec tab_info_connection = host.newTabSpec(getResources().getString(R.string.tab_connection_tag));
        tab_info_connection.setIndicator(getResources().getString(R.string.tab_connection_tag));
        tab_info_connection.setContent(R.id.tab_connection);
        host.addTab(tab_info_connection);

        // Inflate the layout for this fragment
        return view;
    }

    private void getTextviews(View view) {
        // get textviews
        txtView_sensortype_s1 = (TextView)view.findViewById(R.id.txt_sensor_s1);
        txtView_sensortype_s2 = (TextView)view.findViewById(R.id.txt_sensor_s2);
        txtView_sensortype_s3 = (TextView)view.findViewById(R.id.txt_sensor_s3);
        txtView_sensortype_s4 = (TextView)view.findViewById(R.id.txt_sensor_s4);

        txtView_sensormode_s1 = (TextView)view.findViewById(R.id.txt_sensormode_s1);
        txtView_sensormode_s2 = (TextView)view.findViewById(R.id.txt_sensormode_s2);
        txtView_sensormode_s3 = (TextView)view.findViewById(R.id.txt_sensormode_s3);
        txtView_sensormode_s4 = (TextView)view.findViewById(R.id.txt_sensormode_s4);

        txtView_motor_a = (TextView)view.findViewById(R.id.txt_motor_A);
        txtView_motor_b = (TextView)view.findViewById(R.id.txt_motor_B);
        txtView_motor_c = (TextView)view.findViewById(R.id.txt_motor_C);
        txtView_motor_d = (TextView)view.findViewById(R.id.txt_motor_D);

        txtView_robot_id = (TextView)view.findViewById(R.id.txt_robot_id);
        txtView_group_id = (TextView)view.findViewById(R.id.txt_group_id);
        txtView_msg_server_ip = (TextView)view.findViewById(R.id.txt_conn_msgServer);
        txtView_ev3_brick_ip = (TextView)view.findViewById(R.id.txt_conn_brick);
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


    private void setSensorTabTexts(){
            if(SettingsProvider.getInstance().isInitialized()) {
                // ---- load sensortypes ---- //
                String txt = SettingsProvider.getInstance().getSensorS1() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS1().getName();
                txtView_sensortype_s1.setText(txt);

                txt = SettingsProvider.getInstance().getSensorS2() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS2().getName();
                txtView_sensortype_s2.setText(txt);

                txt = SettingsProvider.getInstance().getSensorS3() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS3().getName();
                txtView_sensortype_s3.setText(txt);

                txt = SettingsProvider.getInstance().getSensorS4() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorS4().getName();
                txtView_sensortype_s4.setText(txt);

                // --- load sensormodes ---- //
                txt = SettingsProvider.getInstance().getSensorModeS1() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS1().getValue();
                txtView_sensormode_s1.setText(txt);

                txt = SettingsProvider.getInstance().getSensorModeS2() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS2().getValue();
                txtView_sensormode_s2.setText(txt);

                txt = SettingsProvider.getInstance().getSensorModeS3() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS3().getValue();
                txtView_sensormode_s3.setText(txt);

                txt = SettingsProvider.getInstance().getSensorModeS4() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getSensorModeS4().getValue();
                txtView_sensormode_s4.setText(txt);
                setMotorTabTexts();
            }
    }

    private void setMotorTabTexts() {
        if(SettingsProvider.getInstance().isInitialized()) {
            String txt;
            txt = SettingsProvider.getInstance().getMotorA() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorA().getName();
            txtView_motor_a.setText(txt);

            txt = SettingsProvider.getInstance().getMotorB() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorB().getName();
            txtView_motor_b.setText(txt);

            txt = SettingsProvider.getInstance().getMotorC() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorC().getName();
            txtView_motor_c.setText(txt);

            txt = SettingsProvider.getInstance().getMotorD() == null ? HardwareMapping.notDefined : SettingsProvider.getInstance().getMotorD().getName();
            txtView_motor_d.setText(txt);
        }
    }

    private void setInfoTabTexts(){
        if(SettingsProvider.getInstance().isInitialized()) {
            txtView_robot_id.setText(SettingsProvider.getInstance().getRobotID());
            txtView_group_id.setText(SettingsProvider.getInstance().getGroupID());
            txtView_msg_server_ip.setText(SettingsProvider.getInstance().getMsgServerIP());
            txtView_ev3_brick_ip.setText(SettingsProvider.getInstance().getEv3IP());
        }
    }
}
