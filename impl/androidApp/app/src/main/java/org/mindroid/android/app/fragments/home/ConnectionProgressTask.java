package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

public abstract class ConnectionProgressTask extends AsyncTask<String,Integer,Boolean> {

    org.mindroid.android.app.dialog.ProgressDialog dialog;
    ConnectionProgressFragment dFragment;

    public ConnectionProgressTask(String title,Bundle args,FragmentManager fManager){

        dFragment = ConnectionProgressFragment.newInstance(args);

        dialog = ConnectionProgressDialog.newInstance(title,dFragment);
        dialog.show(fManager,"ProgressDialog");
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(final Boolean success){
        dialog.dismiss();
    }

    @Override
    protected abstract Boolean doInBackground(String... params);

    /**
     *
     * @param key - {@link ConnectionProgressFragment} keys
     * @param success - true if success
     */
    public void setProgressState(String key, boolean success){
        dFragment.setProgressState(key,success);
    }
}
