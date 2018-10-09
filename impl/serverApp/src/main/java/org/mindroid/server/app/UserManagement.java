package org.mindroid.server.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.common.messages.server.RobotId;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManagement {

    private Logger LOGGER;

    private Map<RobotId, InetSocketAddress> ipMapping = new ConcurrentHashMap<>();
    private Map<RobotId, Socket> socketMapping = new ConcurrentHashMap<>();
    private Map<RobotId, MindroidServerWorker> workerMapping = new ConcurrentHashMap<>();

    private static UserManagement ourInstance = new UserManagement();

    public static UserManagement getInstance(){
        return ourInstance;
    }

    private UserManagement(){
        LOGGER = LogManager.getLogger(UserManagement.class);
    }

    public boolean requestRegistration(RobotId robotId, MindroidServerWorker msWorker, Socket socket, int port) {
        if(ipMapping.containsKey(robotId)){
            InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            LOGGER.info("Saved IP: " + ipMapping.get(robotId).getAddress());
            LOGGER.info("Connecting IP: " + remoteAddress.getAddress());
            if(ipMapping.get(robotId).getAddress().equals(remoteAddress.getAddress())){
                // Robot is known, tries to connect with known IP address -> robot is reconnecting, so allow
                LOGGER.log(Level.INFO,"Reconnecting robot: "+robotId.getValue() + " with IP " + socket.getInetAddress().toString().replace("/",""));
                removeUserAndCloseConnection(robotId);
                registerRobot(robotId, msWorker, port);
                return true;
            }else{
                // Connecting Known Name with new IP, reject registration
                msWorker.getMindroidServerFrame().addLocalContentLine("WARNING", "The Connection of "+robotId.getValue()+" got rejected! A Robot with that ID is already connected. Change RobotID to connect.");
                LOGGER.warn("The Connection of "+robotId.getValue()+" got rejected! A Robot with that ID is already connected. Change RobotID to connect.");
                return false;
            }
        }else {
            // New Robot, normal registration
            LOGGER.log(Level.INFO,"Registering robot: "+robotId.getValue() + " with IP " + socket.getInetAddress().toString().replace("/",""));
            registerRobot(robotId, msWorker, port);
            return true;
        }
    }

    private void registerRobot(RobotId robotId, MindroidServerWorker msWorker, int port){
        ipMapping.put(robotId, new InetSocketAddress(((InetSocketAddress)msWorker.getSocket().getRemoteSocketAddress()).getAddress(), port));;
        socketMapping.put(robotId, msWorker.getSocket());
        workerMapping.put(robotId, msWorker);

        ConnectedDevicesFrame.getInstance().updateDevices();
    }


    public void removeAllUsers(){
        for (RobotId robot : ipMapping.keySet()) {
            removeUserAndCloseConnection(robot);
        }
    }

    public void removeUserAndCloseConnection(RobotId robot){
        LOGGER.log(Level.INFO,"Remove User ["+ robot.getValue() +"] and close Connection");

        // Close connections
        workerMapping.get(robot).closeConnection();

        // remove registration
        ipMapping.remove(robot);
        socketMapping.remove(robot);
        workerMapping.remove(robot);

        // Update ConnectedDevicesFrame
        ConnectedDevicesFrame.getInstance().updateDevices();
    }

    public InetSocketAddress getAddress(RobotId robot){
        return ipMapping.get(robot);
    }

    public Socket getSocket(RobotId robot){
        return socketMapping.get(robot);
    }

    public RobotId[] getRobotIdsArray(){
        return ipMapping.keySet().toArray(new RobotId[ipMapping.keySet().size()]);
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
