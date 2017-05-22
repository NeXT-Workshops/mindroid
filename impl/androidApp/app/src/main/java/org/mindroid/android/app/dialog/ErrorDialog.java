package org.mindroid.android.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.mindroid.android.app.R;

/**
 * Created by Torbe on 22.05.2017.
 */

public class ErrorDialog extends AlertDialog {

    protected ErrorDialog(Context context) {
        super(context);
    }

    public static AlertDialog newInstance(Context context,String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);



        builder.setPositiveButton(R.string.text_ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(context.getResources().getDrawable(android.R.drawable.ic_dialog_alert,context.getTheme()));

        return builder.create();
    }
}
