package org.mindroid.android.app.fragments.sensormonitoring;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import org.mindroid.android.app.R;

import java.util.Observable;
import java.util.Observer;

import static org.mindroid.common.messages.SensorMessages.*;

/**
 * Fragment displaying data of single Sensor
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorObservationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorObservationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorObservationFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PORT = "port";
    private static final String ARG_TYPE = "sensortype";
    private static final String ARG_MODE = "sensormode";

    // TODO: Rename and change types of parameters
    private String port;
    private String sensortype;
    private String sensormode;

    // UI Elements
    private TextView txt_view_sensor_port;
    private TextView txt_view_sensor_type;
    private TextView txt_view_sensor_mode;

    private TextView txt_view_slot1_description;
    private TextView txt_view_slot1_value;
    private TextView txt_view_slot2_description;
    private TextView txt_view_slot2_value;
    private TextView txt_view_slot3_description;
    private TextView txt_view_slot3_value;
    private TextView txt_view_slot4_description;
    private TextView txt_view_slot4_value;

    //mode descrition strings
    private String descr_rgb_slot1;
    private String descr_rgb_slot2;
    private String descr_rgb_slot3;
    private String descr_ambient_slot1;
    private String descr_red_slot1;
    private String descr_color_slot1;
    private String descr_angle_slot1;
    private String descr_rate_slot1;
    private String descr_rateAndAngle_slot1;
    private String descr_rateAndAngle_slot2;
    private String descr_listen_slot1;
    private String descr_distance_slot1;
    private String descr_touch_slot1;
    private String descr_seek_slot1;
    private String descr_seek_slot2;
    private String descr_seek_slot3;
    private String descr_seek_slot4;
    private final String descr_nothing = "";


    private OnFragmentInteractionListener mListener;

    private SensorListener sensorListener;


    public SensorObservationFragment() {
        // Required empty public constructor
    }

    protected void registerSensorListener(SensorListener sensorListener){
        this.sensorListener = sensorListener;
        sensorListener.addObserver(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param port port of the sensor.
     * @param sensortype sensortype
     * @param sensormode mode of sensor
     * @return A new instance of fragment SensorObservationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorObservationFragment newInstance(String port, String sensortype, String sensormode) {
        SensorObservationFragment fragment = new SensorObservationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PORT, port);
        args.putString(ARG_TYPE, sensortype);
        args.putString(ARG_MODE, sensormode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            port = getArguments().getString(ARG_PORT);
            sensortype = getArguments().getString(ARG_TYPE);
            sensormode = getArguments().getString(ARG_MODE);
        }

        //Get Resources
        descr_rgb_slot1 = getResources().getString(R.string.txt_sensor_description_mode_rgb_slot1);
        descr_rgb_slot2 = getResources().getString(R.string.txt_sensor_description_mode_rgb_slot2);
        descr_rgb_slot3 = getResources().getString(R.string.txt_sensor_description_mode_rgb_slot3);

        descr_ambient_slot1 = getResources().getString(R.string.txt_sensor_description_mode_ambient_slot1);

        descr_red_slot1 = getResources().getString(R.string.txt_sensor_description_mode_red_slot1);

        descr_color_slot1 = getResources().getString(R.string.txt_sensor_description_mode_color_slot1);
        descr_angle_slot1 = getResources().getString(R.string.txt_sensor_description_mode_angle_slot1);
        descr_rate_slot1 = getResources().getString(R.string.txt_sensor_description_mode_rate_slot1);

        descr_rateAndAngle_slot1 = getResources().getString(R.string.txt_sensor_description_mode_angle_and_rate_slot1);
        descr_rateAndAngle_slot2 = getResources().getString(R.string.txt_sensor_description_mode_angle_and_rate_slot2);

        descr_listen_slot1 = getResources().getString(R.string.txt_sensor_description_mode_listen_slot1);
        descr_distance_slot1 = getResources().getString(R.string.txt_sensor_description_mode_distance_slot1);
        descr_touch_slot1 = getResources().getString(R.string.txt_sensor_description_mode_touch_slot1);

        descr_seek_slot1 = getResources().getString(R.string.txt_sensor_description_mode_seek_slot1);
        descr_seek_slot2 = getResources().getString(R.string.txt_sensor_description_mode_seek_slot2);
        descr_seek_slot3 = getResources().getString(R.string.txt_sensor_description_mode_seek_slot3);
        descr_seek_slot4 = getResources().getString(R.string.txt_sensor_description_mode_seek_slot4);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor_observation, container, false);

        //reset sensor listener
        sensorListener.reset();

        //Get UI
        txt_view_sensor_port = (TextView) view.findViewById(R.id.sobs_sensor_port);
        txt_view_sensor_type = (TextView) view.findViewById(R.id.sobs_sensortype);
        txt_view_sensor_mode = (TextView) view.findViewById(R.id.sobs_sensormode);

        txt_view_slot1_description = (TextView) view.findViewById(R.id.txtView_slot1_description);
        txt_view_slot1_value = (TextView) view.findViewById(R.id.txtView_slot1_value);

        txt_view_slot2_description = (TextView) view.findViewById(R.id.txtView_slot2_description);
        txt_view_slot2_value = (TextView) view.findViewById(R.id.txtView_slot2_value);

        txt_view_slot3_description = (TextView) view.findViewById(R.id.txtView_slot3_description);
        txt_view_slot3_value = (TextView) view.findViewById(R.id.txtView_slot3_value);

        txt_view_slot4_description = (TextView) view.findViewById(R.id.txtView_slot4_description);
        txt_view_slot4_value = (TextView) view.findViewById(R.id.txtView_slot4_value);

        txt_view_sensor_port.setText(getArguments().getString(ARG_PORT));
        txt_view_sensor_type.setText(getArguments().getString(ARG_TYPE));
        txt_view_sensor_mode.setText(getArguments().getString(ARG_MODE));

        setTextViewVisibility(sensorListener.getValueSize());
        updateTextViewsText(sensorListener.getValueSize());

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
    public void update(Observable o, Object arg) {
        if(this.isVisible()) {
            getActivity().runOnUiThread(updateUI());
        }
    }

    private Runnable updateUI(){
        Runnable updateUI = new Runnable(){
            @Override
            public void run() {
                setTextViewVisibility(sensorListener.getValueSize());
                updateTextViewsText(sensorListener.getValueSize());
                System.out.println("VALUE SIZE: "+sensorListener.getValueSize());
            }

        };
        return updateUI;
    }

    private void updateTextViewsText(int size){

        if(size >= 1) {
            txt_view_slot1_description.setText(getDescriptionText(1));
            txt_view_slot1_value.setText(String.format("%1$g",sensorListener.getValue(0)));
        }else{
            txt_view_slot1_description.setText(descr_nothing);
            txt_view_slot1_value.setText(descr_nothing);
        }

        if(size >= 2) {
            txt_view_slot2_description.setText(getDescriptionText(2));
            txt_view_slot2_value.setText(String.format("%1$g",sensorListener.getValue(1)));
        }else{
            txt_view_slot2_description.setText(descr_nothing);
            txt_view_slot2_value.setText(descr_nothing);
        }

        if(size >= 3) {
            txt_view_slot3_description.setText(getDescriptionText(3));
            txt_view_slot3_value.setText(String.format("%1$g",sensorListener.getValue(2)));
        }else{
            txt_view_slot3_description.setText(descr_nothing);
            txt_view_slot3_value.setText(descr_nothing);
        }

        if(size >= 4) {
            txt_view_slot4_description.setText(getDescriptionText(4));
            txt_view_slot4_value.setText(String.format("%1$g",sensorListener.getValue(3)));
        }else{
            txt_view_slot3_description.setText(descr_nothing);
            txt_view_slot3_value.setText(descr_nothing);
        }
    }

    private String getDescriptionText(int slot) {
        System.out.println("Descriptionsize slot "+slot);
        SensorMode_ mode = sensorListener.getMode();
        if(mode != null) {
            if (mode.equals(SensorMode_.RGB)) {
                return getRGBDescription(slot);
            } else if (mode.equals(SensorMode_.AMBIENT)) {
                return getAmbientDescription(slot);
            } else if (mode.equals(SensorMode_.RED)) {
                return getRedDescription(slot);
            } else if (mode.equals(SensorMode_.COLOR_ID)) {
                return getColorIdDescription(slot);
            } else if (mode.equals(SensorMode_.ANGLE)) {
                return getAngleDescription(slot);
            } else if (mode.equals(SensorMode_.RATE)) {
                return getRateDescription(slot);
            } else if (mode.equals(SensorMode_.RATEANDANGLE)) {
                return getRateAndAngleDescription(slot);
            } else if (mode.equals(SensorMode_.LISTEN)) {
                return getListenDescription(slot);
            } else if (mode.equals(SensorMode_.DISTANCE)) {
                return getDistanceDescription(slot);
            } else if (mode.equals(SensorMode_.TOUCH)) {
                return getTouchDescription(slot);
            } else if (mode.equals(SensorMode_.SEEK)) {
                return getSeekDescription(slot);
            }
        }
        return descr_nothing;
    }

    private String getRGBDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_rgb_slot1;
            case 2:
                return descr_rgb_slot2;
            case 3:
                return descr_rgb_slot3;
            default:
                return descr_nothing;
        }
    }

    private String getAmbientDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_ambient_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getRedDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_red_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getColorIdDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_color_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getAngleDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_angle_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getRateDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_rate_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getRateAndAngleDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_rateAndAngle_slot1;
            case 2:
                return descr_rateAndAngle_slot2;
            default:
                return descr_nothing;
        }
    }

    private String getListenDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_listen_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getDistanceDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_distance_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getTouchDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_touch_slot1;
            default:
                return descr_nothing;
        }
    }

    private String getSeekDescription(int slot) {
        switch (slot) {
            case 1:
                return descr_seek_slot1;
            case 2:
                return descr_seek_slot2;
            case 3:
                return descr_seek_slot3;
            case 4:
                return descr_seek_slot4;
            default:
                return descr_nothing;
        }
    }


    private void setTextViewVisibility(int size){
        switch(size){
            case 1:
                showTextView(true,false,false,false);
                break;
            case 2:
                showTextView(true,true,false,false);
                break;
            case 3:
                showTextView(true,true,true,false);
                break;
            case 4:
                showTextView(true,true,true,true);
                break;
            default:
                showTextView(false,false,false,false);
                break;
        }
    }

    private void showTextView(boolean enableSlot1, boolean enableSlot2,boolean enableSlot3,boolean enableSlot4){
        if(enableSlot1) {
            txt_view_slot1_description.setVisibility(View.VISIBLE);
            txt_view_slot1_value.setVisibility(View.VISIBLE);
        }else{
            txt_view_slot1_description.setVisibility(View.INVISIBLE);
            txt_view_slot1_value.setVisibility(View.INVISIBLE);
        }

        if(enableSlot2) {
            txt_view_slot2_description.setVisibility(View.VISIBLE);
            txt_view_slot2_value.setVisibility(View.VISIBLE);
        }else{
            txt_view_slot2_description.setVisibility(View.INVISIBLE);
            txt_view_slot2_value.setVisibility(View.INVISIBLE);
        }
        if(enableSlot3) {
            txt_view_slot3_description.setVisibility(View.VISIBLE);
            txt_view_slot3_value.setVisibility(View.VISIBLE);
        }else{
            txt_view_slot3_description.setVisibility(View.INVISIBLE);
            txt_view_slot3_value.setVisibility(View.INVISIBLE);
        }

        if(enableSlot4) {
            txt_view_slot4_description.setVisibility(View.VISIBLE);
            txt_view_slot4_value.setVisibility(View.VISIBLE);
        }else{
            txt_view_slot4_description.setVisibility(View.INVISIBLE);
            txt_view_slot4_value.setVisibility(View.INVISIBLE);
        }
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

    /**
     * Async task updateing the ui
     */
    public class UpdateUITask extends AsyncTask<String,Integer,Boolean> {
        public UpdateUITask(){

        }


        @Override
        protected Boolean doInBackground(String... params){

            return true;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final Boolean success){

        }



    }
}
