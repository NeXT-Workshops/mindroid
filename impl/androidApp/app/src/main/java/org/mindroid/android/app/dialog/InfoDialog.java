package org.mindroid.android.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import org.mindroid.android.app.R;

public class InfoDialog extends DialogFragment {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_MESSAGE = "MESSAGE";

    public static ErrorDialog newInstance(String title, String message) {
        ErrorDialog frag = new ErrorDialog();
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

        this.setCancelable(false);

        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_info)
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
