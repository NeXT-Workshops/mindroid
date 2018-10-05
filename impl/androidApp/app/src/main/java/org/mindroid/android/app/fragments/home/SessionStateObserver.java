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
    private org.mindroid.android.app.robodancer.Robot robot;

    private boolean ready = false;
    private boolean stopped = false;

    private SessionStateObserver() {
        robot = MainActivity.robot;
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
        robot.stopRunningImplementation();
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
