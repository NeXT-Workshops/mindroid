package org.mindroid.impl.statemachine;

import org.mindroid.api.*;
import org.mindroid.api.communication.IMessenger;
import org.mindroid.api.robot.context.IConstraintEvaluator;
import org.mindroid.api.statemachine.*;
import org.mindroid.api.statemachine.constraints.AbstractLogicOperator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.properties.ITimeProperty;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.robot.context.RobotContextState;
import org.mindroid.impl.robot.context.RobotContextStateEvaluator;
import org.mindroid.impl.robot.context.RobotContextStateManager;
import org.mindroid.impl.robot.context.StartCondition;
import org.mindroid.impl.statemachine.constraints.TimeExpired;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by torben on 19.03.2017.
 */
public class StatemachineExecutor implements ISatisfiedConstraintHandler, IExecutor {

    /**
     * Contains Statemachines set should be executed/stopped when the start/stop method will be called
     *
     * This field gets set by the {@link #setImplementation(List)} method. Normally this method is called by the {@link ExecutorProvider}.
     */
    private List<IStatemachine> runnables;

    /**
     * Contains the currently active State during runtime of each running Statemachine
     */
    Map<String,IState> currentStates;

    /**
     * If the statemachines got started they will be put into this map.
     * It will be set to an {@link ConcurrentHashMap} to work properly during the run of multiple parallel statemachines.
     */
    Map<String,IStatemachine> runningStatemachines;

    /**
     * Each statemachine will be executed in a single Thread.
     * The Threads are kept by this map.
     */
    private ConcurrentHashMap<String,Thread> runningStatemachineThreads;

    private final HashSet<IImplStateListener> IImplStateListeners = new HashSet<>(1);

    ArrayList<IConstraintEvaluator> evaluators;

    private ArrayList<Task_TimeEvent> scheduledTimeEvents;

    private Timer timeEventScheduler;

    /** true if a statemachine is running else false **/
    private boolean isRunning = false;

    private static final Logger LOGGER = Logger.getLogger(StatemachineExecutor.class.getName());

    static{
        APILoggerManager.getInstance().registerLogger(LOGGER);
    }

    public StatemachineExecutor() {
        currentStates = new ConcurrentHashMap<String,IState>();
        evaluators= new ArrayList<IConstraintEvaluator>(1);
        scheduledTimeEvents = new ArrayList<Task_TimeEvent>();
        timeEventScheduler = new Timer();
        runningStatemachines = new ConcurrentHashMap<String,IStatemachine>();
        runningStatemachineThreads = new ConcurrentHashMap<String,Thread>();

        setupStatemachineEngine();
    }

    @Override
    public void registerImplStateListener(IImplStateListener IImplStateListener){
        if(!IImplStateListeners.contains(IImplStateListener)){
            IImplStateListeners.add(IImplStateListener);
        }
    }

