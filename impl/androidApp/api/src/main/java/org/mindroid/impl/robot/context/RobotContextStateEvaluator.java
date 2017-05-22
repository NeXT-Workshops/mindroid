package org.mindroid.impl.robot.context;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.robot.context.IConstraintEvaluator;
import org.mindroid.api.statemachine.ISatisfiedConstraintHandler;
import org.mindroid.api.statemachine.constraints.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by torben on 11.03.2017.
 */
public class RobotContextStateEvaluator implements IConstraintEvaluator{


    Map<String,List<IConstraint>> subscribedConstraints;

    Map<String,ISatisfiedConstraintHandler> listener;

    public RobotContextStateEvaluator(){
        subscribedConstraints = new ConcurrentHashMap<String,List<IConstraint>>();
        listener =  new ConcurrentHashMap<String,ISatisfiedConstraintHandler>();
    }


    private synchronized boolean evaluateConstraint(IConstraint constraint,IRobotContextState rcs){
        if(constraint instanceof IComparator){
            //Evaluate Comparator-Constraint
            return ((IComparator)constraint).evaluate(rcs);

        }else if(constraint instanceof ILogicOperator){
            boolean evalLeft = evaluateConstraint(((ILogicOperator)constraint).getLeftConstraint(),rcs);
            boolean evalRight = evaluateConstraint(((ILogicOperator)constraint).getRightConstraint(),rcs);

            return ((ILogicOperator)constraint).evaluate(evalLeft,evalRight);
        }else{
            throw new IllegalArgumentException("Unknown Constrainttype.");
        }
    }


    @Override
    public synchronized void handleRobotContextState(IRobotContextState rcs) {
        for(String statemachineId : subscribedConstraints.keySet()){
            for (int i = 0; i < subscribedConstraints.get(statemachineId).size(); i++) {
                //System.out.println("ContextStateEvaluator.handleRobotContextState()" +subscribedConstraints.get(statemachineId));
                boolean isSatisfied = false;
                isSatisfied = evaluateConstraint(subscribedConstraints.get(statemachineId).get(i),rcs);

                if(isSatisfied){
                    listener.get(statemachineId).handleSatisfiedConstraint(statemachineId,subscribedConstraints.get(statemachineId).get(i));
                    break;
                }
            }
        }
    }

    @Override
    public synchronized void subscribeConstraints(ISatisfiedConstraintHandler constraintHandler,String statemachineId,List<IConstraint> constraints) {
        if(!subscribedConstraints.containsKey(statemachineId)){
            if(!listener.containsKey(statemachineId)) {
                listener.put(statemachineId, constraintHandler);
            }
            subscribedConstraints.put(statemachineId,constraints);
        }else{
            //remove old subscribed constriants (constraints from an old state!)
            subscribedConstraints.remove(statemachineId);
            //recall method
            subscribeConstraints(constraintHandler,statemachineId,constraints);
        }
    }

    @Override
    public synchronized void unsubscribeConstraints(String statemachineId){
        //Remove Listener
        listener.remove(statemachineId);
        subscribedConstraints.remove(statemachineId);
    }

    private static RobotContextStateEvaluator ourInstance = new RobotContextStateEvaluator();

    public synchronized static RobotContextStateEvaluator getInstance() {
        return ourInstance;
    }

}
