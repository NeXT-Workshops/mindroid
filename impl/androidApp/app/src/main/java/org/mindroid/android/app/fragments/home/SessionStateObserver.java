package org.mindroid.android.app.fragments.home;

import android.os.Bundle;
import org.mindroid.api.AbstractImperativeImplExecutor;

public class SessionStateObserver implements AbstractImperativeImplExecutor.SessionStateObserver {
    private static SessionStateObserver ourInstance = new SessionStateObserver();

    public static SessionStateObserver getInstance() {
        return ourInstance;
    }

    SessionProgressFragment spf;

    boolean isReady = false;
    boolean isStopped = false;

    private SessionStateObserver() {
    }

    @Override
    public void updateState(String state, int currentRobotCount, int maxSessionRobots) {
        spf.setProgressState(state,currentRobotCount,maxSessionRobots);
        if(state.equals(READY)){
            isReady = true;
        }

        if(state.equals(STOP)){
            isStopped = true;
        }
    }

    @Override
    public void stopExecution() {
        isReady = false;
        isStopped = true;
    }

    public SessionProgressFragment createSessionProgressDialog(SessionProgressTask parent){
        isReady = false;
        isStopped = false;
        return spf = SessionProgressFragment.newInstance("Session State",new Bundle(),parent);
    }

    public boolean isSessionComplete() {
        return isReady || isStopped;
    }
}
