package org.mindroid.android.app.fragments.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class ConnectionProgressDialog extends DialogFragment {


    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_BUNDLE = "BUNDLE";

    private static ConnectionProgressFragment fragment;

    public static org.mindroid.android.app.dialog.ProgressDialog newInstance(String title, ConnectionProgressFragment cpFragment) {
        org.mindroid.android.app.dialog.ProgressDialog frag = new org.mindroid.android.app.dialog.ProgressDialog();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        fragment = cpFragment;
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(KEY_TITLE);
        Bundle bundle = getArguments().getBundle(KEY_BUNDLE);

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(title)
                .setView(fragment.getView());

        return builder.create();
    }


}