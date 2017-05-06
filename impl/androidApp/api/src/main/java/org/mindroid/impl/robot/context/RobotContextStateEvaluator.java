package org.mindroid.impl.robot.context;

import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.robot.context.IConstraintEvaluator;
import org.mindroid.api.statemachine.ISatisfiedConstraintHandler;
import org.mindroid.api.statemachine.constraints.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by torben on 11.03.2017.
 */
public class RobotContextStateEvaluator implements IConstraintEvaluator{


    Map<String,List<IConstraint>> subscribedConstraints;

    Map<String,ISatisfiedConstraintHandler> listener;

    public RobotContextStateEvaluator(){
        subscribedConstraints = new HashMap<String,List<IConstraint>>();
        listener =  new HashMap<String,ISatisfiedConstraintHandler>();
    }


    private synchronized boolean evaluateConstraint(IConstraint constraint,IRobotContextState rcs){
        if(constraint instanceof IComparator){
            //Evaluate Comparator-Constraint
            return ((IComparator)constraint).evaluate(rcs);

        }else if(constraint instanceof ILogicOperator){
            boolean eval_left = evaluateConstraint(((ILogicOperator)constraint).getLeftConstraint(),rcs);
            boolean eval_right = evaluateConstraint(((ILogicOperator)constraint).getRightConstraint(),rcs);

            return ((ILogicOperator)constraint).evaluate(eval_left,eval_right);
        }else{
            throw new IllegalArgumentException("Unknown Constrainttype.");
        }
    }


    @Override
    public synchronized void handleRobotContextState(IRobotContextState rcs) {
        for(String statemachine_id : subscribedConstraints.keySet()){
            for (int i = 0; i < subscribedConstraints.get(statemachine_id).size(); i++) {
                System.out.println("ContextStateEvaluator.handleRobotContextState()" +subscribedConstraints.get(statemachine_id));
                boolean isSatisfied = false;
                isSatisfied = evaluateConstraint(subscribedConstraints.get(statemachine_id).get(i),rcs);

                if(isSatisfied){
                    listener.get(statemachine_id).handleSatisfiedConstraint(statemachine_id,subscribedConstraints.get(statemachine_id).get(i));
                    break;
                }
            }
        }
    }

    @Override
    public synchronized void subscribeConstraints(ISatisfiedConstraintHandler constraintHandler,String statemachine_id,List<IConstraint> constraints) {
        if(!subscribedConstraints.containsKey(statemachine_id)){
            if(!listener.containsKey(statemachine_id)) {
                listener.put(statemachine_id, constraintHandler);
            }
            subscribedConstraints.put(statemachine_id,constraints);
        }else{
            //remove old subscribed constriants (constraints from an old state!)
            subscribedConstraints.remove(statemachine_id);
            //recall method
            subscribeConstraints(constraintHandler,statemachine_id,constraints);
        }
    }

    private static RobotContextStateEvaluator ourInstance = new RobotContextStateEvaluator();

    public static RobotContextStateEvaluator getInstance() {
        return ourInstance;
    }

}
