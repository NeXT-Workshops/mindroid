package org.mindroid.server.app.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.server.app.ConnectedDevicesFrame;
import org.mindroid.server.app.MindroidServerWorker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class UserManagement {

    private Logger logger;

    private Map<Destination, InetSocketAddress> ipMapping = new HashMap<>();
    private Map<Destination, Socket> socketMapping = new HashMap<>();
    private Map<Destination, MindroidServerWorker> workerMapping = new HashMap<>();

    private static UserManagement ourInstance = new UserManagement();

    public static UserManagement getInstance(){
        return ourInstance;
    }

    private UserManagement(){
        logger = LogManager.getLogger(UserManagement.class);
    }

    public boolean registerRobot(RobotId robotId, MindroidServerWorker msWorker, Socket socket, int port) throws IOException {
        logger.log(Level.INFO,"Registering robot: "+robotId.getValue());

        InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        Destination destKey = new Destination(robotId.getValue());
        if(getIPMapping().containsKey(destKey) || getSocketMapping().containsKey(destKey)){
            //Reject registration
            return false;
        }else {
            getIPMapping().put(destKey, new InetSocketAddress(((InetSocketAddress) remoteSocketAddress).getAddress(), port));
            getSocketMapping().put(destKey, socket);
            getWorkerMapping().put(destKey, msWorker);

            ConnectedDevicesFrame.getInstance().updateDevices();
            return true;
        }
    }


    public void removeRegistration(String robotName) {
        logger.log(Level.INFO,"Remove registration: "+robotName);
        Destination dest = new Destination(robotName);

        getIPMapping().remove(dest);
        getSocketMapping().remove(dest);

        ConnectedDevicesFrame.getInstance().updateDevices();
    }


    public InetSocketAddress getAddress(Destination destination){
        return ipMapping.get(destination);
    }

    public Socket getSocket(Destination destination){
        return socketMapping.get(destination);
    }

    public Map<Destination, InetSocketAddress> getIPMapping(){
        return ipMapping;
    }

    public Map<Destination, Socket> getSocketMapping(){
        return socketMapping;
    }

    public Map<Destination, MindroidServerWorker> getWorkerMapping() {
        return workerMapping;
    }

    private static final String regexIP = "((((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)[.]){3})(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?){1}){1}";

    public static boolean isValidIP(String ip) {
        if(ip == null){
            System.err.println("[Server] UserManagement: IP is null");
            return false;
        }
        return ip.matches(regexIP);
    }

}
