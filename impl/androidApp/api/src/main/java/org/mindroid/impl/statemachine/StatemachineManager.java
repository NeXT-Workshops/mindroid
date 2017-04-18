package org.mindroid.impl.statemachine;

import org.mindroid.api.*;
import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.robot.context.IRobotContextStateEvaluator;
import org.mindroid.api.statemachine.*;
import org.mindroid.api.statemachine.constraints.AbstractLogicOperator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.ITimeProperty;
import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.robot.context.RobotContextStateListener;
import org.mindroid.impl.robot.context.RobotContextStateManager;
import org.mindroid.impl.robot.context.StartCondition;
import org.mindroid.impl.statemachine.constraints.TimeExpired;
import org.mindroid.impl.statemachine.properties.Milliseconds;
import org.mindroid.impl.statemachine.properties.Seconds;

import java.util.*;

/**
 * Created by torben on 19.03.2017.
 */
public class StatemachineManager implements ISatisfiedConstraintHandler {

    HashMap<String,IState> currentStates;
    HashMap<String,IStatemachine> statemachines;

    ArrayList<IRobotContextStateEvaluator> evaluators;
    private ArrayList<Task_TimeEvent> scheduledTimeEvents;

    private Timer timeEventScheduler;

    private StatemachineManager() {
        currentStates = new HashMap<String,IState>();
        statemachines = new HashMap<String,IStatemachine>();
        evaluators= new ArrayList<IRobotContextStateEvaluator>(1);
        scheduledTimeEvents = new ArrayList<Task_TimeEvent>();
        timeEventScheduler = new Timer();

    }


    private static StatemachineManager ourInstance = new StatemachineManager();

    public static StatemachineManager getInstance() {
        return ourInstance;
    }



    /**
     * Gets called when the RobotStateEvaluator found a satisfied Constraint. If the current state of the Statemachine(ID) has this constraint in one of its transition it
     * is able to fire.
     *
     * @param ID - StatemachineID
     * @param satConstraint - Constraint which is satisfied
     */
    @Override
    public void handleSatisfiedConstraint(String ID,IConstraint satConstraint){
        /*if(!isActive){ //Return if statemachine is deactived!
            return;
        }*/

        if(currentStates.get(ID) != null && currentStates.get(ID).isActive()) { //Otherwise statemachine is deactivated by stop();
            ITransition transition = null;

            transition = currentStates.get(ID).getSatisfiedTransition(satConstraint);

            if (transition != null) {
                currentStates.get(ID).deactivate();
                currentStates.put(ID,transition.fire());
                //Clear RobotContextStateListener -> Remove old TimeEvents and Messages from context
                RobotContextStateManager.getInstance().cleanContextState();

                handleTimeEventScheduling(ID);

                //Set StartCondition
                StartCondition.getInstance().setStateActiveTime(System.currentTimeMillis());
                //TODO add Gyrosensors position too

                if(Robot.getInstance().messageingEnabled){
                    Robot.getInstance().messenger.sendMessage(IMessenger.SERVER_LOG,"Changed State to --> "+currentStates.get(ID).getName());
                }

                //Activate state
                currentStates.get(ID).activate();
            }
        }
        System.out.println("StateMachine.handleSatisfiedConstraint() => "+satConstraint);


        //Get Constraints of the current State, and subscribe them to the Evaluator
        subscribeConstraints(ID);

    }

    /**
     * Removes old(unused) scheduled Time events, and initiate the new ones of the current state
     * @param ID
     */
    private void handleTimeEventScheduling(String ID) {
        //Remove old/unused scheduled TimeEvents
        for (Task_TimeEvent scheduledTimeEvent : scheduledTimeEvents) {
            scheduledTimeEvent.cancel();
        }
        scheduledTimeEvents.clear();


        //Schedule TimeEvents
        for (IConstraint iConstraint : currentStates.get(ID).getConstraints()) {
            scheduleTimeEvents(iConstraint,currentStates.get(ID));
        }
    }

    private void subscribeConstraints(String ID) {
        for (int i = 0; i < evaluators.size(); i++) {
            evaluators.get(i).subscribeConstraints(this,ID, currentStates.get(ID).getConstraints());
        }
    }

    /**
     * Checks for TimeProperties in the constraint and schedules proper TimeEvents.
     * @param constraint
     * @param source
     */
    private void scheduleTimeEvents(IConstraint constraint,IState source) {
        System.out.println("StatemachineManager.scheduleTimeEvents(): scheduleTimeEvents called: "+constraint+" from state: "+source);
        if(constraint instanceof AbstractLogicOperator){
            scheduleTimeEvents(((AbstractLogicOperator) constraint).getLeftConstraint(),source);
            scheduleTimeEvents(((AbstractLogicOperator) constraint).getRightConstraint(),source);
        }else{
            if(constraint instanceof TimeExpired){
                if( ((TimeExpired) constraint).getProperty() instanceof ITimeProperty){
                    ITimeProperty property = (ITimeProperty) ((TimeExpired) constraint).getProperty();

                    Task_TimeEvent task;
                    long time = property.getTime();
                    ITimeEvent timeevent = new TimeEvent(time, source);

                    task = new Task_TimeEvent(RobotContextStateListener.getInstance(), timeevent);
                    scheduledTimeEvents.add(task);
                    timeEventScheduler.schedule(task, time);

                    System.out.println("StatemachineManager.scheduleTimeEvents(): ScheduledTimeEvent" + timeevent);
                }
            }
        }
    }

    @Override
    public void addConstraintEvalauator(IRobotContextStateEvaluator evaluator) {
        this.evaluators.add(evaluator);
    }


    public void addStatemachine(IStatemachine sm){
        statemachines.put(sm.getID(),sm);
        currentStates.put(sm.getID(),sm.getStartState());
    }

    public boolean startStatemachine(String id){
        if(statemachines.containsKey(id)){
            currentStates.put(id,statemachines.get(id).getStartState());
            RobotContextStateManager.getInstance().cleanContextState();
            subscribeConstraints(id);
            handleTimeEventScheduling(id);
            StartCondition.getInstance().setStateActiveTime(System.currentTimeMillis());
            if(Robot.getInstance().messageingEnabled){
                Robot.getInstance().messenger.sendMessage(IMessenger.SERVER_LOG,"Start Statemachine: "+id);
            }

            statemachines.get(id).start();

            return true;
        }
        return false;
    }

    public boolean stopStatemachine(String id){
        if(statemachines.containsKey(id)){
            //Stop all motors
            Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_A);
            Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_B);
            Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_C);
            Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_D);
            if(Robot.getInstance().messageingEnabled){
                Robot.getInstance().messenger.sendMessage(IMessenger.SERVER_LOG,"Stop Statemachine: "+id);
            }
            statemachines.get(id).stop();
            return true;
        }
        return false;
    }

    public boolean resetStatemachine(String id){
        if(statemachines.containsKey(id)){
            statemachines.get(id).reset();
            return true;
        }
        return false;
    }

    public IState getCurrentState(String id){
        return currentStates.get(id);
    }

    public Set<String> getStatemachineIDs(){
        return statemachines.keySet();
    }

    // ------------------------ Class: Task_schedule_TimeEvents

    private class Task_TimeEvent extends TimerTask {

        ITimeEventListener listener;
        ITimeEvent timeEvent;

        public Task_TimeEvent(ITimeEventListener listener,ITimeEvent timeEvent){
            this.listener = listener;
            this.timeEvent = timeEvent;
        }

        @Override
        public void run() {
            listener.handleTimeEvent(timeEvent);
        }
    }
}
