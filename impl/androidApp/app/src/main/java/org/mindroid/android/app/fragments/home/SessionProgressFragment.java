package org.mindroid.android.app.fragments.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.mindroid.android.app.R;
import org.mindroid.impl.ev3.EV3PortIDs;

public class SessionProgressFragment extends DialogFragment {

    private static final String KEY_TITLE = "TITLE";

    private static SessionProgressTask parentTask;

    private Dialog dialog;
    private TextView txtView_sessionState;
    private TextView txtView_sessionSize;

    private View view;

    public SessionProgressFragment() {
        // Required empty public constructor
    }

    public static SessionProgressFragment newInstance(String title, Bundle configBundle,SessionProgressTask parent) {
        SessionProgressFragment fragment = new SessionProgressFragment();
        configBundle.putString(KEY_TITLE,title);
        fragment.setArguments(configBundle);
        parentTask = parent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                .setNegativeButton(getResources().getString(R.string.txt_abort),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Interrupt Task using this dialog
                        parentTask.interrupt();
                    }
                }

        )
        .setView(getCustomView());

        return builder.create();
    }

    /**
     *
     * @return view of the Dialog shown by this fragment
     */
    public View getCustomView(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_session_progress, null);

        txtView_sessionState = (TextView) view.findViewById(R.id.txtView_sessionState);
        txtView_sessionSize = (TextView) view.findViewById(R.id.txtView_sessionSize);

        // Inflate the layout for this fragment
        return (this.view = view);
    }


    public void setProgressState(final String state, final int currentSize, final int sessionMaxSize){
        Runnable updateView = new Runnable() {
            @Override
            public void run() {
                if(txtView_sessionState != null && txtView_sessionSize != null) {
                    txtView_sessionState.setText(state);
                    txtView_sessionSize.setText(String.valueOf(currentSize).concat("/").concat(String.valueOf(sessionMaxSize)));
                }
            }
        };
        view.post(updateView);
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
