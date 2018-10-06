package org.mindroid.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractImperativeImplExecutor extends Observable implements IExecutor {

    private ImperativeAPI runnable;

    List<SessionStateObserver> obsList = new CopyOnWriteArrayList<SessionStateObserver>();

    public void setInterrupted(boolean isInterrupted){
        runnable.isInterrupted = isInterrupted;
    }

    public ImperativeAPI getRunnable() {
        return runnable;
    }

    public void setRunnable(ImperativeAPI runnable) {
        this.runnable = runnable;
    }

    /**
     * Resets the given Runnable.
     * Used by the implementation of this abstract class
     *
     * @param runnable - imperativeAPI to stopAllMotors
     */
    public void stopAllMotors(ImperativeAPI runnable){
        runnable.stopAllMotors();
    }

    public void addSessionStateObserver(SessionStateObserver observer){
        if(!obsList.contains(observer)){
            obsList.add(observer);
        }
    }

    public void removeSessionStateObserver(SessionStateObserver observer){
        obsList.remove(observer);
    }

    public void updateObserver(String state, int currentRobotCount, int maxSessionRobots){
        for (SessionStateObserver sessionStateObserver : obsList) {
            sessionStateObserver.updateState(state,currentRobotCount,maxSessionRobots);
        }
    }

    public interface SessionStateObserver{
        String INIT = "Init Session";
        String PENDING = "Pending";
        String READY = "Ready";
        String STOP = "Stop";

        void updateState(String state, int currentRobotCount, int maxSessionRobots);
        void stopExecution();

        }
}
