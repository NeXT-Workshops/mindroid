package org.mindroid.android.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import org.mindroid.android.app.R;

public class DemoDialogFragment extends DialogFragment {

    public DemoDialogFragment() {
        // Required empty public constructor
    }

    public static DemoDialogFragment newInstance() {
        DemoDialogFragment fragment = new DemoDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                .setNegativeButton(getResources().getString(R.string.txt_abort),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Interrupt Task using this dialog
                                dismiss();
                            }
                        }

                )
                .setView(getCustomView())
                .setCancelable(false);


        return builder.create();
    }

    /**
     *
     * @return view of the Dialog shown by this fragment
     */
    public View getCustomView(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_demo_dialog, null);
        //TODO change layout

        //txtView_sessionState = (TextView) view.findViewById(R.id.txtView_sessionState);
        //txtView_sessionSize = (TextView) view.findViewById(R.id.txtView_sessionSize);

        // Inflate the layout for this fragment
        return view;
    }
}
