package org.mindroid.android.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ProgressBar;

public class ProgressDialog extends DialogFragment {


    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_MESSAGE = "MESSAGE";

    public static ProgressDialog newInstance(String title, String message) {
        ProgressDialog frag = new ProgressDialog();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(KEY_TITLE);
        String msg = getArguments().getString(KEY_MESSAGE);



        android.app.ProgressDialog.Builder builder = new android.app.ProgressDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(title)
                .setMessage(msg);

        ProgressBar pb = new ProgressBar(builder.getContext());

        builder.setView(pb);

        return builder.create();
    }
}
