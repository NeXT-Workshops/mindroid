package org.mindroid.server.app;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;

import java.io.IOException;
import java.util.ArrayList;

public class SessionHandler {
    private MindroidServerWorker mss;
    private MindroidServerFrame msf;
    private int maxSessionSize = 0;
    private ArrayList<RobotId> sessionRobots = new ArrayList<>();

    private boolean uncoupledSession = false;
    private boolean isSessionRunning = false;

    private SessionState nextState = SessionState.IDLE;
    private SessionState currentState = SessionState.IDLE;


    private enum SessionState{
        // wait for call to create a (un)coupled session
        IDLE,
        // wait for users join the pending session
        PENDING,

        RUNNING_COUPLED,
        RUNNING_UNCOUPLED
    }



    public SessionHandler(MindroidServerWorker mindroidServerWorker) {
        mss = mindroidServerWorker;
        msf = mss.getMindroidServerFrame();
    }

    private MindroidMessage startSessionMessage= new MindroidMessage(new RobotId(Destination.SERVER_LOG.getValue()), MessageType.SESSION, "START SESSION", Destination.BROADCAST, MindroidMessage.START_SESSION);

    public   void handleSessionMessage(MindroidMessage msg) throws IOException {
        //TODO Refactor code, as statemachine:
        int sessionCommand = msg.getSessionRobotCount();
        RobotId robot = msg.getSource();
        currentState = nextState;
        switch(currentState){
            case  IDLE:
                switch(sessionCommand){
                    case MindroidMessage.QUIT_SESSION:
                        msf.addLocalContentLine("LOG", "No Session open to leave."); break;
                    case MindroidMessage.UNCOUPLED_SESSION:
                        nextState = SessionState.RUNNING_UNCOUPLED;
                        sessionRobots.add(robot);
                        break;
                    default: // s>0
                        nextState = SessionState.PENDING;
                        sessionRobots.add(msg.getSource());
                        maxSessionSize = sessionCommand;
                        break;
                }
                break;
            case PENDING:
                switch (sessionCommand){
                    case MindroidMessage.QUIT_SESSION:
                        sessionRobots.remove(robot);
                        // if last Robot left pending, go back to IDLE, else stay in PENDING
                        nextState = sessionRobots.isEmpty() ? SessionState.IDLE : SessionState.PENDING;
                        break;
                    case MindroidMessage.UNCOUPLED_SESSION:
                        msf.addLocalContentLine("WARN", "Can't start uncoupled session when coupled session is pending");
                        break;
                    default:
                        if(sessionCommand == maxSessionSize) {
                            // sessionSize correct, join Session
                            sessionRobots.add(robot);
                            // if all joined, run session, else keep Pending
                            nextState = (sessionRobots.size() == maxSessionSize) ? SessionState.RUNNING_COUPLED : SessionState.PENDING;
                        } else{
                            msf.addLocalContentLine("WARN", "tried to Join Session, but has wrong size");
                        }
                        break;
                }
                break;
            case RUNNING_COUPLED:
                if (sessionCommand == MindroidMessage.QUIT_SESSION){
                    sessionRobots.clear();
                    nextState = SessionState.IDLE;
                    msf.addLocalContentLine("INFO", "Robot quit session, session ended");
                } else {
                    msf.addLocalContentLine("INFO", "Coupled Session already running");
                }
                break;
            case RUNNING_UNCOUPLED:
                switch(sessionCommand){
                    case MindroidMessage.QUIT_SESSION:
                        sessionRobots.remove(robot);
                        nextState = sessionRobots.isEmpty() ? SessionState.IDLE : SessionState.RUNNING_UNCOUPLED;
                        break;
                    case MindroidMessage.UNCOUPLED_SESSION:
                        sessionRobots.add(robot);
                        break;
                    default:
                        msf.addLocalContentLine("INFO", "Can't start coupled session as uncoupled session is already running");
                        break;
                }
            default:
                msf.addLocalContentLine("INFO", "Unknown state!");
                break;
        }



/*



        if(msg.getSessionRobotCount() < 0){
            switch (msg.getSessionRobotCount()){
                case MindroidMessage.QUIT_SESSION:
                    sessionRobots.remove(msg.getSource());
                    break;
                case MindroidMessage.UNCOUPLED_SESSION:
                    if(maxSessionSize == 0){
                        mss.broadcastMessage(startSessionMessage);
                        isSessionRunning = true;
                        uncoupledSession = true;
                    }
            }
        }else {
            if(!isSessionRunning()){
                //Session is not runnig, it is possible to start a session or join one
                if (maxSessionSize == 0) {
                    // first robot tells us sessionSize and gets added to it
                    maxSessionSize = msg.getSessionRobotCount();

                } else if (msg.getSessionRobotCount() > 0 && sessionRobots.size() < maxSessionSize) {
                    // session not full yet, add robot
                    sessionRobots.add(msg.getSource());
                }
                //check if session is full
                if (sessionRobots.size() == maxSessionSize) {
                    mss.broadcastMessage(startSessionMessage);
                    isSessionRunning = true;
                }
            }else{
                //A Session is already running
                if(isUncoupledSession()){
                    //A uncoupled session is running (0)

                }else{
                    //A Session with a limited number of users is running
                }
            }
        }
        */
    }

    public boolean isSessionRunning() {
        return isSessionRunning;
    }

    public boolean isUncoupledSession() {
        return uncoupledSession;
    }
}
