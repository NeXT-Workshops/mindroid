package org.mindroid.server.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;

import java.io.IOException;
import java.util.ArrayList;

public class SessionHandler {

    private static SessionHandler ourInstance = new SessionHandler();
    private MindroidServerWorker msw;
    private MindroidServerFrame msf;
    private int maxSessionSize = 0;
    private ArrayList<RobotId> sessionRobots = new ArrayList<>();
    private SessionState currentState = SessionState.IDLE;
    private Logger l;


    private enum SessionState{
        // wait for call to create a (un)coupled session
        IDLE,
        // wait for users join the pending session
        PENDING,
        RUNNING_COUPLED,
        RUNNING_UNCOUPLED
    }

    public static SessionHandler getInstance(){
        return ourInstance;
    }

    private SessionHandler() {
        l = LogManager.getLogger(SessionHandler.class);
    }

    public void setMsw(MindroidServerWorker msw){
        this.msw = msw;
        this.msf = msw.getMindroidServerFrame();
        updateSessionLabel();
    }

    private MindroidMessage startSessionMessage= new MindroidMessage(new RobotId(Destination.SERVER_LOG.getValue()), MessageType.SESSION, "START SESSION", Destination.BROADCAST, MindroidMessage.START_SESSION);

    public void handleSessionMessage(MindroidMessage msg) throws IOException {
        l.info("State before: "+ currentState);
        int sessionCommand = msg.getSessionRobotCount();
        RobotId robot = msg.getSource();

        switch(currentState){
            case  IDLE:
                switch(sessionCommand){
                    // Leave Session: no Session to leave
                    case MindroidMessage.QUIT_SESSION:
                        break;
                    // add Robot and start uncoupled Session
                    case MindroidMessage.UNCOUPLED_SESSION:
                        currentState = SessionState.RUNNING_UNCOUPLED;
                        sessionRobots.add(robot);
                        break;
                    // set Session Size, add robot and start pending
                    default: // s>0
                        currentState = SessionState.PENDING;
                        sessionRobots.add(robot);
                        maxSessionSize = sessionCommand;
                        break;
                }
                break;

            case PENDING:
                switch (sessionCommand){
                    // remove robot from pending session
                    case MindroidMessage.QUIT_SESSION:
                        sessionRobots.remove(robot);
                        // if last Robot left pending, go back to IDLE, else stay in PENDING
                        currentState = sessionRobots.isEmpty() ? SessionState.IDLE : SessionState.PENDING;
                        break;
                    // cant start uncoupled while pending
                    case MindroidMessage.UNCOUPLED_SESSION:
                        msf.addLocalContentLine("WARN", "Can't start uncoupled session when coupled session is pending");
                        break;
                    // wants to join
                    default:
                        //joins if session size is correct
                        if(sessionCommand == maxSessionSize) {
                            // sessionSize correct, join Session
                            sessionRobots.add(robot);
                            // if all joined, run session, else keep Pending
                            if (sessionRobots.size() == maxSessionSize) {
                                currentState = SessionState.RUNNING_COUPLED;
                                msw.broadcastMessage(startSessionMessage);
                            }else{
                                currentState = SessionState.PENDING;
                            }

                        } else{
                            msf.addLocalContentLine("WARN", "tried to Join Session, but has wrong size");
                        }
                        break;
                }
                break;
            case RUNNING_COUPLED:
                if (sessionCommand == MindroidMessage.QUIT_SESSION){
                    sessionRobots.clear();
                    currentState = SessionState.IDLE;
                    msf.addLocalContentLine("INFO", "Robot quit session, session ended");
                } else {
                    msf.addLocalContentLine("INFO", "Coupled Session already running, cannot join");
                }
                break;
            case RUNNING_UNCOUPLED:
                switch(sessionCommand){
                    case MindroidMessage.QUIT_SESSION:
                        sessionRobots.remove(robot);
                        currentState = sessionRobots.isEmpty() ? SessionState.IDLE : SessionState.RUNNING_UNCOUPLED;
                        break;
                    case MindroidMessage.UNCOUPLED_SESSION:
                        sessionRobots.add(robot);
                        break;
                    default:
                        msf.addLocalContentLine("INFO", "Can't start coupled session as uncoupled session is already running");
                        break;
                }
            default:
                msf.addLocalContentLine("INFO", "Unknown state, please contact support!");
                break;
        }
        updateSessionLabel();
        l.info("State after: "+ currentState);
    }

    public boolean isSessionRunning() {
        return currentState == SessionState.RUNNING_COUPLED || currentState == SessionState.RUNNING_UNCOUPLED;
    }

    public boolean isUncoupledSession() {
        return currentState == SessionState.RUNNING_UNCOUPLED;
    }
    
    private void updateSessionLabel(){        
        String text = "";        
        switch (currentState){
            case IDLE: text =  "IDLE"; break;
            case PENDING: text =  "PENDING " + sessionRobots.size() + "/" + maxSessionSize; break;
            case RUNNING_COUPLED: text =  "RUNNING COUPLED, " + sessionRobots.size() + "/" + maxSessionSize + " robots connected"; break;
            case RUNNING_UNCOUPLED: text =  "RUNNING UNCOUPLED, " + sessionRobots.size() + " robot connected"; break;
            default: text = "UNKNOWN STATE; PLEASE CALL SUPPORT"; break;
        }
        msf.displaySessionState(text);
    }
}
