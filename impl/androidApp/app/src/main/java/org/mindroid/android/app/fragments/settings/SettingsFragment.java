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
import android.widget.*;

import org.mindroid.android.app.R;
import org.mindroid.android.app.fragments.home.HomeFragment;
import org.mindroid.android.app.robodancer.ConnectionPropertiesChangedListener;
import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.android.app.serviceloader.ImplementationService;
import org.mindroid.android.app.util.IPUtils;
import org.mindroid.android.app.util.ShellService;

import java.util.ServiceLoader;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private OnSettingsChanged settingsChangedListener;
    private ConnectionPropertiesChangedListener connectionPropertiesChangedListener = SettingsProvider.getInstance();

    private Activity parentActivity;

    private final String[] languages = {"English","Deutsch"};

    // has changed
    private boolean hasChanged = false;

    //UI-Button
    private Button btn_saveSettings;

    //Switch
    private Switch usbSwitch;

    //UI-Textfield
    private TextView txtView_note;

    public EditText txt_input_robotID;
    public EditText txt_input_groupID;

    public EditText txt_input_EV3TCPPort;
    public EditText txt_input_ServerTCPPort;

    private EditText txt_input_ev3ip_part1;
    private EditText txt_input_ev3ip_part2;
    private EditText txt_input_ev3ip_part3;
    private EditText txt_input_ev3ip_part4;

    private EditText txt_input_serverip_part1;
    private EditText txt_input_serverip_part2;
    private EditText txt_input_serverip_part3;
    private EditText txt_input_serverip_part4;

    private TextView txt_program_set;
    private Spinner spinner_programs;

    //Msg showing that the messenger needs to be disconnected
    private String noteMessage;

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
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        /** INIT TextView **/
        txtView_note = (TextView) view.findViewById(R.id.txtView_note);
        noteMessage = getResources().getString(R.string.txtView_note_msg_disconnect_messenger);

        /** INIT Buttons **/
        btn_saveSettings = (Button) view.findViewById(R.id.btn_saveSettings);

        /** INIT SWITCH */
        usbSwitch = (Switch) view.findViewById(R.id.switchUSB);

        /** INIT TextEdit fields **/
        txt_input_robotID = (EditText) view.findViewById(R.id.txt_input_robotID);
        txt_input_groupID = (EditText) view.findViewById(R.id.txt_input_groupID);
        if(HomeFragment.robot.isMessengerConnected()){
            txt_input_robotID.setEnabled(false);
            txt_input_groupID.setEnabled(false);
            txtView_note.setText(noteMessage);
            txtView_note.setVisibility(View.VISIBLE);
        }else{
            txt_input_robotID.setEnabled(true);
            txt_input_groupID.setEnabled(true);
            txtView_note.setVisibility(View.GONE);
        }

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



        btn_saveSettings.setText(getResources().getString(R.string.btn_text_save_settings));

        btn_saveSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveSettings();
            }
        });

        usbSwitch.setVisibility(View.GONE);
        usbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked){
                ShellService.usbChargingControl(checked);
            }
        });

        txt_program_set = (TextView) view.findViewById(R.id.txt_program_set);
        spinner_programs = (Spinner) view.findViewById(R.id.spinner_program_set);

        spinner_programs.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ImplementationService.getInstance().getImplementationSets()));

        if(SettingsProvider.getInstance().isAdminModeUnlocked()) {
            txt_program_set.setVisibility(View.VISIBLE);
            spinner_programs.setVisibility(View.VISIBLE);
        }else{
            txt_program_set.setVisibility(View.GONE);
            spinner_programs.setVisibility(View.GONE);
        }

        spinner_programs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettingsProvider.getInstance().setSelectedProgramSet((String) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SettingsProvider.getInstance().setSelectedProgramSet(ImplementationService.getInstance().getDefaultSet());
            }
        });

        //Load saved SettingsProvider
        loadSettings();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //stopAllMotors
        hasChanged = false;

        if (context instanceof OnSettingsChanged) {
            settingsChangedListener = (OnSettingsChanged) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

            savedVal = SettingsProvider.getInstance().getMsgServerIP();
            if(!savedVal.isEmpty()){
                String[] val = savedVal.split("\\.");
                txt_input_serverip_part1.setText(val[0]);
                txt_input_serverip_part2.setText(val[1]);
                txt_input_serverip_part3.setText(val[2]);
                txt_input_serverip_part4.setText(val[3]);
            }

            savedVal = ""+SettingsProvider.getInstance().getMsgServerPort();
            txt_input_ServerTCPPort.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_MSG_SERVER_PORT) : savedVal);

            savedVal = SettingsProvider.getInstance().getRobotID();
            txt_input_robotID.setText((savedVal.isEmpty()) ? SettingsProvider.getInstance().generateUniqueRobotName() : savedVal);

            savedVal = SettingsProvider.getInstance().getGroupID();
            txt_input_groupID.setText((savedVal.isEmpty()) ? getResources().getString(R.string.DEFAULT_GROUP_ID) : savedVal);
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

        if(!HomeFragment.robot.isMessengerConnected()) {
            /** Data to connect to EV3 Brick **/
            e.putString(getResources().getString(R.string.KEY_ROBOT_ID), txt_input_robotID.getText().toString());

            /** Data to connect to Server **/
            e.putString(getResources().getString(R.string.KEY_GROUP_ID), txt_input_groupID.getText().toString());
        }

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
        //Check if robotID field is empty
        if(txt_input_robotID.getText().toString().isEmpty()){
            return false;
        }

        //check if groupID is empty
        if(txt_input_groupID.getText().toString().isEmpty()){
            return false;
        }

        String ev3IP = IPUtils.getConcatenatedIPAddress(txt_input_ev3ip_part1.getText().toString(),
                txt_input_ev3ip_part2.getText().toString(),
                txt_input_ev3ip_part3.getText().toString(),
                txt_input_ev3ip_part4.getText().toString());


        String msgServerIP = IPUtils.getConcatenatedIPAddress(txt_input_serverip_part1.getText().toString(),
                txt_input_serverip_part2.getText().toString(),
                txt_input_serverip_part3.getText().toString(),
                txt_input_serverip_part4.getText().toString());

        //Check if IPs (ev3, msgServer) are valid, if not return false
        if(!IPUtils.validateIP(ev3IP) || !IPUtils.validateIP(msgServerIP)) {
            return false;
        }
        System.out.println("SettingsFrag: IPs VALID");


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
        return serverip;
    }

}
