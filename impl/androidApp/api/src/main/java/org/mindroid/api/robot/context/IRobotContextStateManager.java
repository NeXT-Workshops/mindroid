package org.mindroid.api.robot.context;

/**
 * Created by torben on 11.03.2017.
 */
public interface IRobotContextStateManager {

    IRobotContextState takeSnapshot();
    public void registerRobotContextStateListener(IConstraintEvaluator evaluator);

    public void cleanContextState();

    void setGyroSensorStartCondition();
}
