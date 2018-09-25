package org.mindroid.server.app.util;

import org.mindroid.common.messages.server.Destination;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

public class IPService {

    private static HashMap<Destination, InetSocketAddress> ipMapping = new HashMap<>();
    private static HashMap<Destination, Socket> socketMapping = new HashMap<>();

    public static InetSocketAddress findAddress(Destination destination) {
        return ipMapping.get(destination);
    }

    public static Socket findSocket(Destination destination){
        return socketMapping.get(destination);
    }

    public static HashMap<Destination, InetSocketAddress> getIPMapping() {return ipMapping;}

    public static HashMap<Destination, Socket> getSocketMapping() {
        return socketMapping;
    }


    private static final String regexIP = "((((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)[.]){3})(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?){1}){1}";

    public static boolean isValidIP(String ip) {
        if(ip == null){
            System.err.println("[Server] IPService: IP is null");
            return false;
        }
        return ip.matches(regexIP);
    }

}
