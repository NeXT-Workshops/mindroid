package org.mindroid.android.app.fragments.sensormonitoring;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import org.mindroid.android.app.R;
import org.mindroid.android.app.acitivites.MainActivity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static org.mindroid.android.app.fragments.sensormonitoring.SensorListener.SIZE_LAST_VALUES;

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

    private TextView txt_last_value;
    private TextView txt_avg_value;
    private TextView last_value;
    private TextView avg_value;
    private ToggleButton btn_toggle_pause;
    private LinearLayout layout_last_values;

    private OnFragmentInteractionListener mListener;

    private SensorListener sensorListener;

    private ArrayList<TextView> lastValuesViews = new ArrayList<>( SIZE_LAST_VALUES);

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor_observation, container, false);


        txt_view_sensor_port = (TextView) view.findViewById(R.id.sobs_sensor_port);
        txt_view_sensor_type = (TextView) view.findViewById(R.id.sobs_sensortype);
        txt_view_sensor_mode = (TextView) view.findViewById(R.id.sobs_sensormode);

        txt_last_value = (TextView) view.findViewById(R.id.txtView_txt_last_value);
        txt_avg_value = (TextView) view.findViewById(R.id.txtView_txt_avg_value);
        last_value = (TextView) view.findViewById(R.id.txtView_last_value);
        avg_value = (TextView) view.findViewById(R.id.txtView_avg_value);
        btn_toggle_pause = (ToggleButton) view.findViewById(R.id.btn_toggle_pause);
        layout_last_values = (LinearLayout) view.findViewById(R.id.linLayout_last_values);

        txt_view_sensor_port.setText(getArguments().getString(ARG_PORT));
        txt_view_sensor_type.setText(getArguments().getString(ARG_TYPE));
        txt_view_sensor_mode.setText(getArguments().getString(ARG_MODE));

        btn_toggle_pause.setVisibility(View.GONE);

        for (int i = 0; i < SensorListener.SIZE_LAST_VALUES; i++) {
            lastValuesViews.add(new TextView(getContext()));
            lastValuesViews.get(i).setText("-1");
            lastValuesViews.get(i).setWidth(1000/SensorListener.SIZE_LAST_VALUES);
            layout_last_values.addView(lastValuesViews.get(i).getRootView());
        }

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
            public void run(){
                for (int i = 0; i < lastValuesViews.size(); i++) {
                    lastValuesViews.get(i).setText(sensorListener.getLastValueAt(i)+"");
                }
                last_value.setText(sensorListener.getLastValue()+"");
                avg_value.setText(sensorListener.getAvgValue()+"");

            }
        };
        return updateUI;
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
