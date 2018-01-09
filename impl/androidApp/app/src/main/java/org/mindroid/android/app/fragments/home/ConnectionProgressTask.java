package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Async Task showing a customized fragmentDialog during progress.
 *
 */
public abstract class ConnectionProgressTask extends AsyncTask<String,Integer,Boolean> {

    ConnectionProgressDialogFragment dFragment;
    FragmentManager fManager;
    String title;
    Bundle configBundle;

    public ConnectionProgressTask(String title,Bundle configBundle,FragmentManager fManager){
        this.fManager = fManager;
        this.title = title;
        this.configBundle = configBundle;
    }

    @Override
    protected void onPreExecute() {
        this.dFragment = ConnectionProgressDialogFragment.newInstance(title,configBundle);
        dFragment.setCancelable(false);
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
        if(dFragment.isVisible()) { //To prevent app from crashing when progress gets aborted
            dFragment.setProgressState(key, success);
        }
    }
}
