package org.mindroid.api;

public interface IExecutor{

    void setImplementation(ImperativeAPI run);

    void setImplementation(StatemachineAPI run);

    void start();

    void stop();

}
