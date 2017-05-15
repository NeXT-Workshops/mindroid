package org.mindroid.android.app.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Torbe on 15.05.2017.
 */

public abstract class ProgressTask extends AsyncTask<String,Integer,Boolean>{

    ProgressDialog dialog;

    Context context;

    public ProgressTask(Context context,String progressMsg){
        this.context = context;
        dialog = new ProgressDialog(context);
        this.dialog.setMessage(progressMsg);
        dialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean success){
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected abstract Boolean doInBackground(String... params);

}
