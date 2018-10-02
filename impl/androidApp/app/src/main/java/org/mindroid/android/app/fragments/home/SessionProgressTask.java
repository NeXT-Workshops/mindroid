package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

public abstract class SessionProgressTask extends AsyncTask<String,Integer,Boolean> {

    SessionProgressFragment dFragment;
    FragmentManager fManager;
    String title;
    Bundle configBundle;

    public SessionProgressTask(String title, Bundle configBundle, FragmentManager fManager) {
        this.fManager = fManager;
        this.title = title;
        this.configBundle = configBundle;
    }

    @Override
    protected void onPreExecute() {

        this.dFragment = SessionStateObserver.getInstance().createSessionProgressDialog();
        dFragment.setCancelable(false);
        dFragment.show(fManager, "SessionProgressDialog");
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        dFragment.dismiss();
    }

    @Override
    protected abstract Boolean doInBackground(String... params);
}
