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

    private OnFragmentInteractionListener mListener;

    public MyRobotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyRobotFragment.
     */
    public static MyRobotFragment newInstance() {
        MyRobotFragment fragment = new MyRobotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_robot, container, false);

        TabHost host = (TabHost)view.findViewById(R.id.tab_container);
        host.setup();

        //Init sensor Configuration Tab
        TabHost.TabSpec tab_sensors_spec = host.newTabSpec("Sensors");//getResources().getString(R.string.tab_sensors_tag)
        tab_sensors_spec.setIndicator(getResources().getString(R.string.tab_sensors_tag));
        tab_sensors_spec.setContent(R.id.tab_sensors);
        host.addTab(tab_sensors_spec);

        createSensorFragments(view);

        //Init motor configuration Tab
        TabHost.TabSpec tab_motors_spec = host.newTabSpec(getResources().getString(R.string.tab_motors_tag));
        tab_motors_spec.setIndicator(getResources().getString(R.string.tab_motors_tag));
        tab_motors_spec.setContent(R.id.tab_motors);
        host.addTab(tab_motors_spec);

        createMotorFragments(view);

        return view;
    }

    /**
     * Creats HardwareSelectionFragments for the Sensor tab and adds them to the UI
     *
     * @param view
     */
    private void createSensorFragments(View view) {
        LinearLayout sensor_container = (LinearLayout) view.findViewById(R.id.sensor_container);
        sensor_container.setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater;
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View seperator1 = (View) inflater.inflate(R.layout.seperator ,null);
        View seperator2 = (View) inflater.inflate(R.layout.seperator ,null);
        View seperator3 = (View) inflater.inflate(R.layout.seperator ,null);

        //---------- first fragment ---------
        createSensorSelectionFragment(sensor_container,getResources().getString(R.string.KEY_SENSOR_S1));

        //---------- second fragment ---------
        createSensorSelectionFragment(sensor_container,getResources().getString(R.string.KEY_SENSOR_S2));

        //---------- third fragment ---------
        createSensorSelectionFragment(sensor_container,getResources().getString(R.string.KEY_SENSOR_S3));

        //---------- fourth fragment ---------
        createSensorSelectionFragment(sensor_container,getResources().getString(R.string.KEY_SENSOR_S4));
    }

    /**
     * adds a single Sensor Selection Fragment to the layout
     * @param sensor_container
     * @param sensorPort
     */
    private void createSensorSelectionFragment(LinearLayout sensor_container, String sensorPort) {
        HardwareSelectionFragment fragment = HardwareSelectionFragment.newInstance(HardwareSelectionFragment.HARDWARE_SELECTION_MODE_SENSOR,sensorPort);
        getFragmentManager().beginTransaction()
                .add(sensor_container.getId(), fragment)
                .commit();
    }

    /**
     * Creates HardwareSelectionFragments for the Motor-Tab an adds them to the UI
     *
     * @param view
     * @param fragmentManager
     */
    private void createMotorFragments(View view) {
        LinearLayout motor_container = (LinearLayout) view.findViewById(R.id.motor_container);
        motor_container.setOrientation(LinearLayout.VERTICAL);

        createMotorSelectionFragment(motor_container,getResources().getString(R.string.KEY_MOTOR_A));
        createMotorSelectionFragment(motor_container,getResources().getString(R.string.KEY_MOTOR_B));
        createMotorSelectionFragment(motor_container,getResources().getString(R.string.KEY_MOTOR_C));
        createMotorSelectionFragment(motor_container,getResources().getString(R.string.KEY_MOTOR_D));
    }

    /**
     *  Adds a single motor selection fragment to the layout
     * @param motor_container
     * @param motorPort
     */
    private void createMotorSelectionFragment(LinearLayout motor_container,String motorPort) {
        getFragmentManager().beginTransaction()
                .add(motor_container.getId(), HardwareSelectionFragment.newInstance(HardwareSelectionFragment.HARDWARE_SELECTION_MODE_MOTOR,motorPort))
                .commit();
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
        void onFragmentInteraction(Uri uri);
    }

}
