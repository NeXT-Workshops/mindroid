package org.mindroid.android.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import org.mindroid.android.app.R;

/**
 * Created by Torbe on 22.05.2017.
 */

public class ErrorDialog extends DialogFragment {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_MESSAGE = "MESSAGE";

    public static ErrorDialog newInstance(String title, String message) {
        ErrorDialog frag = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title+"new dialog");
        args.putString(KEY_MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(KEY_TITLE);
        String msg = getArguments().getString(KEY_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .setTitle(title)
                .setMessage(msg)
                .create();
    }

}
