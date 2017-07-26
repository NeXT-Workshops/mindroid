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
import java.util.concurrent.*;

/**
 * Created by torben on 19.03.2017.
 */
public class StatemachineManager implements ISatisfiedConstraintHandler {

    Map<String,IState> currentStates;

    Map<String,IStatemachine> runningStatemachines;


    StatemachineCollection statemachineCollection;

    ArrayList<IConstraintEvaluator> evaluators;
    private ArrayList<Task_TimeEvent> scheduledTimeEvents;

    private Timer timeEventScheduler;

    private ConcurrentHashMap<String,Thread> runningStatemachineThreads;

    private StatemachineManager() {
        statemachineCollection = new StatemachineCollection();
        currentStates = new ConcurrentHashMap<String,IState>();
        evaluators= new ArrayList<IConstraintEvaluator>(1);
        scheduledTimeEvents = new ArrayList<Task_TimeEvent>();
        timeEventScheduler = new Timer();
        runningStatemachines = new ConcurrentHashMap<String,IStatemachine>();
        runningStatemachineThreads = new ConcurrentHashMap<String,Thread>();
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
        //Testausgabe
        if(runningStatemachines.containsKey(ID) && currentStates.containsKey(ID)) {
            System.out.println("# handleSatisfiedConstraint(id,satConstraint) --> " + ID + " " + runningStatemachines.get(ID).isActive() + " " + currentStates.get(ID).isActive());
        }

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
            //Get Constraints of the current State, and subscribe them to the Evaluator
            subscribeConstraints(ID);
        }
        System.out.println("StateMachine.handleSatisfiedConstraint() => "+satConstraint);




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
     *
     * @param constraint
     * @param source
     */
    private void scheduleTimeEvents(IConstraint constraint,IState source) {
        //System.out.println("StatemachineManager.scheduleTimeEvents(): scheduleTimeEvents called: "+constraint+" from state: "+source);
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
            ArrayList<IStatemachine> statemachines = sc.getStatemachineList(id);
            if(statemachines.size() > 0){
                if(statemachines.size() == 1){
                    this.statemachineCollection.addStatemachine(id,statemachines.get(0));
                }else{
                    this.statemachineCollection.addParallelStatemachines(id,statemachines);
                }
            }
        }

    }

    /**
     * Starts a single Statemachine with the given id, or a group of parallel running statemchines with the given groupID (id).
     *
     * @param groupID of one Statemachine - or a GroupID of a group of parallel running Statemachines
     */
    public void startStatemachines(String groupID){
        //System.out.println ("## 'startStatemachines(id):' called with ID: "+id);
        ArrayList<IStatemachine> statemachines = statemachineCollection.getStatemachineList(groupID);
        if(statemachines == null || statemachines.size() == 0){
            return;
        }

        if(statemachines.size() == 1){
            //start singleStatemachines
            startStatemachine(statemachines.get(0));
            //System.out.println ("## startStatemachines(id):' only one machine found "+id);
        }else {
            //System.out.println ("## startStatemachines(id):' set of statemachines found - parallel start initiated "+id);
            //Start parallel statemachines
            for (IStatemachine statemachine : statemachines) {
                //set new statemachine id: "<groupID>_<statemachineID>"
                startStatemachine(statemachine);
            }
        }
    }

    /**
     * Starts the given statemachine in a thread
     *
     * @param sm - The Statemachine which should be executed
     */
    private synchronized void startStatemachine(final IStatemachine sm){
        System.out.println ("## startStatemachines(IStatemachine sm) called with --> sm-id: "+sm.getID());
        System.out.println("isAtive:"+sm.isActive());
        System.out.println("SM.ToString"+ sm.toString());
        if(!sm.isActive()) { //If sm is not active already --> start statemachine
            System.out.println ("## The Statemachine is not active already and will be started --> sm-id: "+sm.getID());
            final Runnable runSM = new Runnable() {
                @Override
                public void run() {
                    // System.out.println("## Starting statemachine in Thread --> "+sm.getID()+" ##");
                    if (Robot.getInstance().isMessageingEnabled()) {
                        Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG, "Start Statemachine: " + sm.getID());
                        Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG, "Current State: " + sm.getStartState().getName());
                    }
                    System.out.print("[Statemachine-Thread][StatemachineID: "+sm.getID()+"] Step 1");
                    currentStates.put(sm.getID(), sm.getStartState());
                    RobotContextStateManager.getInstance().cleanContextState();
                    subscribeConstraints(sm.getID());
                    handleTimeEventScheduling(sm.getID());
                    System.out.print("[Statemachine-Thread][StatemachineID: "+sm.getID()+"] Step 2");
                    //Set Start Conditions
                    StartCondition.getInstance().setStateActiveTime(System.currentTimeMillis());
                    RobotContextStateManager.getInstance().setGyroSensorStartCondition();

                    runningStatemachines.put(sm.getID(), sm);
                    System.out.print("[Statemachine-Thread][StatemachineID: "+sm.getID()+"] Step 3");
                    try {
                        sm.start();
                        System.out.print("[Statemachine-Thread][StatemachineID: "+sm.getID()+"] Statemachine started");
                    } catch (NoStartStateException e) {
                        e.printStackTrace();//TODO call error handler
                    }
                    // System.out.println("## Statemachine "+sm.getID()+" is now running in Thread ##");
                }
            };
            Thread t = new Thread(runSM);
            runningStatemachineThreads.put(sm.getID(),t);
            t.start();
            while(!sm.isActive()){
                try {
                    Thread.sleep(50);
                    System.out.println("Wait until statemachine got started.. [StatemachineID: "+sm.getID()+"]");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        //System.out.println("## 'Start Statemachine'-Thread is running! ##");
    }

    /**
     *
     * Stops a single Statemachine with the given groupID, or a group of parallel running statemchines with the given groupID (groupID).
     *
     * @param groupID of one Statemachine - or a GroupID of a group of parallel running Statemachines
     */
    public synchronized void stopStatemachines(String groupID){
        //Stop all motors
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_A);
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_B);
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_C);
        Robot.getRobotController().getMotorController().stop(EV3PortIDs.PORT_D);

        ArrayList<IStatemachine> statemachines = statemachineCollection.getStatemachineList(groupID);

        if(statemachines == null || statemachines.size() == 0){
            return;
        }

        System.out.println("### Number of Statemachines to Stop: "+statemachines.size());


        for (IStatemachine statemachine : statemachines) {
            stopStatemachine(statemachine);
        }
    }

    /**
     * Stops a single statemachine
     * @param sm
     */
    private void stopStatemachine(IStatemachine sm){
        System.out.println("### Trying to stop Statemachine: "+sm.getID());

        //Hard-Kill Imperative Statemachine Thread
        if(sm.getID().equals(LVL2API.IMPERATIVE_STATEMACHINE_ID)){
            System.out.println("### Imperative Statemachine, trying to kill Thread with stop()");
            //runningStatemachineThreads.get(sm.getID()).stop();

            System.out.println("### Imperative Statemachin killed by thread.stop()");
        }
            //Stop statemachine
        sm.stop();



        if(Robot.getInstance().isMessageingEnabled()){
            Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"Stop Statemachine: "+sm.getID());
        }
        unsubscribeFromEvaluators(sm.getID());
        runningStatemachines.remove(sm.getID());
        currentStates.remove(sm.getID());
        runningStatemachineThreads.remove(sm.getID());

        System.out.println("### Stopped Statemachine: "+sm.getID());
        System.out.println("### StatemachineStatus: "+sm.toString());

    }

    /**
     * Unsubscribe the Statemachine from the Evaluator.
     *
     * @param ID - Statemachine ID
     */
    private void unsubscribeFromEvaluators(String ID){
        for (IConstraintEvaluator evaluator : evaluators) {
            evaluator.unsubscribeConstraints(ID);
        }

    }


    public void resetStatemachine(String id){
        ArrayList<IStatemachine> statemachines = statemachineCollection.getStatemachineList(id);
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
