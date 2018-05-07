package org.mindroid.api;

public interface IExecutor{


    void start();

    void stop();

    boolean isRunning();

    void registerImplStateListener(IImplStateListener IImplStateListener);
}