    public void setImplementation(List<IStatemachine> runnables) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (IStatemachine runnable : runnables) {
            sb.append("smID=").append(runnable.getID()).append(",");
        }
        sb.append("}");
        LOGGER.log(Level.INFO,"Set Statemachine(s): "+sb.toString());
        this.runnables = runnables;
    }

    /**
     * Setups the Statemachine Engine.
     * A RobotContextStateEvaluator will be created and set as a listener to the RobotContextStatemanager.
     *
     */
    private void setupStatemachineEngine() {
        IConstraintEvaluator evaluator = new RobotContextStateEvaluator();
        addConstraintEvaluator(evaluator);
        RobotContextStateManager.getInstance().registerRobotContextStateListener(evaluator);
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


                if(Robot.getInstance().getMessenger().isConnected() && runningStatemachines.get(ID).isMessageingAllowed()){
                    //Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"Changed State to --> "+currentStates.get(ID).getName());
                }

                //Activate state
                currentStates.get(ID).activate();
            }
            //Get Constraints of the current State, and subscribe them to the Evaluator
            subscribeConstraints(ID);
        }
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
                }
            }
        }
    }

    @Override
    public void addConstraintEvaluator(IConstraintEvaluator evaluator) {
        this.evaluators.add(evaluator);
    }


    /**
     * Starts the given statemachine in a thread
     *
     * @param sm - The Statemachine which should be executed
     */
    private synchronized void startStatemachine(final IStatemachine sm){
        if(!sm.isActive()) { //If sm is not active already --> start statemachine
            final Runnable runSM = new Runnable() {
                @Override
                public void run() {
                    if (Robot.getInstance().getMessenger().isConnected()) {
                        //Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG, "Start Statemachine: " + sm.getID());
                        //Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG, "Current State: " + sm.getStartState().getName());
                    }
                    currentStates.put(sm.getID(), sm.getStartState());
                    RobotContextStateManager.getInstance().cleanContextState();
                    subscribeConstraints(sm.getID());
                    handleTimeEventScheduling(sm.getID());
                    //Set Start Conditions
                    StartCondition.getInstance().setStateActiveTime(System.currentTimeMillis());
                    RobotContextStateManager.getInstance().setGyroSensorStartCondition();
                    runningStatemachines.put(sm.getID(), sm);
                    try {
                        sm.start();
                    } catch (NoStartStateException e) {
                        ErrorHandlerManager.getInstance().handleError(e,StatemachineExecutor.class,sm.getID());
                    } catch(Exception e){
                        Exception execException = new SMExecutionException(getStatemachineErrorMsg(sm.getID(),e));
                        ErrorHandlerManager.getInstance().handleError(execException,StatemachineExecutor.class,execException.getMessage());
                }

                }
            };
            Thread t = new Thread(runSM);
            runningStatemachineThreads.put(sm.getID(),t);

            t.start();

            while(!sm.isActive()){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            setIsRunning(true);
       }
    }

    private void setIsRunning(boolean isRunning){
        this.isRunning = isRunning;
        for (IImplStateListener IImplStateListener : IImplStateListeners) {
            IImplStateListener.handleIsRunning(isRunning);
        }
    }

    private String getStatemachineErrorMsg(String smID, Exception e){
        return "An Error occured while executing the Statemachine "+smID+".\n The given Errormessage: "+e.getMessage();
    }

    /**
     * Stops a single statemachine
     * @param sm
     */
    private void stopStatemachine(IStatemachine sm){
        LOGGER.log(Level.INFO,"Stopping statemachine: "+sm.getID());


        //stop statemachine
        sm.stop();
        sm.reset();

        if(Robot.getInstance().getMessenger().isConnected()){
            //Robot.getRobotController().getMessenger().sendMessage(IMessenger.SERVER_LOG,"Stop Statemachine: "+sm.getID());
        }
        unsubscribeFromEvaluators(sm.getID());
        runningStatemachines.remove(sm.getID());
        currentStates.remove(sm.getID());
        runningStatemachineThreads.remove(sm.getID());

        setIsRunning(false);

        //TODO stopAllMotors statemachine calling sm.stopAllMotors() ??
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

    public IState getCurrentState(String id){
        return currentStates.get(id);
    }

    @Deprecated
    public Set<String> getStatemachineIDs(){
        return null;
    }

    /**
     * Starts all Statemachines contained in the {@link #runnables} field
     */
    @Override
    public void start() {
        if (!areRunnablesValid()){ return; }

        //Start all statemachines contained in the runnable list
        for (IStatemachine statemachine : runnables) {
            //set new statemachine id: "<groupID>_<statemachineID>"
            startStatemachine(statemachine);
        }
    }

    /**
     * Stops all statemachines contained in the {@link #runnables} field
     */
    @Override
    public void stop() {
        //Stop all motors
        stopAllMotors();

        if (!areRunnablesValid()){ return; }

        //Stop every statemachine
        for (IStatemachine statemachine : runnables) {
            stopStatemachine(statemachine);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Checks if the given Runnables (Statemachines to execute) are not null or == 0,
     * @return true if size is >= 1 and != null else false
     */
    private boolean areRunnablesValid() {
        if(runnables == null || runnables.size() == 0){
            Exception e = new IllegalStateException("The vaialable List of Statemachines is null or does not contain any Statemachine that can be executed!");
            ErrorHandlerManager.getInstance().handleError(e,StatemachineExecutor.class,e.getMessage());
            return false;
        }
        return true;
    }

    private void stopAllMotors() {
        Robot.getRobotController().getMotorProvider().getMotor(EV3PortIDs.PORT_A).stop();
        Robot.getRobotController().getMotorProvider().getMotor(EV3PortIDs.PORT_B).stop();
        Robot.getRobotController().getMotorProvider().getMotor(EV3PortIDs.PORT_C).stop();
        Robot.getRobotController().getMotorProvider().getMotor(EV3PortIDs.PORT_D).stop();
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

    public class SMExecutionException extends Exception {
        public SMExecutionException(String statemachineErrorMsg) {
            super(statemachineErrorMsg);
        }
    }
}
