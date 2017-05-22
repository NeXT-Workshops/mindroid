package org.mindroid.android.app.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mindroid.android.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private OnSettingsChanged settingsChangedListener;

    private Activity parentActivity;

    // has changed
    private boolean hasChanged = false;

    //UI-Button
    private Button btn_saveSettings;

    //UI-Textfield
    public static EditText txt_input_robotID;
    public static EditText txt_input_groupID;

    public static EditText txt_input_EV3IP;
    public static EditText txt_input_ServerIP;

    public static EditText txt_input_EV3TCPPort;
    public static EditText txt_input_ServerTCPPort;
    public static EditText txt_input_robotServerPort;



    public SettingsFragment() {
        // Required empty public constructor
    }

    public interface OnSettingsChanged {
        public void onSettingsChanged(boolean settingsChanged);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        /** INIT Buttons **/
        btn_saveSettings = (Button) view.findViewById(R.id.btn_saveSettings);

        /** INIT TextEdit fields **/
        txt_input_robotID = (EditText) view.findViewById(R.id.txt_input_robotID);
        txt_input_groupID = (EditText) view.findViewById(R.id.txt_input_groupID);

        txt_input_EV3IP = (EditText) view.findViewById(R.id.txt_input_ev3IP);
        txt_input_EV3TCPPort = (EditText) view.findViewById(R.id.txt_input_EV3TCPPort);

        txt_input_ServerIP = (EditText) view.findViewById(R.id.txt_input_ServerIP);
        txt_input_ServerTCPPort = (EditText) view.findViewById(R.id.txt_input_ServerTCPPort);
        txt_input_robotServerPort = (EditText) view.findViewById(R.id.txt_input_robotServerPort);

        /** Set ports **/
        txt_input_EV3IP.setText(R.string.DEFAULT_EV3_BRICK_IP);
        txt_input_EV3TCPPort.setText(R.string.DEFAULT_EV3_BRICK_PORT);

        txt_input_ServerIP.setText(R.string.DEFAULT_MSG_SERVER_IP);
        txt_input_ServerTCPPort.setText(R.string.DEFAULT_MSG_SERVER_PORT);
        txt_input_robotServerPort.setText(R.string.DEFAULT_BRICK_MSG_SERVER_PORT);

        btn_saveSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveSettings();
            }
        });

        //Load saved Settings
        loadSettings();

        return view;
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

        //reset
        hasChanged = false;

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            settingsChangedListener = (OnSettingsChanged) context;
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

    private void loadSettings(){
        SharedPreferences connectionProperties = parentActivity.getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_connection_Data),Context.MODE_PRIVATE);
        if(connectionProperties != null) {
            String savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_EV3_IP), getResources().getString(R.string.DEFAULT_EV3_BRICK_IP));
            txt_input_EV3IP.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_EV3_BRICK_IP) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_EV3_TCP_PORT), getResources().getString(R.string.DEFAULT_EV3_BRICK_PORT));
            txt_input_EV3TCPPort.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_EV3_BRICK_PORT) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_SERVER_IP), getResources().getString(R.string.DEFAULT_MSG_SERVER_IP));
            txt_input_ServerIP.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_MSG_SERVER_IP) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_SERVER_TCP_PORT), getResources().getString(R.string.DEFAULT_MSG_SERVER_PORT));
            txt_input_ServerTCPPort.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_MSG_SERVER_PORT) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_ROBOT_ID), getResources().getString(R.string.DEFAULT_ROBOT_ID));
            txt_input_robotID.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_ROBOT_ID) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_GROUP_ID), getResources().getString(R.string.DEFAULT_GROUP_ID));
            txt_input_groupID.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_GROUP_ID) : savedVal);

            savedVal = connectionProperties.getString(getResources().getString(R.string.KEY_ROBOT_SERVER_TCP_PORT), getResources().getString(R.string.DEFAULT_BRICK_MSG_SERVER_PORT));
            txt_input_robotServerPort.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_BRICK_MSG_SERVER_PORT) : savedVal);
        }

    }

    private void saveSettings(){

        SharedPreferences.Editor e = parentActivity.getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_connection_Data),Context.MODE_PRIVATE).edit();
        e.clear();

        /** Data to connect to EV3 Brick **/
        e.putString(getResources().getString(R.string.KEY_EV3_IP),txt_input_EV3IP.getText().toString());
        e.putString(getResources().getString(R.string.KEY_EV3_TCP_PORT),txt_input_EV3TCPPort.getText().toString());

        /** Data to connect to Server **/
        e.putString(getResources().getString(R.string.KEY_SERVER_IP),txt_input_ServerIP.getText().toString());
        e.putString(getResources().getString(R.string.KEY_SERVER_TCP_PORT),txt_input_ServerTCPPort.getText().toString());

        /** Data to connect to EV3 Brick **/
        e.putString(getResources().getString(R.string.KEY_ROBOT_ID),txt_input_robotID.getText().toString());

        /** Data to connect to Server **/
        e.putString(getResources().getString(R.string.KEY_GROUP_ID),txt_input_groupID.getText().toString());

        e.putString(getResources().getString(R.string.KEY_ROBOT_SERVER_TCP_PORT),txt_input_robotServerPort.getText().toString());

        e.commit();

        showShortToast(parentActivity,getResources().getString(R.string.msg_taost_settings_saved));

        settingsChangedListener.onSettingsChanged(true); //TODO check if settings have really changed
    }

    private void showShortToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
