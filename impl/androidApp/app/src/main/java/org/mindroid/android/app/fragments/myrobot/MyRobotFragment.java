package org.mindroid.android.app.fragments.myrobot;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import org.mindroid.android.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyRobotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyRobotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRobotFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    public MyRobotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRobotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRobotFragment newInstance(String param1, String param2) {
        MyRobotFragment fragment = new MyRobotFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_robot, container, false);

        TabHost host = (TabHost)view.findViewById(R.id.tab_container);
        host.setup();

        FragmentManager fragmentManager = getFragmentManager();

        //Init sensor Configuration Tab
        TabHost.TabSpec tab_sensors_spec = host.newTabSpec("Sensors");//getResources().getString(R.string.tab_sensors_tag)
        tab_sensors_spec.setIndicator(getResources().getString(R.string.tab_sensors_tag));
        tab_sensors_spec.setContent(R.id.tab_sensors);
        host.addTab(tab_sensors_spec);

        createSensorFragments(view, fragmentManager);

        //Init motor configuration Tab
        TabHost.TabSpec tab_motors_spec = host.newTabSpec(getResources().getString(R.string.tab_motors_tag));
        tab_motors_spec.setIndicator(getResources().getString(R.string.tab_motors_tag));
        tab_motors_spec.setContent(R.id.tab_motors);
        host.addTab(tab_motors_spec);

        createMotorFragments(view, fragmentManager);

        return view;
    }

    /**
     * Creats HardwareSelectionFragments for the Sensor tab and adds them to the UI
     *
     * @param view
     * @param fragmentManager
     */
    private void createSensorFragments(View view, FragmentManager fragmentManager) {
        LinearLayout sensor_container = (LinearLayout) view.findViewById(R.id.sensor_container);
        sensor_container.setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater;
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View seperator1 = (View) inflater.inflate(R.layout.seperator ,null);
        View seperator2 = (View) inflater.inflate(R.layout.seperator ,null);
        View seperator3 = (View) inflater.inflate(R.layout.seperator ,null);

        //---------- first fragment ---------
        createSensorSelectionFragment(fragmentManager, sensor_container,getResources().getString(R.string.KEY_SENSOR_S1));

        //---------- second fragment ---------
        createSensorSelectionFragment(fragmentManager, sensor_container,getResources().getString(R.string.KEY_SENSOR_S2));

        //---------- third fragment ---------
        createSensorSelectionFragment(fragmentManager, sensor_container,getResources().getString(R.string.KEY_SENSOR_S3));

        //---------- fourth fragment ---------
        createSensorSelectionFragment(fragmentManager, sensor_container,getResources().getString(R.string.KEY_SENSOR_S4));
    }

    /**
     * adds a single Sensor Selection Fragment to the layout
     * @param fragmentManager
     * @param sensor_container
     * @param sensorPort
     */
    private void createSensorSelectionFragment(FragmentManager fragmentManager, LinearLayout sensor_container, String sensorPort) {
        HardwareSelectionFragment fragment = HardwareSelectionFragment.newInstance(HardwareSelectionFragment.HARDWARE_SELECTION_MODE_SENSOR,sensorPort);
        fragment.getView().findViewById(R.id.hardware_selection_container).setBackgroundColor(getResources().getColor(R.color.hardware_selection_elmnt));
        fragmentManager.beginTransaction()
                .add(sensor_container.getId(), fragment)
                .commit();
    }

    /**
     * Creates HardwareSelectionFragments for the Motor-Tab an adds them to the UI
     *
     * @param view
     * @param fragmentManager
     */
    private void createMotorFragments(View view, FragmentManager fragmentManager) {
        LinearLayout motor_container = (LinearLayout) view.findViewById(R.id.motor_container);
        motor_container.setOrientation(LinearLayout.VERTICAL);

        createMotorSelectionFragment(fragmentManager, motor_container,getResources().getString(R.string.KEY_MOTOR_A));
        createMotorSelectionFragment(fragmentManager, motor_container,getResources().getString(R.string.KEY_MOTOR_B));
        createMotorSelectionFragment(fragmentManager, motor_container,getResources().getString(R.string.KEY_MOTOR_C));
        createMotorSelectionFragment(fragmentManager, motor_container,getResources().getString(R.string.KEY_MOTOR_D));
    }

    /**
     *  Adds a single motor selection fragment to the layout
     * @param fragmentManager
     * @param motor_container
     * @param motorPort
     */
    private void createMotorSelectionFragment(FragmentManager fragmentManager, LinearLayout motor_container,String motorPort) {
        fragmentManager.beginTransaction()
                .add(motor_container.getId(), HardwareSelectionFragment.newInstance(HardwareSelectionFragment.HARDWARE_SELECTION_MODE_MOTOR,motorPort))
                .commit();
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

}
