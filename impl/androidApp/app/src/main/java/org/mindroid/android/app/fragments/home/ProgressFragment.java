package org.mindroid.android.app.fragments.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.mindroid.android.app.R;
import org.mindroid.android.app.acitivites.MainActivity;

public class ProgressFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_INFO_TEXT = "ARG_INFO_TEXT";

    // TODO: Rename and change types of parameters
    private String infoText;

    private ImageView imgView_success;
    private ImageView imgView_fail;
    private ProgressBar progressBar;
    private TextView txtView_infoText;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stateText shown information text
     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance(String stateText) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_INFO_TEXT, stateText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            infoText = getArguments().getString(ARG_PARAM_INFO_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_progress, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        imgView_success = (ImageView) view.findViewById(R.id.imgView_success);
        imgView_fail = (ImageView) view.findViewById(R.id.imgView_failed);
        txtView_infoText = (TextView) view.findViewById(R.id.txtView_stateInfo);

        txtView_infoText.setText(infoText);

        imgView_success.setVisibility(View.GONE);
        imgView_fail.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setProgressState(final boolean success){
        Runnable updateUI = new Runnable(){
            @Override
            public void run(){
                if(success){
                    imgView_success.setVisibility(View.VISIBLE);
                    imgView_fail.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }else{
                    imgView_success.setVisibility(View.GONE);
                    imgView_fail.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        getActivity().runOnUiThread(updateUI);
    }

}
