package org.mindroid.impl.statemachine;

import org.mindroid.api.*;
import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.robot.context.IConstraintEvaluator;
import org.mindroid.api.statemachine.*;
import org.mindroid.api.statemachine.constraints.AbstractLogicOperator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.ITimeProperty;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.robot.context.RobotContextState;
import org.mindroid.impl.robot.context.RobotContextStateManager;
import org.mindroid.impl.robot.context.StartCondition;
import org.mindroid.impl.statemachine.constraints.TimeExpired;

import java.util.*;

/**
 * Created by torben on 19.03.2017.
 */
public class StatemachineManager implements ISatisfiedConstraintHandler {

    HashMap<String,IState> currentStates;

    HashMap<String,IStatemachine> runningStatemachines;

    StatemachineCollection statemachineCollection;

    ArrayList<IConstraintEvaluator> evaluators;
    private ArrayList<Task_TimeEvent> scheduledTimeEvents;

    private Timer timeEventScheduler;

    private StatemachineManager() {
        statemachineCollection = new StatemachineCollection();
        currentStates = new HashMap<String,IState>();
        evaluators= new ArrayList<IConstraintEvaluator>(1);
        scheduledTimeEvents = new ArrayList<Task_TimeEvent>();
        timeEventScheduler = new Timer();
        runningStatemachines = new HashMap<String,IStatemachine>();

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
    public synchronized void handleSatisfiedConstraint(String ID,IConstraint satConstraint){
        /*if(!isActive){ //Return if statemachine is deactived!
            return;
        }*/
        System.out.println("## handleSatisfiedConstraint(id,satConstraint) --> "+ID+  runningStatemachines.get(ID).isActive() + currentStates.get(ID).isActive());

        if(currentStates.get(ID) != null && currentStates.get(ID).isActive() && runningStatemachines.containsKey(ID) && runningStatemachines.get(ID).isActive()) { //Otherwise statemachine is deactivated by stop();
            ITransition transition = null;
            transition = currentStates.get(ID).getSatisfiedTransition(satConstraint);

            if (transition != null) {
                currentStates.get(ID).deactivate();
                IState nextState = transition.fire();
                currentStates.put(ID,nextState);
                //Clear RobotContextState -> Remove old TimeEvents and Messages from context
                RobotContextStateManager.getInstance().cleanContextState();

                handleTimeEventScheduling(ID);

                //Set StartCondition
                StartCondition.getInstance().setStateActiveTime(System.currentTimeMillis());
                RobotContextStateManager.getInstance().setGyroSensorStartCondition();


                if(Robot.getInstance().isMessageingEnabled()){
                    Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"Changed State to --> "+currentStates.get(ID).getName());
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

    /**
     * Subscribe constraints of the current state of the Statemachine 'parameter ID' to the evaluators
     *
     * @param ID
     */
    private void subscribeConstraints(String ID) {
        for (int i = 0; i < this.evaluators.size(); i++) {
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

                    task = new Task_TimeEvent(RobotContextState.getInstance(), timeevent);
                    scheduledTimeEvents.add(task);
                    timeEventScheduler.schedule(task, time);

                    //System.out.println("StatemachineManager.scheduleTimeEvents(): ScheduledTimeEvent" + timeevent);
                }
            }
        }
    }

    @Override
    public void addConstraintEvaluator(IConstraintEvaluator evaluator) {
        this.evaluators.add(evaluator);
    }


    public void addStatemachines(StatemachineCollection sc){
        for(String id: sc.getStatemachineKeySet()){
            IStatemachine[] statemachines = sc.getStatemachineSet(id);
            if(statemachines.length > 0){
                if(statemachines.length == 1){
                    this.statemachineCollection.addStatemachine(statemachines[0]);
                }else{
                    this.statemachineCollection.addParallelStatemachines(id,statemachines);
                }
            }
        }

    }

    /**
     * Starts a single Statemachine with the given id, or a group of parallel running statemchines with the given groupID (id).
     *
     * @param id of one Statemachine - or a GroupID of a group of parallel running Statemachines
     */
    public void startStatemachine(String id){
        //System.out.println ("## 'startStatemachine(id):' called with ID: "+id);
        IStatemachine[] statemachines = statemachineCollection.getStatemachineSet(id);
        if(statemachines == null || statemachines.length == 0){
            return;
        }

        if(statemachines.length == 1){
            //start singleStatemachines
            startStatemachine(statemachines[0]);
            //System.out.println ("## startStatemachine(id):' only one machine found "+id);
        }else {
            //System.out.println ("## startStatemachine(id):' set of statemachines found - parallel start initiated "+id);
            //Start parallel statemachines
            for (IStatemachine statemachine : statemachines) {
                //set new statemachine id: "<groupID>_<statemachineID>"
                statemachine.setID(id + "_" + statemachine.getID());
                startStatemachine(statemachine);
            }
        }
    }

    private void startStatemachine(final IStatemachine sm){
        //System.out.println ("## 'Start Statemachine'-Thread will be started soon! ##");
        Runnable runSM = new Runnable(){
         @Override
         public void run(){
             // System.out.println("## Starting statemachine in Thread --> "+sm.getID()+" ##");
                currentStates.put(sm.getID(),sm.getStartState());
                RobotContextStateManager.getInstance().cleanContextState();
                subscribeConstraints(sm.getID());
                handleTimeEventScheduling(sm.getID());
                StartCondition.getInstance().setStateActiveTime(System.currentTimeMillis());
                RobotContextStateManager.getInstance().setGyroSensorStartCondition();
                if(Robot.getInstance().isMessageingEnabled()){
                    Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"Start Statemachine: "+sm.getID());
                    Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"Current State: "+currentStates.get(sm.getID()).getName());
                }
                runningStatemachines.put(sm.getID(),sm);

                sm.start();

             // System.out.println("## Statemachine "+sm.getID()+" is now running in Thread ##");
          }
         };
        Thread t = new Thread(runSM);
        t.start();
        //System.out.println("## 'Start Statemachine'-Thread is running! ##");
    }

    /**
     *
     * Stops a single Statemachine with the given id, or a group of parallel running statemchines with the given groupID (id).
     *
     * @param id of one Statemachine - or a GroupID of a group of parallel running Statemachines
     */
    public void stopStatemachines(String id){
        //Stop all motors
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_A);
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_B);
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_C);
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_D);

        IStatemachine[] statemachines = statemachineCollection.getStatemachineSet(id);

        if(statemachines == null || statemachines.length == 0){
            return;
        }
        for (IStatemachine statemachine : statemachines) {
            stopStatemachine(statemachine);
        }
    }

    /**
     * Stops a single statemachine
     * @param sm
     */
    private void stopStatemachine(IStatemachine sm){
        sm.stop();
        if(Robot.getInstance().isMessageingEnabled()){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"Stop Statemachine: "+sm.getID());
        }
        runningStatemachines.remove(sm.getID());
        currentStates.remove(sm.getID());
    }


    public void resetStatemachine(String id){
        IStatemachine[] statemachines = statemachineCollection.getStatemachineSet(id);
        for (IStatemachine statemachine : statemachines) {
            statemachine.reset();
        }
    }

    public IState getCurrentState(String id){
        return currentStates.get(id);
    }

    public Set<String> getStatemachineIDs(){
        return statemachineCollection.getStatemachineKeySet();
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
