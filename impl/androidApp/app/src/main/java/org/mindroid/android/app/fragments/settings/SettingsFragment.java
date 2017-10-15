package org.mindroid.android.app.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.mindroid.android.app.R;
import org.mindroid.android.app.acitivites.MainActivity;
import org.mindroid.android.app.robodancer.ConnectionPropertiesChangedListener;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;

import java.io.IOException;
import java.util.Locale;

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
    private ConnectionPropertiesChangedListener connectionPropertiesChangedListener = SettingsProvider.getInstance();

    private Activity parentActivity;

    private final String[] languages = {"English","Deutsch"};

    // has changed
    private boolean hasChanged = false;

    //UI-Button
    private Button btn_saveSettings;

    //UI-Textfield
    private TextView txtView_language;

    public EditText txt_input_robotID;
    public EditText txt_input_groupID;

    public EditText txt_input_EV3TCPPort;
    public EditText txt_input_ServerTCPPort;
    public EditText txt_input_robotServerPort;

    private EditText txt_input_ev3ip_part1;
    private EditText txt_input_ev3ip_part2;
    private EditText txt_input_ev3ip_part3;
    private EditText txt_input_ev3ip_part4;

    private EditText txt_input_serverip_part1;
    private EditText txt_input_serverip_part2;
    private EditText txt_input_serverip_part3;
    private EditText txt_input_serverip_part4;

    private Spinner spinner_language;
    private Switch switch_chargePhone;


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
     * EditText@param param1 Parameter 1.
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

        txt_input_ev3ip_part1 = (EditText) view.findViewById(R.id.txt_input_ev3ip_part1);
        txt_input_ev3ip_part2 = (EditText) view.findViewById(R.id.txt_input_ev3ip_part2);
        txt_input_ev3ip_part3 = (EditText) view.findViewById(R.id.txt_input_ev3ip_part3);
        txt_input_ev3ip_part4 = (EditText) view.findViewById(R.id.txt_input_ev3ip_part4);
        txt_input_EV3TCPPort = (EditText) view.findViewById(R.id.txt_input_EV3TCPPort);

        txt_input_serverip_part1 = (EditText) view.findViewById(R.id.txt_input_msg_serverip_part1);
        txt_input_serverip_part2 = (EditText) view.findViewById(R.id.txt_input_msg_serverip_part2);
        txt_input_serverip_part3 = (EditText) view.findViewById(R.id.txt_input_msg_serverip_part3);
        txt_input_serverip_part4 = (EditText) view.findViewById(R.id.txt_input_msg_serverip_part4);
        txt_input_ServerTCPPort = (EditText) view.findViewById(R.id.txt_input_ServerTCPPort);
        txt_input_robotServerPort = (EditText) view.findViewById(R.id.txt_input_robotServerPort);
        txtView_language = (TextView) view.findViewById(R.id.txtView_language);

        /** Set ports **/
        String[] def_ev3ip = getResources().getString(R.string.DEFAULT_EV3_BRICK_IP).split("\\.");
        txt_input_ev3ip_part1.setText(def_ev3ip[0]);
        txt_input_ev3ip_part2.setText(def_ev3ip[1]);
        txt_input_ev3ip_part3.setText(def_ev3ip[2]);
        txt_input_ev3ip_part4.setText(def_ev3ip[3]);
        txt_input_EV3TCPPort.setText(R.string.DEFAULT_EV3_BRICK_PORT);

        String[] dev_serverip = getResources().getString(R.string.DEFAULT_MSG_SERVER_IP).split("\\.");
        txt_input_serverip_part1.setText(dev_serverip[0]);
        txt_input_serverip_part2.setText(dev_serverip[1]);
        txt_input_serverip_part3.setText(dev_serverip[2]);
        txt_input_serverip_part4.setText(dev_serverip[3]);

        txt_input_ServerTCPPort.setText(R.string.DEFAULT_MSG_SERVER_PORT);
        txt_input_robotServerPort.setText(R.string.DEFAULT_BRICK_MSG_SERVER_PORT);

        //Charging phone switch
        initChargePhoneSwitch(view);

        //Language spinner
        spinner_language = (Spinner) view.findViewById(R.id.spinner_language);
        spinner_language.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, languages));
        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        int count = 0;
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(count >= 1) {
                    switch(pos){
                        case 0: changeLanguage(Locale.ENGLISH); break;
                        case 1: changeLanguage(Locale.GERMAN); break;
                    }

                }else{
                    count++;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
        spinner_language.setVisibility(View.GONE);
        txtView_language.setVisibility(View.GONE);


        btn_saveSettings.setText(getResources().getString(R.string.btn_text_save_settings));

        btn_saveSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveSettings();
            }
        });

        //Load saved SettingsProvider
        loadSettings();

        return view;
    }

    /**
     * Initializes the switch to enable/disable phone chargin
     * @param view view
     */
    private void initChargePhoneSwitch(View view) {
        switch_chargePhone = (Switch) view.findViewById(R.id.switch_chargePhone);
        switch_chargePhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    ProcessBuilder proBuilder = new ProcessBuilder();
                    proBuilder.command("su").start();
                    if(switch_chargePhone.isChecked()){
                        //enable charge phone
                        proBuilder.command("echo 1 > /sys/class/power_supply/battery/charging_enabled").start();
                    }else{
                        //disable charge phone
                        proBuilder.command("echo 0 > /sys/class/power_supply/battery/charging_enabled").start();
                    }
                    //proBuilder.start();
                } catch (IOException e) {
                        ErrorHandlerManager.getInstance().handleError(e,this.getClass(),e.toString());
                }
            }
        });

        //Disabled switch, because it is not working as intended:
        switch_chargePhone.setVisibility(View.GONE);
    }

    private void changeLanguage(Locale localLang) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setLocale(localLang);
        }
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
            String savedVal = SettingsProvider.getInstance().getEv3IP();
            if(!savedVal.isEmpty()){
                String[] val = savedVal.split("\\.");
                txt_input_ev3ip_part1.setText(val[0]);
                txt_input_ev3ip_part2.setText(val[1]);
                txt_input_ev3ip_part3.setText(val[2]);
                txt_input_ev3ip_part4.setText(val[3]);
            }

            savedVal = ""+SettingsProvider.getInstance().getEv3TCPPort();
            txt_input_EV3TCPPort.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_EV3_BRICK_PORT) : savedVal);

            savedVal = SettingsProvider.getInstance().getServerIP();
            if(!savedVal.isEmpty()){
                String[] val = savedVal.split("\\.");
                txt_input_serverip_part1.setText(val[0]);
                txt_input_serverip_part2.setText(val[1]);
                txt_input_serverip_part3.setText(val[2]);
                txt_input_serverip_part4.setText(val[3]);
            }

            savedVal = ""+SettingsProvider.getInstance().getServerTCPPort();
            txt_input_ServerTCPPort.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_MSG_SERVER_PORT) : savedVal);

            savedVal = SettingsProvider.getInstance().getRobotID();
            txt_input_robotID.setText((savedVal.isEmpty()) ? SettingsProvider.getInstance().generateUniqueRobotName() : savedVal);

            savedVal = SettingsProvider.getInstance().getGroupID();
            txt_input_groupID.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_GROUP_ID) : savedVal);

            savedVal = ""+SettingsProvider.getInstance().getRobotServerPort();
            txt_input_robotServerPort.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_BRICK_MSG_SERVER_PORT) : savedVal);
    }

    private void saveSettings(){
        if(!validateSettings()){
            showShortToast(getActivity(),getResources().getString(R.string.msg_toast_settings_invalid_input));
            return;
        }

        SharedPreferences.Editor e = parentActivity.getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_pref_connection_Data),Context.MODE_PRIVATE).edit();
        e.clear();

        /** Data to connect to EV3 Brick **/
        e.putString(getResources().getString(R.string.KEY_EV3_IP),getInputEV3IP());
        e.putString(getResources().getString(R.string.KEY_EV3_TCP_PORT),txt_input_EV3TCPPort.getText().toString());


        /** Data to connect to Server **/
        e.putString(getResources().getString(R.string.KEY_SERVER_IP),getInputServerIP());
        e.putString(getResources().getString(R.string.KEY_SERVER_TCP_PORT),txt_input_ServerTCPPort.getText().toString());

        /** Data to connect to EV3 Brick **/
        e.putString(getResources().getString(R.string.KEY_ROBOT_ID),txt_input_robotID.getText().toString());

        /** Data to connect to Server **/
        e.putString(getResources().getString(R.string.KEY_GROUP_ID),txt_input_groupID.getText().toString());

        e.putString(getResources().getString(R.string.KEY_ROBOT_SERVER_TCP_PORT),txt_input_robotServerPort.getText().toString());

        e.commit();

        showShortToast(parentActivity,getResources().getString(R.string.msg_taost_settings_saved));

        // Inform listener that the settings have changed
        connectionPropertiesChangedListener.onConnectionPropertiesChangedListener();

        //Call this listener (MainActivity) to close the Settings fragment an display the HomeFragment
        settingsChangedListener.onSettingsChanged(true);
    }


    /**
     * Checks if the SettingsProvider input values are valid
     *
     * @return
     */
    private boolean validateSettings() {
        String ipRegex = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"; //
        //Validate EV3IP
        if(! (txt_input_ev3ip_part1.getText().toString().matches(ipRegex) &&
                txt_input_ev3ip_part2.getText().toString().matches(ipRegex) &&
                txt_input_ev3ip_part3.getText().toString().matches(ipRegex) &&
                txt_input_ev3ip_part4.getText().toString().matches(ipRegex))){
            return false;
        }

        if(! (txt_input_serverip_part1.getText().toString().matches(ipRegex) &&
                txt_input_serverip_part2.getText().toString().matches(ipRegex) &&
                txt_input_serverip_part3.getText().toString().matches(ipRegex) &&
                txt_input_serverip_part4.getText().toString().matches(ipRegex))){
            return false;
        }

        //TODO complete?
        return true;
    }

    private void showShortToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * Concats the Text-Fields of the EV3-IP and returns a string in ip format
     *
     * @return
     */
    private String getInputEV3IP() {
        String ev3ip =ev3ip = "";
        ev3ip = ev3ip.concat(txt_input_ev3ip_part1.getText().toString());
        ev3ip = ev3ip.concat(".");
        ev3ip = ev3ip.concat(txt_input_ev3ip_part2.getText().toString());
        ev3ip = ev3ip.concat(".");
        ev3ip = ev3ip.concat(txt_input_ev3ip_part3.getText().toString());
        ev3ip = ev3ip.concat(".");
        ev3ip = ev3ip.concat(txt_input_ev3ip_part4.getText().toString());
        System.out.println(" ## EV3ip: "+ev3ip);
        return ev3ip;
    }

    /**
     * Concats the Text-Fields of the Msg-Server-IP and returns a string in ip format
     *
     * @return
     */
    private String getInputServerIP() {
        String serverip = "";
        serverip = serverip.concat(txt_input_serverip_part1.getText().toString());
        serverip = serverip.concat(".");
        serverip = serverip.concat(txt_input_serverip_part2.getText().toString());
        serverip = serverip.concat(".");
        serverip = serverip.concat(txt_input_serverip_part3.getText().toString());
        serverip = serverip.concat(".");
        serverip = serverip.concat(txt_input_serverip_part4.getText().toString());
        System.out.println(" ## ServerIP: "+serverip);
        return serverip;
    }

}
