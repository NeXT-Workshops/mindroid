package org.mindroid.android.app.fragments.home;

import android.os.Bundle;
import org.mindroid.android.app.activities.MainActivity;
import org.mindroid.api.AbstractImperativeImplExecutor;
import org.mindroid.impl.robot.Robot;

public class SessionStateObserver implements AbstractImperativeImplExecutor.SessionStateObserver {
    private static SessionStateObserver ourInstance = new SessionStateObserver();

    public static SessionStateObserver getInstance() {
        return ourInstance;
    }

    SessionProgressFragment spf;
    private boolean ready = false;
    private boolean stopped = false;

    private SessionStateObserver() {
    }

    public boolean isDialogDisplayed() {
        return spf.isDialogDisplayed();
    }

    @Override
    public void updateState(String state, int currentRobotCount, int maxSessionRobots) {
        spf.setProgressState(state,currentRobotCount,maxSessionRobots);
        if(state.equals(READY)){
            ready = true;
        }

        if(state.equals(STOP)){
            stopped = true;
        }
    }

    @Override
    public void stopExecution() {
        ready = false;
        stopped = true;
        MainActivity.robot.stopRunningImplementation();
    }

    public SessionProgressFragment createSessionProgressDialog(SessionProgressTask parent){
        ready = false;
        stopped = false;
        return spf = SessionProgressFragment.newInstance("Session State",new Bundle(),parent);
    }

    public boolean isSessionComplete() {
        return ready || stopped;
    }
}
