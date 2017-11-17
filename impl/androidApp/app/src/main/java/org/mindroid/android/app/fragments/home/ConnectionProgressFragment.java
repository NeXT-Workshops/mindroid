package org.mindroid.android.app.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import org.mindroid.android.app.R;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;

public class ConnectionProgressFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_PARAM_SEN_P1 = "sen_port1";
    private static final String KEY_PARAM_SEN_P2 = "sen_port2";
    private static final String KEY_PARAM_SEN_P3 = "sen_port3";
    private static final String KEY_PARAM_SEN_P4 = "sen_port4";

    private static final String KEY_PARAM_MOT_A = "mot_port1";
    private static final String KEY_PARAM_MOT_B = "mot_port2";
    private static final String KEY_PARAM_MOT_C = "mot_port3";
    private static final String KEY_PARAM_MOT_D = "mot_port4";

    private static final String KEY_PARAM_BRICK = "Brick";
    private static final String KEY_PARAM_MESSENGER = "Messenger";

    private String sen_p1;
    private String sen_p2;
    private String sen_p3;
    private String sen_p4;

    private String mot_p1;
    private String mot_p2;
    private String mot_p3;
    private String mot_p4;

    private LinearLayout container_messenger;
    private LinearLayout container_brick;
    private LinearLayout container_sensors;
    private LinearLayout container_motors;

    private ProgressFragment pfMessenger;
    private ProgressFragment pfBrick;
    private ProgressFragment pfSensor1;
    private ProgressFragment pfSensor2;
    private ProgressFragment pfSensor3;
    private ProgressFragment pfSensor4;
    private ProgressFragment pfMotor1;
    private ProgressFragment pfMotor2;
    private ProgressFragment pfMotor3;
    private ProgressFragment pfMotor4;

    public ConnectionProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param args Bundle of parameters
     * @return A new instance of fragment ConnectionProgressFragment.
     */
    public static ConnectionProgressFragment newInstance(Bundle args) {
        ConnectionProgressFragment fragment = new ConnectionProgressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sen_p1 = getArguments().getString(KEY_PARAM_SEN_P1);
            sen_p2 = getArguments().getString(KEY_PARAM_SEN_P2);
            sen_p3 = getArguments().getString(KEY_PARAM_SEN_P3);
            sen_p4 = getArguments().getString(KEY_PARAM_SEN_P4);

            mot_p1 = getArguments().getString(KEY_PARAM_MOT_A);
            mot_p2 = getArguments().getString(KEY_PARAM_MOT_B);
            mot_p3 = getArguments().getString(KEY_PARAM_MOT_C);
            mot_p4 = getArguments().getString(KEY_PARAM_MOT_D);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_progress, container, false);

        container_messenger = (LinearLayout) view.findViewById(R.id.container_msger);
        container_brick = (LinearLayout) view.findViewById(R.id.container_brick);
        container_sensors = (LinearLayout) view.findViewById(R.id.container_sensors);
        container_motors = (LinearLayout) view.findViewById(R.id.container_motors);

        pfMessenger = ProgressFragment.newInstance("Connecting to Message Server");
        container_messenger.addView(pfMessenger.getView());

        pfBrick = ProgressFragment.newInstance("Connecting to Brick");
        container_brick.addView(pfBrick.getView());

        createSensorProgressFragment(EV3PortIDs.PORT_1);
        createSensorProgressFragment(EV3PortIDs.PORT_2);
        createSensorProgressFragment(EV3PortIDs.PORT_3);
        createSensorProgressFragment(EV3PortIDs.PORT_4);

        createMotorProgressFragment(EV3PortIDs.PORT_A);
        createMotorProgressFragment(EV3PortIDs.PORT_B);
        createMotorProgressFragment(EV3PortIDs.PORT_C);
        createMotorProgressFragment(EV3PortIDs.PORT_D);


        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Creates a proper sensor progress fragment, if the argument found for the given port is not null.
     * Also adds the created fragment to its container.
     * @param senPort - port
     */
    private void createSensorProgressFragment(EV3PortID senPort){
        if(senPort.equals(EV3PortIDs.PORT_1) && sen_p1 != null){
            pfSensor1 = ProgressFragment.newInstance(sen_p1);
            container_sensors.addView(pfSensor1.getView());
        }else if(senPort.equals(EV3PortIDs.PORT_2) && sen_p2 != null){
            pfSensor2 = ProgressFragment.newInstance(sen_p2);
            container_sensors.addView(pfSensor2.getView());
        }else if(senPort.equals(EV3PortIDs.PORT_3) && sen_p3 != null) {
            pfSensor3 = ProgressFragment.newInstance(sen_p3);
            container_sensors.addView(pfSensor3.getView());
        }else if(senPort.equals(EV3PortIDs.PORT_4) && sen_p4 != null){
            pfSensor4 = ProgressFragment.newInstance(sen_p4);
            container_sensors.addView(pfSensor4.getView());
        }
    }

    /**
     * Creates a proper motor progress fragment, if the argument found for the given port is not null.
     * Also adds the created fragment to its container.
     * @param motorPort - port
     */
    private void createMotorProgressFragment(EV3PortID motorPort){
        if(motorPort.equals(EV3PortIDs.PORT_A) && mot_p1 != null){
            pfMotor1 = ProgressFragment.newInstance(mot_p1);
            container_motors.addView(pfMotor1.getView());
        }else if(motorPort.equals(EV3PortIDs.PORT_B) && mot_p2 != null){
            pfMotor2 = ProgressFragment.newInstance(mot_p2);
            container_motors.addView(pfMotor2.getView());
        }else if(motorPort.equals(EV3PortIDs.PORT_C) && mot_p3 != null) {
            pfMotor3 = ProgressFragment.newInstance(mot_p3);
            container_motors.addView(pfMotor3.getView());
        }else if(motorPort.equals(EV3PortIDs.PORT_D) && mot_p4 != null){
            pfMotor4 = ProgressFragment.newInstance(mot_p4);
            container_motors.addView(pfMotor4.getView());
        }
    }

    public void setProgressState(String key, boolean success){
        ProgressFragment frag = null;
        switch(key){
            case KEY_PARAM_BRICK:
                frag = pfBrick;
                break;
            case KEY_PARAM_MESSENGER:
                frag = pfMessenger;
                break;
            case KEY_PARAM_SEN_P1:
                frag = pfSensor1;
                break;
            case KEY_PARAM_SEN_P2:
                frag = pfSensor2;
                break;
            case KEY_PARAM_SEN_P3:
                frag = pfSensor3;
                break;
            case KEY_PARAM_SEN_P4:
                frag = pfSensor4;
                break;
            case KEY_PARAM_MOT_A:
                frag = pfMotor1;
                break;
            case KEY_PARAM_MOT_B:
                frag = pfMotor2;
                break;
            case KEY_PARAM_MOT_C:
                frag = pfMotor3;
                break;
            case KEY_PARAM_MOT_D:
                frag = pfMotor4;
                break;
        }

        if(frag != null){
            frag.setProgressState(success);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
