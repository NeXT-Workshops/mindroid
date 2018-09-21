package org.mindroid.server.app;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;

import java.io.IOException;
import java.util.ArrayList;

public class SessionHandler {
    private MindroidServerWorker msw;
    private MindroidServerFrame msf;
    private int maxSessionSize = 0;
    private ArrayList<RobotId> sessionRobots = new ArrayList<>();
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
        msw = mindroidServerWorker;
        msf = msw.getMindroidServerFrame();
    }

    private MindroidMessage startSessionMessage= new MindroidMessage(new RobotId(Destination.SERVER_LOG.getValue()), MessageType.SESSION, "START SESSION", Destination.BROADCAST, MindroidMessage.START_SESSION);

    public   void handleSessionMessage(MindroidMessage msg) throws IOException {
        int sessionCommand = msg.getSessionRobotCount();
        RobotId robot = msg.getSource();

        switch(currentState){
            case  IDLE:
                switch(sessionCommand){
                    // Leave Session: no Session to leave
                    case MindroidMessage.QUIT_SESSION:
                        msf.addLocalContentLine("LOG", "No Session open to leave.");
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
                    msf.addLocalContentLine("INFO", "Coupled Session already running");
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
                        msw.broadcastMessage(startSessionMessage);
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
                    msw.broadcastMessage(startSessionMessage);
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
        return currentState == SessionState.RUNNING_COUPLED || currentState == SessionState.RUNNING_UNCOUPLED;
    }

    public boolean isUncoupledSession() {
        return currentState == SessionState.RUNNING_UNCOUPLED;
    }
}
