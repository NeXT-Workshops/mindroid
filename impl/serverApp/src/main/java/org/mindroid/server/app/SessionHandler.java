package org.mindroid.server.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private MindroidMessage startSessionMessage= new MindroidMessage(RobotId.SESSION_HANDLER, RobotId.BROADCAST, MessageType.SESSION, "START SESSION", MindroidMessage.START_SESSION);

    public void handleSessionMessage(MindroidMessage msg) throws IOException {

        l.info("State before: "+ currentState);
        int sessionCommand = msg.getSessionRobotCount();
        RobotId robot = msg.getSource();

        if(sessionCommand == MindroidMessage.BAD_SESSION_SIZE){
            l.error("Received Bad Session-Message from " + robot);
        }else {
            switch (currentState) {
                case IDLE:
                    switch (sessionCommand) {
                        case MindroidMessage.QUIT_SESSION:
                            // Leave Session: no Session to leave, do nothing
                            l.warn(robot + " tried to leave session, while SessionHandler is IDLE");
                            break;
                        case MindroidMessage.UNCOUPLED_SESSION:
                            // add Robot and start uncoupled Session
                            currentState = SessionState.RUNNING_UNCOUPLED;
                            sessionRobots.add(robot);
                            msw.sendMessage(startSessionMessage, robot);
                            l.info(robot + " started uncoupled Session");
                            break;
                        case MindroidMessage.START_SESSION:
                            // should not occur, do nothing
                            l.warn(robot + " tried to start session");
                        default: // s>0
                            // set Session Size, add robot and start pending
                            currentState = SessionState.PENDING;
                            sessionRobots.add(robot);
                            maxSessionSize = sessionCommand;
                            break;
                    }
                    break;

                case PENDING:
                    switch (sessionCommand) {
                        case MindroidMessage.QUIT_SESSION:
                            // remove robot from pending session
                            sessionRobots.remove(robot);
                            l.info(robot + " leaves Session");
                            // if last Robot left pending, go back to IDLE, else stay in PENDING
                            if(sessionRobots.isEmpty()){
                                currentState = SessionState.IDLE;
                                l.info("Session now empty, going to IDLE");
                            }else {
                                currentState = SessionState.PENDING;
                                sendSessionUpdate(sessionRobots.size());
                                l.info("Session not empty yet, keep PENDING");
                            }
                            break;
                        case MindroidMessage.UNCOUPLED_SESSION:
                            // cant start uncoupled while pending
                            msf.addLocalContentLine("WARN", "Can't start uncoupled session when coupled session is pending");
                            break;
                        case MindroidMessage.START_SESSION:
                            // should not occur, do nothing
                            l.warn(robot + " tried to start session");
                        default:
                            // wants to join
                            if (sessionCommand == maxSessionSize) {
                                // sessionSize correct, join Session
                                sessionRobots.add(robot);
                                sendSessionUpdate(sessionRobots.size());
                                l.info(robot + " joins Session");
                                // if all joined, run session, else keep Pending
                                if (sessionRobots.size() == maxSessionSize) {
                                    l.info("Session is full, start RUNNING_COUPLED");
                                    currentState = SessionState.RUNNING_COUPLED;
                                    msw.multicast(sessionRobots.toArray(new RobotId[sessionRobots.size()]), startSessionMessage);
                                } else {
                                    l.info("Session not full yet, keep PENDING");
                                    currentState = SessionState.PENDING;
                                }
                            } else {
                                l.warn(robot + " tried to join Session but sent wrong size");
                                msf.addLocalContentLine("WARN", robot.getValue() + "tried to join Session, but has wrong size");
                            }
                            break;
                    }
                    break;
                case RUNNING_COUPLED:
                    if (sessionCommand == MindroidMessage.QUIT_SESSION) {
                        sendSessionUpdate(MindroidMessage.STOP_SESSION);
                        sessionRobots.clear();
                        currentState = SessionState.IDLE;
                        l.info(robot + " leaves Session, going to IDLE");
                        msf.addLocalContentLine("INFO", "Robot quit session, session ended");
                    } else {
                        l.warn(robot + " tried to join, but RUNNING_COUPLED");
                        msf.addLocalContentLine("WARN", "Coupled Session already running, cannot join");
                    }
                    break;
                case RUNNING_UNCOUPLED:
                    switch (sessionCommand) {
                        case MindroidMessage.QUIT_SESSION:
                            l.info(robot + " leaving RUNNING_UNCOUPLED");
                            sessionRobots.remove(robot);
                            if (sessionRobots.isEmpty()){
                                l.info("RUNNING_UNCOUPLED is empty, going to IDLE");
                                currentState = SessionState.IDLE;
                            }else{
                                l.info("RUNNING_UNCOUPLED not empty yet, keep RUNNING UNCOUPLED");
                                currentState = SessionState.RUNNING_UNCOUPLED;
                            }
                            break;
                        case MindroidMessage.UNCOUPLED_SESSION:
                            l.info(robot + " joins RUNNING_UNCOUPLED");
                            sessionRobots.add(robot);
                            msw.sendMessage(startSessionMessage, robot);
                            break;
                        default:
                            msf.addLocalContentLine("WARN", "Can't start coupled session as uncoupled session is already running");
                            break;
                    }
                default:
                    msf.addLocalContentLine("INFO", "Unknown state, please contact support!");
                    break;
            }
            updateSessionLabel();
            l.info("State after: " + currentState);
        }
    }

    private void sendSessionUpdate(int sessionUpdate){
        MindroidMessage msg = new MindroidMessage(RobotId.SESSION_HANDLER, RobotId.BROADCAST, MessageType.SESSION, "SessionUpdate", sessionUpdate);
        msw.multicast(sessionRobots.toArray(new RobotId[sessionRobots.size()]), msg);
    }

    public boolean isSessionRunning() {
        return currentState == SessionState.RUNNING_COUPLED || currentState == SessionState.RUNNING_UNCOUPLED;
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
