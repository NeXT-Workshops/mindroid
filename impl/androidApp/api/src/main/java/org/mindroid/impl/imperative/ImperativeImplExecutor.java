package org.mindroid.impl.imperative;


import javafx.beans.InvalidationListener;
import org.mindroid.api.AbstractImperativeImplExecutor;
import org.mindroid.api.IExecutor;
import org.mindroid.api.ImperativeAPI;
import org.mindroid.api.IImplStateListener;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.impl.communication.MessengerClient;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.util.Throwables;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImperativeImplExecutor extends AbstractImperativeImplExecutor implements IExecutor {


    /** true if a statemachine is running else false **/
    private boolean isRunning = false;

    private final HashSet<IImplStateListener> IImplStateListeners = new HashSet<>(1);

    private static final Logger LOGGER = Logger.getLogger(ImperativeImplExecutor.class.getName());

    private MessengerClient messenger;


    static{
        APILoggerManager.getInstance().registerLogger(LOGGER);
    }

    /**
     * Start the execution of the Implementation
     * Executes run method in a thread.
     * Stops all motors, when execution finished/got interrupted.
     */
    @Override
    public final void start() {

        messenger = Robot.getRobotController().getMessenger();

        final ImperativeAPI runningImpl = getRunnable();
        Runnable runImpl = new Runnable() {
            @Override
            public void run() {
                setExecutionFinished(false);
                setInterrupted(false);
                try {
                    setIsRunning(true);

                    int sessionRobotCount = runningImpl.getSessionRobotCount();
                    // handle Session stuff before running program itself
                    messenger.sendSessionMessage(sessionRobotCount);
                    // if we need to wait for other robots to join

                    updateObserver("Init Session",0,runningImpl.getSessionRobotCount());

                    if (sessionRobotCount > 0) {
                        boolean startSession = false;
                        Robot.getRobotController().getBrickController().setEV3StatusLight(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.BLINKING);
                        updateObserver("Pending",1,runningImpl.getSessionRobotCount());
                        while (!startSession) {
                            Thread.sleep(10);
                            // wait for start-message from Server
                            if (messenger.hasMessage()) {
                                MindroidMessage msg = messenger.getNextMessage();
                                int sessionCommand = msg.getSessionRobotCount();
                                LOGGER.log(Level.INFO, msg.toString());
                                if(msg.getSource().equals(RobotId.SESSION_HANDLER) && msg.getMessageType().equals(MessageType.SESSION) ){
                                    if (sessionCommand == MindroidMessage.START_SESSION) {
                                        startSession = true;
                                    }else{
                                        updateObserver("Pending", sessionCommand, runningImpl.getSessionRobotCount());
                                    }
                                }
                            }
                        }
                    }
                    updateObserver("READY",1, runningImpl.getSessionRobotCount());
                    Robot.getRobotController().getBrickController().setEV3StatusLight(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);
                    Robot.getRobotController().getBrickController().buzz();
                    
                    runningImpl.run();

                } catch (Exception e) {
                    //Throw error
                    String errMsg = "An Error occured while trying to execute the Imperative Implementation with the ID \"" + runningImpl.getImplementationID() + "\". \n The given errormessage is: " + e.getMessage();
                    Exception execException = new IIExecutionException(errMsg);
                    ErrorHandlerManager.getInstance().handleError(execException, ImperativeAPI.class, execException.getMessage());
                    LOGGER.log(Level.WARNING, "Source: ImperativeImplExecutor; ImperativeAPI.class.\n\r"+Throwables.getStackTrace(execException)+"\r\n"+ Throwables.getStackTrace(e));
                } finally {
                    LOGGER.log(Level.INFO, "Execution finished");
                    //Detects, that the implementation is finished
                    setExecutionFinished(true);
                    setIsRunning(false);

                    //Clear Messages from message client
                    Robot.getRobotController().getMessenger().clearMessageCache();

                    //When run is finished (imperative impl got stopped (interrupted) or just code is done) - stopAllMotors former state
                    stopAllMotors(runningImpl);
                    quitSession();

                }
            }
        };
        LOGGER.log(Level.INFO,"Starting an Implementation: ID="+runningImpl.getImplementationID());
        new Thread(runImpl).start();
    }

    private void quitSession() {
        messenger.sendSessionMessage(MindroidMessage.QUIT_SESSION);
    }

    private void setIsRunning(boolean isRunning){
        this.isRunning = isRunning;
        for (IImplStateListener IImplStateListener : IImplStateListeners) {
            IImplStateListener.handleIsRunning(isRunning);
        }
    }

    @Override
    public void stop() {
        LOGGER.log(Level.INFO,"Stopping the currently running implementation");
        quitSession();
        //Only set interrupted field, when exection has not finished
        if (!isExecutionFinished()) {
            setInterrupted(true);
            setExecutionFinished(true);
            setIsRunning(false);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Will be true, when the execution is finished, after starting it.
     * Will be set to false, when an execution got stopped or started.
     * internal var to set isInterrupted correctly.
     */
    private boolean isExecutionFinished = false;


    /**
     * Returns true, if execution has finished otherwise false
     *
     * @return true, if execution has finished.
     */
    public final boolean isExecutionFinished() {
        return isExecutionFinished;
    }

    private void setExecutionFinished(boolean executionFinished) {
        isExecutionFinished = executionFinished;
    }

    public void setImplementation(ImperativeAPI runnable) {
        setRunnable(runnable);
        LOGGER.log(Level.INFO,"Implementation Set: ID="+runnable.getImplementationID());
        LOGGER.log(Level.INFO,"Implementation class="+runnable.getClass());
    }

    // ------ Methods to add some code sugar ------

    final class IIExecutionException extends Exception {
        IIExecutionException(String msg) {
            super(msg);
        }
    }

    @Override
    public void registerImplStateListener(IImplStateListener IImplStateListener){
        if(!IImplStateListeners.contains(IImplStateListener)){
            IImplStateListeners.add(IImplStateListener);
        }
    }
}
