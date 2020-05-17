package org.mindroid.android.app.fragments.home;

import android.app.FragmentManager;
import android.os.AsyncTask;
import org.mindroid.android.app.activities.MainActivity;
import org.mindroid.android.app.dialog.ProgressDialog;

public class AbortInitializationTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        MainActivity.robot.abortInitializationProcess();
        return true;
    }

    public static AsyncTask create(final FragmentManager fragManager, String tag){
        return new AbortInitializationTask(){
            ProgressDialog pd;
            @Override
            protected void onPreExecute() {

                pd = ProgressDialog.newInstance("Aborting"," Please wait");
                pd.show(fragManager,"ABORT_CONFIG_PROCESS");
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(Object o) {
                pd.dismiss();
                super.onPostExecute(o);
            }
        };
    }

}
