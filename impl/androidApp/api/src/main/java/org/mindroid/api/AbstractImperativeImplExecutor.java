package org.mindroid.api;

public abstract class AbstractImperativeImplExecutor implements IExecutor {

    private ImperativeAPI runnable;

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
}
