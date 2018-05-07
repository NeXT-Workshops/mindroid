package org.mindroid.api;

public interface IImplStateListener {
    /**
     * Gets called when the 'isRunning'-state of the Implementation changed
     * @param isRunning - true if the impl is running else false
     *
     */
    void handleIsRunning(boolean isRunning);
}
