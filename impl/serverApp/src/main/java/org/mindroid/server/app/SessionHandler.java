package org.mindroid.server.app;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.MessageType;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;

import java.io.IOException;
import java.util.ArrayList;

public class SessionHandler {
    private MindroidServerWorker mss;
    private int maxSessionSize = 0;
    private ArrayList<RobotId> sessionRobots = new ArrayList<>();

    private boolean uncupledSession = false;
    private boolean isSessionRunning = false;

    int currentState = 0;
    //wait for call to create a session
    private final int WAITING_FOR_SESSION = 1;
    //wait for users joing the session
    private final int  CUPLED_SESSION_WAITING_FOR_PLAYERS = 2;

    private final int  CUPLED_SESSION_RUNNING = 3;

    private final int  UNCUPLED_SESSION_RUNNING = 4;

    private final int QUIT_UNCUPLED_SESSION = 5;

    private final int QUIT_CUPLED_SESSION = 6;

    public SessionHandler(MindroidServerWorker mindroidServerWorker) {
        mss = mindroidServerWorker;
    }

    private MindroidMessage startSessionMessage= new MindroidMessage(new RobotId(Destination.SERVER_LOG.getValue()), MessageType.SESSION, "START SESSION", Destination.BROADCAST, MindroidMessage.START_SESSION);

    public   void handleSessionMessage(MindroidMessage msg) throws IOException {
        //TODO Refactor code, as statemachine:

        switch(currentState){
            case  WAITING_FOR_SESSION: break;
            case  CUPLED_SESSION_WAITING_FOR_PLAYERS: break;
        }

        if(msg.getSessionRobotCount() < 0){
            switch (msg.getSessionRobotCount()){
                case MindroidMessage.QUIT_SESSION:
                    sessionRobots.remove(msg.getSource());
                    break;
                case MindroidMessage.UNCUPLED_SESSION:
                    if(maxSessionSize == 0){
                        mss.broadcastMessage(startSessionMessage);
                        isSessionRunning = true;
                        uncupledSession = true;
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
                if(isUncupledSession()){
                    //A uncupled session is running (0)

                }else{
                    //A Session with a limited number of users is running
                }

            }

        }
    }

    public boolean isSessionRunning() {
        return isSessionRunning;
    }

    public boolean isUncupledSession() {
        return uncupledSession;
    }
}
