package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Async Task showing a customized fragmentDialog during progress.
 *
 */
public abstract class ConnectionProgressTask extends AsyncTask<String,Integer,Boolean> {

    ConnectionProgressDialog dialog;
    ConnectionProgressDialogFragment dFragment;
    FragmentManager fManager;

    public ConnectionProgressTask(String title,Bundle configBundle,FragmentManager fManager){
        this.fManager = fManager;
        this.dFragment = ConnectionProgressDialogFragment.newInstance(title,configBundle);
        //this.dialog = ConnectionProgressDialog.newInstance(title,dFragment);

    }

    @Override
    protected void onPreExecute() {
        System.out.println("[ConnectionProgressTask:onPreExecute] showing dialog");
        dFragment.show(fManager,"ProgressDialog");

    }

    @Override
    protected void onPostExecute(final Boolean success){
        dFragment.dismiss();
    }

    @Override
    protected abstract Boolean doInBackground(String... params);

    /**
     *
     * @param key - {@link ConnectionProgressDialogFragment} keys
     * @param success - true if success
     */
    public void setProgressState(String key, boolean success){
        dFragment.setProgressState(key,success);
    }
}
