package org.mindroid.android.app.fragments.sensormonitoring;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import org.mindroid.android.app.R;
import org.mindroid.android.app.activities.MainActivity;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.android.app.robodancer.Robot;

import static org.mindroid.android.app.fragments.home.HomeFragment.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorMonitoringFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorMonitoringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorMonitoringFragment extends Fragment implements SensorObservationFragment.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROBOT = "robot";

    private OnFragmentInteractionListener mListener;


    private String sensortypeS1;
    private String sensortypeS2;
    private String sensortypeS3;
    private String sensortypeS4;

    private String sensormodeS1;
    private String sensormodeS2;
    private String sensormodeS3;
    private String sensormodeS4;

    //UI Elements
    private LinearLayout list_sens_monitoring;

    private SensorObservationFragment sensor_port1_monitor;
    private SensorObservationFragment sensor_port2_monitor;
    private SensorObservationFragment sensor_port3_monitor;
    private SensorObservationFragment sensor_port4_monitor;

    private String infoMessage;

    public SensorMonitoringFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SensorMonitoringFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorMonitoringFragment newInstance() {
        SensorMonitoringFragment fragment = new SensorMonitoringFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPortConfig();

        //Create Fragments
        sensor_port1_monitor = SensorObservationFragment.newInstance("S"+EV3PortIDs.PORT_1.getLabel(), ""+sensortypeS1,""+sensormodeS1);
        sensor_port2_monitor = SensorObservationFragment.newInstance("S"+EV3PortIDs.PORT_2.getLabel(), sensortypeS2,sensormodeS2);
        sensor_port3_monitor = SensorObservationFragment.newInstance("S"+EV3PortIDs.PORT_3.getLabel(), sensortypeS3,sensormodeS3);
        sensor_port4_monitor = SensorObservationFragment.newInstance("S"+EV3PortIDs.PORT_4.getLabel(), sensortypeS4,sensormodeS4);

        Robot robot = HomeFragment.robot;

        sensor_port1_monitor.registerSensorListener(robot.getListenerForPort(EV3PortIDs.PORT_1));
        sensor_port2_monitor.registerSensorListener(robot.getListenerForPort(EV3PortIDs.PORT_2));
        sensor_port3_monitor.registerSensorListener(robot.getListenerForPort(EV3PortIDs.PORT_3));
        sensor_port4_monitor.registerSensorListener(robot.getListenerForPort(EV3PortIDs.PORT_4));

        infoMessage = getResources().getString(R.string.dialog_info_message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sensor_monitoring, container, false);

        list_sens_monitoring = (LinearLayout) view.findViewById(R.id.linLayout_sensor_monitoring);

        addSensorObservationToUI(sensor_port1_monitor);
        addSensorObservationToUI(sensor_port2_monitor);
        addSensorObservationToUI(sensor_port3_monitor);
        addSensorObservationToUI(sensor_port4_monitor);

        if(!robot.isRunning){
            if(getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).showInfoDialog("Info",infoMessage);
            }
        }

        return view;
    }

    private void addSensorObservationToUI(SensorObservationFragment sobs_fragment) {
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(list_sens_monitoring.getId(), sobs_fragment);
        transaction.commit();
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

    private void loadPortConfig(){
        SharedPreferences portConfigProperties = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_portConfiguration),Context.MODE_PRIVATE);

        final String notDefined = "-";

        if(portConfigProperties != null) {
            // ---- load sensortypes ---- //
            String savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S1), "");
            sensortypeS1 = (savedVal.isEmpty()) ? notDefined : savedVal;

            savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S2), "");
            sensortypeS2 = (savedVal.isEmpty()) ? notDefined : savedVal;

            savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S3), "");
            sensortypeS3= (savedVal.isEmpty()) ? notDefined : savedVal;

            savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSOR_S4), "");
            sensortypeS4= (savedVal.isEmpty()) ? notDefined : savedVal;

            // --- load sensormodes ---- //
            savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S1), "");
            sensormodeS1 = (savedVal.isEmpty()) ? notDefined : savedVal;

            savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S2), "");
            sensormodeS2 = (savedVal.isEmpty()) ? notDefined : savedVal;

            savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S3), "");
            sensormodeS3 = (savedVal.isEmpty()) ? notDefined : savedVal;

            savedVal = portConfigProperties.getString(getResources().getString(R.string.KEY_SENSORMODE_S4), "");
            sensormodeS4 = (savedVal.isEmpty()) ? notDefined : savedVal;
        }
    }
}
