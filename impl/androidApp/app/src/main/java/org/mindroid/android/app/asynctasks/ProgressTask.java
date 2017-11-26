package org.mindroid.android.app.asynctasks;


import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Torbe on 15.05.2017.
 */

public abstract class ProgressTask extends AsyncTask<String,Integer,Boolean>{

    org.mindroid.android.app.dialog.ProgressDialog dialog;
    private final FragmentManager fManager;

    private final String progressMsg;
    private final String title;


    public ProgressTask(FragmentManager fManager,String title,String progressMsg){
        this.fManager = fManager;
        this.progressMsg = progressMsg;
        this.title = title;

        dialog = org.mindroid.android.app.dialog.ProgressDialog.newInstance(title,progressMsg);
    }

    @Override
    protected void onPreExecute() {
        dialog.show(fManager,"ProgressDialog");
    }

    @Override
    protected void onPostExecute(final Boolean success){
        dialog.dismiss();
    }

    @Override
    protected abstract Boolean doInBackground(String... params);

}
