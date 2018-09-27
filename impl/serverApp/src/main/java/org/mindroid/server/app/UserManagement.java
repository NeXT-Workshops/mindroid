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

    private Logger logger;

    private Map<RobotId, InetSocketAddress> ipMapping = new ConcurrentHashMap<>();
    private Map<RobotId, Socket> socketMapping = new ConcurrentHashMap<>();
    private Map<RobotId, MindroidServerWorker> workerMapping = new ConcurrentHashMap<>();
    
    private static UserManagement ourInstance = new UserManagement();

    public static UserManagement getInstance(){
        return ourInstance;
    }

    private UserManagement(){
        logger = LogManager.getLogger(UserManagement.class);
    }

    public boolean registerRobot(RobotId robotId, MindroidServerWorker msWorker, Socket socket, int port) {
        logger.log(Level.INFO,"Registering robot: "+robotId.getValue() + " with IP " + socket.getInetAddress().toString().replace("/",""));

        InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

        if(ipMapping.containsKey(robotId) || socketMapping.containsKey(robotId)){
            //Reject registration
            return false;
        }else {
            ipMapping.put(robotId, new InetSocketAddress(remoteSocketAddress.getAddress(), port));
            socketMapping.put(robotId, socket);
            workerMapping.put(robotId, msWorker);

            ConnectedDevicesFrame.getInstance().updateDevices();
            return true;
        }
    }

    public void removeAllUsers(){
        for (RobotId destination : ipMapping.keySet()) {
            removeUserAndCloseConnection(destination);
        }
    }

    public void removeUserAndCloseConnection(RobotId robot){
        logger.log(Level.INFO,"Remove User ["+ robot.getValue() +"] and close Connection");

        //Close connections
        workerMapping.get(robot).closeConnection();

        //remove registration
        removeRegistration(robot);

        //Update ConnectedDevicesFrame
        ConnectedDevicesFrame.getInstance().updateDevices();
    }

    private void removeRegistration(RobotId robot) {
        logger.log(Level.INFO,"Remove registration of [" + robot.getValue() + "]");
        
        ipMapping.remove(robot);
        socketMapping.remove(robot);
        workerMapping.remove(robot);

        ConnectedDevicesFrame.getInstance().updateDevices();
    }

    public InetSocketAddress getAddress(RobotId robot){
        return ipMapping.get(robot);
    }

    public Socket getSocket(RobotId robot){
        return socketMapping.get(robot);
    }

    public RobotId[] getRobotIdsArray(){
        return ipMapping.keySet().toArray(new RobotId[UserManagement.getInstance().ipMapping.keySet().size()]);
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
