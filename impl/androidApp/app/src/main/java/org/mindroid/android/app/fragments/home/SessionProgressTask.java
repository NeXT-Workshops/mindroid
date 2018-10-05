package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

public abstract class SessionProgressTask extends AsyncTask<String,Integer,Boolean> {

    SessionProgressFragment dFragment;
    FragmentManager fManager;
    String title;
    Bundle configBundle;

    boolean isInterrupted = false;

    public SessionProgressTask(String title, Bundle configBundle, FragmentManager fManager) {
        this.fManager = fManager;
        this.title = title;
        this.configBundle = configBundle;
        this.isInterrupted = false;
    }

    @Override
    protected void onPreExecute() {
        this.dFragment = SessionStateObserver.getInstance().createSessionProgressDialog(this);
        dFragment.setCancelable(false);
        dFragment.show(fManager, "SessionProgressDialog");
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        dFragment.dismiss();
    }

    @Override
    protected abstract Boolean doInBackground(String... params);

    protected void interrupt(){
        this.isInterrupted = true;
    }
}
