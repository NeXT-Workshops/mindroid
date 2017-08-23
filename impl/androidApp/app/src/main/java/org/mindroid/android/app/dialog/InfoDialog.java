package org.mindroid.android.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.mindroid.android.app.R;

public class InfoDialog extends AlertDialog {

    protected InfoDialog(Context context) {
        super(context);
    }

    public static AlertDialog newInstance(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton(R.string.text_ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(context.getResources().getDrawable(android.R.drawable.ic_dialog_info,context.getTheme()));

        return builder.create();
    }
}
